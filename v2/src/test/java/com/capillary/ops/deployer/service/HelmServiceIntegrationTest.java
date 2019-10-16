package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.App;
import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.repository.ApplicationRepository;
import com.capillary.ops.deployer.repository.DeploymentRepository;
import com.capillary.ops.deployer.repository.EnvironmentRepository;
import com.capillary.ops.deployer.service.interfaces.IHelmService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import io.fabric8.kubernetes.api.model.ContainerPort;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import org.apache.commons.io.IOUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {App.class})
@ActiveProfiles({"helminttest", "dev"})
public class HelmServiceIntegrationTest {

    @Autowired
    public IHelmService helmService;

    @Autowired
    public EnvironmentRepository environmentRepository;

    @Autowired
    public ApplicationRepository applicationRepository;

    @Autowired
    private DeploymentRepository deploymentRepository;

    private String clusterName;
    private File kindExecutable;
    private File kubectlExecutable;
    private File helmExecutable;
    private String kubeConfigPath;
    private String k8sToken;
    private String apiEndpoint;
    private DefaultKubernetesClient kubernetesClient;
    private Logger logger = LoggerFactory.getLogger(HelmServiceIntegrationTest.class);

    @Before
    public void setUp() throws Exception {
        clusterName = "deployer-helminttest-" + System.currentTimeMillis();
        kindExecutable = getExecutable("kind-darwin-amd64");
        kubectlExecutable = getExecutable("kubectl");
        helmExecutable = getExecutable("helm");
        createCluster();
        kubeConfigPath = getKubeConfigPath();
        installTiller();
        k8sToken = getK8sToken();
        apiEndpoint = getApiEndpoint();
        kubernetesClient = new DefaultKubernetesClient(
                new ConfigBuilder()
                        .withMasterUrl(apiEndpoint)
                        .withOauthToken(k8sToken)
                        .withTrustCerts(true)
                        .withClientCertData(null)
                        .withCaCertData(null)
                        .withClientKeyData(null)
                        .withUsername(null)
                        .withPassword(null)
                        .withWebsocketTimeout(60*1000)
                        .withConnectionTimeout(30*1000)
                        .withRequestTimeout(30*1000)
                        .build());
    }

    @Test
    public void testInstall() {
        Environment localEnvironment = createLocalEnvironment(ApplicationFamily.CRM, EnvironmentType.PRODUCTION);
        Application application = createApplication("helmint-test-1", ApplicationFamily.CRM);
        Deployment deployment = createDeployment(application);
        helmService.deploy(application, deployment);
        io.fabric8.kubernetes.api.model.extensions.Deployment k8sdeployment = kubernetesClient.extensions().deployments().inNamespace("default").withName(application.getName()).get();
        String image = k8sdeployment.getSpec().getTemplate().getSpec().getContainers().get(0).getImage();
        Assert.assertEquals("nginx:latest", image);
        List<ContainerPort> ports = k8sdeployment.getSpec().getTemplate().getSpec().getContainers().get(0).getPorts();
        Assert.assertTrue(ports.stream().anyMatch(x -> x.getContainerPort().equals(application.getPorts().get(0).getContainerPort().intValue())));
        Assert.assertTrue(ports.stream().anyMatch(x -> x.getContainerPort().equals(application.getPorts().get(1).getContainerPort().intValue())));
        List<Pod> pods = kubernetesClient.pods().inNamespace("default").list().getItems().stream().filter(
                pod -> pod.getMetadata().getAnnotations() != null &&
                        deployment.getId().equalsIgnoreCase(pod.getMetadata().getAnnotations().get("deploymentId"))
        ).filter(pod -> pod.getMetadata().getAnnotations().get("buildId").equalsIgnoreCase(deployment.getBuildId()))
                .filter(pod -> pod.getMetadata().getLabels().get("app").equalsIgnoreCase(application.getName()))
                .collect(Collectors.toList());
        Service service = kubernetesClient.services().inNamespace("default").withName(application.getName()).get();
        Assert.assertEquals("0.0.0.0/0", service.getMetadata().getAnnotations().get("service.beta.kubernetes.io/aws-load-balancer-internal"));
        Assert.assertEquals("true", service.getMetadata().getAnnotations().get("service.beta.kubernetes.io/azure-load-balancer-internal"));
        Assert.assertEquals("helmint-test-1-dns.local.internal", service.getMetadata().getAnnotations().get("external-dns.alpha.kubernetes.io/hostname"));
        Assert.assertEquals(1, pods.size());
    }

    @After
    public void tearDown() throws Exception {
        destroyCluster();
    }

    private void createCluster() throws IOException, InterruptedException {
        runCommand(new String[]{kindExecutable.getAbsolutePath(), "create", "cluster", "--name", clusterName});
    }

    private void destroyCluster() throws IOException, InterruptedException {
        runCommand(new String[]{kindExecutable.getAbsolutePath(), "delete", "cluster", "--name", clusterName});
    }

    private String getKubeConfigPath() throws IOException, InterruptedException {
        String out = runCommand(new String[]{kindExecutable.getAbsolutePath(), "get", "kubeconfig-path", "--name", clusterName});
        return out;
    }

    private String runCommand(String[] command) throws IOException, InterruptedException {
        Map<String, String> additionalEnv = new HashMap<>();
        if(kubeConfigPath != null) {
            additionalEnv.put("KUBECONFIG", kubeConfigPath);
        }
        return runCommand(command, additionalEnv);

    }

    private String runCommand(String[] command, Map<String, String> env) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.environment().putAll(env);
        Process process = processBuilder.start();
        process.waitFor();
        String result = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
        return result.trim();
    }

    private File getExecutable(String binaryName) {
        try {
            InputStream resourceAsStream = HelmServiceIntegrationTest.class.getResourceAsStream("/bin/" + binaryName);
            File executable = File.createTempFile(binaryName, String.valueOf(System.currentTimeMillis()));
            Files.copy(resourceAsStream, executable.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.setPosixFilePermissions(executable.toPath(), ImmutableSet.of(PosixFilePermission.OWNER_EXECUTE));
            return executable;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private void installTiller() throws IOException, InterruptedException {
        runCommand(new String[]{
                kubectlExecutable.getAbsolutePath(),
                "create", "clusterrolebinding", "helm", "--clusterrole", "cluster-admin",
                "--serviceaccount", "kube-system:tiller-deploy",
        });
        runCommand(new String[]{
                kubectlExecutable.getAbsolutePath(),
                "create", "sa", "tiller-deploy", "-n", "kube-system"});
        runCommand(new String[]{
                        helmExecutable.getAbsolutePath(),
                        "init", "--service-account", "tiller-deploy", "--wait"});
        runCommand(new String[]{
                        kubectlExecutable.getAbsolutePath(),
                        "get", "po", "-n", "kube-system"});
    }

    private String getK8sToken() throws IOException, InterruptedException {
        String result = runCommand(new String[]{
                kubectlExecutable.getAbsolutePath(),
                "get", "sa", "tiller-deploy", "-n", "kube-system", "-o", "json"});
        JsonNode jsonNode = new ObjectMapper().readTree(result);
        String secretname = jsonNode.get("secrets").get(0).get("name").asText();
        logger.info("Access token " + secretname);
        result = runCommand(new String[]{
                kubectlExecutable.getAbsolutePath(),
                "get", "secret", secretname, "-n", "kube-system", "-o", "json"});
        jsonNode = new ObjectMapper().readTree(result);
        String token = new String(Base64.getDecoder().decode(jsonNode.get("data").get("token").asText()));
        return token;
    }

    private Environment createLocalEnvironment(ApplicationFamily applicationFamily, EnvironmentType environmentType) {
        logger.info("api " + apiEndpoint + " token " + k8sToken);
        EnvironmentMetaData environmentMetaData = new EnvironmentMetaData(environmentType, clusterName, applicationFamily);
        EnvironmentConfiguration environmentConfiguration = new EnvironmentConfiguration();
        environmentConfiguration.setKubernetesApiEndpoint(apiEndpoint);
        environmentConfiguration.setKubernetesToken(k8sToken);
        environmentConfiguration.setPrivateDnsConfiguration(new ExternalDnsConfiguration("", "", "", "local.internal"));
        Environment environment = new Environment(environmentMetaData, environmentConfiguration);
        environmentRepository.save(environment);
        return environment;
    }

    private String getApiEndpoint() throws IOException, InterruptedException {
        String result = runCommand(new String[]{
                kindExecutable.getAbsolutePath(),
                "get", "kubeconfig", "--name", clusterName});
        Object load = new Yaml().load(result);
        return (String) ((Map) ((Map)((List) ((Map) load).get("clusters")).get(0)).get("cluster")).get("server");
    }

    private Application createApplication(String name, ApplicationFamily applicationFamily) {
        Port port1 = new Port("http", 8080L, 80L);
        Port port2 = new Port("https", 8081L, 443L);

        Probe readinessProbe = new Probe(8080, 1, 2, null, 1, 4, 5);
        Probe livelinessProbe = new Probe(8081, 6, 7, null, 1, 9, 10);

        HealthCheck healthCheck = new HealthCheck(livelinessProbe, readinessProbe);
        Application app = new Application(Application.ApplicationType.SERVICE, name,
                VCSProvider.GITHUB, "", "",
                Arrays.asList(port1, port2), LoadBalancerType.INTERNAL,
                new ArrayList<>(),
                BuildType.MVN, applicationFamily, name + "-dns", healthCheck,
                Application.DnsType.PRIVATE, new HashMap<>());
        applicationRepository.save(app);
        return app;
    }

    private Deployment createDeployment(Application application) {
        List<EnvironmentVariable> environmentVariables = Arrays.asList(
                new EnvironmentVariable("TZ", "Asia/Kolkata"),
                new EnvironmentVariable("KEY1", "VALUE1"));
        HPA hpa = new HPA(50, 1, 4);
        Deployment dep = new Deployment(application.getApplicationFamily(), application.getId(), "nginx:latest", "xyz",
                clusterName, environmentVariables, new Date(), PodSize.LARGE, hpa, null,
                false, "me");
        deploymentRepository.save(dep);
        return dep;
    }

}