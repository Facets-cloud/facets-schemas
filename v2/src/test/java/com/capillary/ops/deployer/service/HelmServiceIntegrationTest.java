package com.capillary.ops.deployer.service;

import com.capillary.ops.App;
import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.bo.Probe;
import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.repository.ApplicationRepository;
import com.capillary.ops.deployer.repository.DeploymentRepository;
import com.capillary.ops.deployer.repository.EnvironmentRepository;
import com.capillary.ops.deployer.service.interfaces.IIAMService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import hapi.chart.ChartOuterClass;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import mockit.Expectations;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.Parameterized;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;
import org.microbean.helm.chart.URLChartLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@ContextConfiguration(classes = {App.class, TestConfiguration.class})
@ActiveProfiles({"helminttest", "dev"})
@TestPropertySource("/application-dev.properties")
@RunWith(Parameterized.class)
@Parameterized.UseParametersRunnerFactory(HelmServiceIntegrationTest.SpringParametersRunnerFactory.class)
public class HelmServiceIntegrationTest {

    public static final String V12 = "v1.12.10";
    public static final String V14 = "v1.14.9";

    public static class SpringParametersRunnerFactory implements ParametersRunnerFactory {
        @Override
        public Runner createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
            final BlockJUnit4ClassRunnerWithParameters runnerWithParameters = new BlockJUnit4ClassRunnerWithParameters(test);
            return new SpringJUnit4ClassRunner(test.getTestClass().getJavaClass()) {
                @Override
                protected Object createTest() throws Exception {
                    final Object testInstance = runnerWithParameters.createTest();
                    getTestContextManager().prepareTestInstance(testInstance);
                    return testInstance;
                }
            };
        }
    }
    @Parameterized.Parameters(name = "{index}: Test with version={0}")
    public static Collection<String> data() {
        String[] data = new String[] { V12, V14 };
        return Arrays.asList(data);
    }

    @Parameterized.Parameter
    public String version;

    private static String OS = System.getProperty("os.name").toLowerCase();

    @Autowired
    public HelmService helmService;

    @Autowired
    public EnvironmentRepository environmentRepository;

    @Autowired
    public ApplicationRepository applicationRepository;

    @Autowired
    private DeploymentRepository deploymentRepository;

    @Autowired
    private IIAMService iamService;

    @Autowired
    private SecretService secretService;

    private String clusterName;
    private File kindExecutable;
    private File kubectlExecutable;
    private File helmExecutable;
    private String kubeConfigPath;
    private String k8sToken;
    private String apiEndpoint;
    private DefaultKubernetesClient kubernetesClient;
    private Logger logger = LoggerFactory.getLogger(HelmServiceIntegrationTest.class);
    private Environment localCRMEnvironment;

    @Before
    public void setUp() throws Exception {
        System.out.println("Setup started with K8s version: " + version);
        clusterName = "deployer-helminttest-" + System.currentTimeMillis();
        kindExecutable = getExecutable("kind");
        kubectlExecutable = getExecutable("kubectl");
        helmExecutable = getExecutable("helm");
        createCluster(version);
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
        localCRMEnvironment = createLocalEnvironment(ApplicationFamily.CRM, EnvironmentType.PRODUCTION);
    }

    @Test
    public void testInstall() throws Exception {
        URLChartLoader chartLoader = new URLChartLoader();
        ChartOuterClass.Chart.Builder chart = chartLoader.load(this.getClass().getResource("/charts/capillary-base"));
        new Expectations(helmService) {
            {
                helmService.getChart("capillary-base");
                result = chart;
            }
        };

        Application application = createApplication("helmint-test-1", ApplicationFamily.CRM);
        final String applicationName = application.getName();
        Deployment deployment = createDeployment(application);

        new Expectations(iamService) {
            {
                iamService.getApplicationRole((Application) any, (Environment) any);
                result = applicationName + "-role";
            }
        };
        // deploy new app, internal

        helmService.deploy(application, deployment);
        io.fabric8.kubernetes.api.model.apps.Deployment k8sdeployment = kubernetesClient.apps().deployments().inNamespace("default").withName(application.getName()).get();
        Container container =
            k8sdeployment.getSpec().getTemplate().getSpec().getContainers()
                .stream()
                .filter(x -> x.getName().equals(application.getName()))
                .findFirst().get();
        Optional<Container> sidecarO =
            k8sdeployment.getSpec().getTemplate().getSpec().getContainers()
                .stream()
                .filter(x -> x.getName().equals("jmx"))
                .findFirst();
        Assert.assertTrue(sidecarO.isPresent());
        String image = container.getImage();
        Assert.assertEquals("nginx:latest", image);

        List<ContainerPort> ports = container.getPorts();
        Assert.assertTrue(ports.stream().anyMatch(x -> x.getContainerPort().equals(application.getPorts().get(0).getContainerPort().intValue())));
        Assert.assertTrue(ports.stream().anyMatch(x -> x.getContainerPort().equals(application.getPorts().get(1).getContainerPort().intValue())));
        final List<Pod> pods = kubernetesClient.pods().inNamespace("default").list().getItems().stream().filter(
                pod -> pod.getMetadata().getAnnotations() != null &&
                        deployment.getId().equalsIgnoreCase(pod.getMetadata().getAnnotations().get("deploymentId"))
        ).filter(pod -> pod.getMetadata().getAnnotations().get("buildId").equalsIgnoreCase(deployment.getBuildId()))
                .filter(pod -> pod.getMetadata().getLabels().get("app").equalsIgnoreCase(application.getName()))
                .collect(Collectors.toList());
        Service service = kubernetesClient.services().inNamespace("default").withName(application.getName() + "-test").get();
        Assert.assertEquals("0.0.0.0/0", service.getMetadata().getAnnotations().get("service.beta.kubernetes.io/aws-load-balancer-internal"));
        Assert.assertEquals("true", service.getMetadata().getAnnotations().get("service.beta.kubernetes.io/azure-load-balancer-internal"));
        Assert.assertEquals(applicationName+ "-dns.local.internal", service.getMetadata().getAnnotations().get(
            "external-dns.alpha.kubernetes.io/hostname"));
        Assert.assertEquals(1, pods.size());
        if(version.equals(V12)) {
            Assert.assertNull(pods.get(0).getSpec().getEnableServiceLinks());
        }
        if(version.equals(V14)) {
            Assert.assertFalse(pods.get(0).getSpec().getEnableServiceLinks());
        }

        deployment.getConfigurations().forEach(
                config -> {
                List <EnvVar> env1 = pods.get(0).getSpec().getContainers().get(0).getEnv().stream()
                        .filter(x -> config.getName().equals(x.getName()))
                        .collect(Collectors.toList());
                Assert.assertEquals(1, env1.size());
                String env1SecretName = env1.get(0).getValueFrom().getSecretKeyRef().getName();
                Assert.assertEquals(applicationName + "-configs", env1SecretName);
                Secret configsSecret = kubernetesClient.secrets().inNamespace("default").withName(
                    applicationName + "-configs").get();
                Assert.assertEquals(config.getValue(),
                        new String(Base64.getDecoder().decode(configsSecret.getData().get(config.getName()))));
        });

        Assert.assertEquals(applicationName + "-role", pods.get(0).getMetadata().getAnnotations().get("iam.amazonaws.com/role"));

        //ZK publish
        Assert.assertEquals("TEST_ZK_SERVICE_NAME_1,TEST_ZK_SERVICE_NAME_2", service.getMetadata().getAnnotations().get("zk-name"));
        Assert.assertEquals("false", service.getMetadata().getLabels().get("zk-publish"));

        // update secrets and env
        secretService.initializeApplicationSecrets(
                Arrays.asList(new ApplicationSecretRequest(application.getApplicationFamily(),
                        application.getId(), "CREDENTIAL1", "")));
        secretService.updateApplicationSecrets(clusterName, application.getApplicationFamily(),
                application.getId(),
                Arrays.asList(
                        new ApplicationSecret(clusterName, application.getApplicationFamily(), application.getId(),
                                "CREDENTIAL1", "CREDENTIAL_VALUE1",
                                ApplicationSecret.SecretStatus.FULFILLED)));
        secretService.initializeApplicationSecrets(
                Arrays.asList(new ApplicationSecretRequest(application.getApplicationFamily(),
                        application.getId(), "CREDENTIALFILE1", "",
                        ApplicationSecretRequest.SecretType.FILE, "/etc/nginx/nginx2.conf")));
        secretService.updateApplicationSecrets(clusterName, application.getApplicationFamily(),
                application.getId(),
                Arrays.asList(
                        new ApplicationSecret(clusterName, application.getApplicationFamily(), application.getId(),
                                "CREDENTIALFILE1", Base64.getEncoder().encodeToString("CREDENTIALFILE1".getBytes()),
                                ApplicationSecret.SecretStatus.FULFILLED)));
        secretService.initializeApplicationSecrets(
                Arrays.asList(new ApplicationSecretRequest(application.getApplicationFamily(),
                        application.getId(), "CREDENTIALFILE2", "",
                        ApplicationSecretRequest.SecretType.FILE, "/etc/nginx/nginx.conf")));
        secretService.updateApplicationSecrets(clusterName, application.getApplicationFamily(),
                application.getId(),
                Arrays.asList(
                        new ApplicationSecret(clusterName, application.getApplicationFamily(), application.getId(),
                                "CREDENTIALFILE2", Base64.getEncoder().encodeToString("CREDENTIALFILE2".getBytes()),
                                ApplicationSecret.SecretStatus.FULFILLED)));

        Deployment updatedDeployment = createDeployment(application);
        ArrayList<EnvironmentVariable> updatedEnvironmentVariables = new ArrayList<>(deployment.getConfigurations());
        updatedEnvironmentVariables.get(0).setValue("UTC");
        updatedEnvironmentVariables.add(new EnvironmentVariable("NEWCONFIG",  "NEWCONFIGVALUE"));
        deployment.setConfigurations(updatedEnvironmentVariables);
        updatedDeployment.setBuildId("newbuild");
        helmService.deploy(application, updatedDeployment);

        List<Pod> updatedPods = kubernetesClient.pods().inNamespace("default").list().getItems().stream().filter(
                pod -> pod.getMetadata().getAnnotations() != null &&
                        updatedDeployment.getId().equalsIgnoreCase(pod.getMetadata().getAnnotations().get("deploymentId"))
        ).filter(pod -> pod.getMetadata().getAnnotations().get("buildId").equalsIgnoreCase(updatedDeployment.getBuildId()))
                .filter(pod -> pod.getMetadata().getLabels().get("app").equalsIgnoreCase(application.getName()))
                .collect(Collectors.toList());

        // provided configs
        updatedDeployment.getConfigurations().forEach(
            config -> {
                List <EnvVar> env1 = updatedPods.get(0).getSpec().getContainers().get(0).getEnv().stream()
                        .filter(x -> config.getName().equals(x.getName()))
                        .collect(Collectors.toList());
                Assert.assertEquals(1, env1.size());
                String env1SecretName = env1.get(0).getValueFrom().getSecretKeyRef().getName();
                Assert.assertEquals(applicationName + "-configs", env1SecretName);
                Secret configsSecret = kubernetesClient.secrets().inNamespace("default").withName(
                    applicationName + "-configs").get();
                Assert.assertEquals(config.getValue(),
                        new String(Base64.getDecoder().decode(configsSecret.getData().get(config.getName()))));
            });

        // cluster default configs
        localCRMEnvironment.getEnvironmentConfiguration().getCommonConfigs().entrySet().stream().forEach(
            commonConfig -> {
                List <EnvVar> env1 = updatedPods.get(0).getSpec().getContainers().get(0).getEnv().stream()
                        .filter(x -> commonConfig.getKey().equals(x.getName()))
                        .collect(Collectors.toList());
                Assert.assertEquals(1, env1.size());
                String env1SecretName = env1.get(0).getValueFrom().getSecretKeyRef().getName();
                Assert.assertEquals(applicationName + "-configs", env1SecretName);
                Secret configsSecret = kubernetesClient.secrets().inNamespace("default").withName(
                    applicationName + "-configs").get();
                Assert.assertEquals(commonConfig.getValue(),
                        new String(Base64.getDecoder().decode(configsSecret.getData().get(commonConfig.getKey()))));
            }
        );


        // cluster default credentials
        localCRMEnvironment.getEnvironmentConfiguration().getCommonCredentials().entrySet().stream().forEach(
            commonCredential -> {
                List <EnvVar> env1 = updatedPods.get(0).getSpec().getContainers().get(0).getEnv().stream()
                        .filter(x -> commonCredential.getKey().equals(x.getName()))
                        .collect(Collectors.toList());
                Assert.assertEquals(1, env1.size());
                String env1SecretName = env1.get(0).getValueFrom().getSecretKeyRef().getName();
                Assert.assertEquals(applicationName + "-credentials", env1SecretName);
                Secret configsSecret = kubernetesClient.secrets().inNamespace("default").withName(
                    applicationName + "-credentials").get();
                Assert.assertEquals(commonCredential.getValue(),
                        new String(Base64.getDecoder().decode(configsSecret.getData().get(commonCredential.getKey()))));
            }
        );

        // provided credentials
        Map<String, ApplicationSecretRequest> requests =
                secretService.getApplicationSecretRequests(application.getApplicationFamily(), application.getId())
                .stream().collect(Collectors.toMap(ApplicationSecretRequest::getSecretName, Function.identity()));
        secretService.getApplicationSecrets(clusterName, application.getApplicationFamily(), application.getId())
            .stream().filter(x -> x.getSecretStatus().equals(ApplicationSecret.SecretStatus.FULFILLED))
            .filter(x -> requests.get(x.getSecretName()).getSecretType().equals(ApplicationSecretRequest.SecretType.ENVIRONMENT))
            .forEach(
            credential -> {
                List <EnvVar> env1 = updatedPods.get(0).getSpec().getContainers().get(0).getEnv().stream()
                        .filter(x -> credential.getSecretName().equals(x.getName()))
                        .collect(Collectors.toList());
                Assert.assertEquals(1, env1.size());
                String env1SecretName = env1.get(0).getValueFrom().getSecretKeyRef().getName();
                Assert.assertEquals(applicationName + "-credentials", env1SecretName);
                Secret configsSecret = kubernetesClient.secrets().inNamespace("default").withName(
                    applicationName + "-credentials").get();
                Assert.assertEquals(credential.getSecretValue(),
                        new String(Base64.getDecoder().decode(configsSecret.getData().get(credential.getSecretName()))));
            });

        Optional<VolumeMount> dumpsVolumeCreated = updatedPods.get(0).getSpec().getContainers().get(0).getVolumeMounts().stream()
                .filter(x -> x.getMountPath().equals("/var/log/dumps"))
                .findFirst();
        Assert.assertTrue(dumpsVolumeCreated.isPresent());
        Assert.assertEquals(dumpsVolumeCreated.get().getName(), "dump-vol");
        Optional<Volume> logVolumeOptional = updatedPods.get(0).getSpec().getVolumes().stream().filter(x -> x.getName().equals("dump-vol")).findFirst();
        Assert.assertTrue(logVolumeOptional.isPresent());
        Assert.assertEquals("/var/log/dumps/" + application.getName(), logVolumeOptional.get().getHostPath().getPath());


        Optional<VolumeMount> credentialFileMountCreated = updatedPods.get(0).getSpec().getContainers().get(0).getVolumeMounts().stream()
                .filter(x -> x.getMountPath().equals("/etc/credentialfiles/"))
                .findFirst();
        Assert.assertTrue(credentialFileMountCreated.isPresent());

        // update to public app, check ssl and annotations
        application.setLoadBalancerType(LoadBalancerType.EXTERNAL);
        application.setDnsType(Application.DnsType.PUBLIC);
        helmService.deploy(application, updatedDeployment);
        Service updatedService = kubernetesClient.services().inNamespace("default").withName(application.getName() + "-test").get();
        String updatedDns = updatedService.getMetadata().getAnnotations().get("external-dns.alpha.kubernetes.io/hostname");
        Assert.assertEquals(clusterName + "-" + application.getName() + "-dns.local.public", updatedDns);
        Assert.assertFalse(updatedService.getMetadata().getAnnotations().containsKey("service.beta.kubernetes.io/aws-load-balancer-internal"));
        Assert.assertFalse(updatedService.getMetadata().getAnnotations().containsKey("service.beta.kubernetes.io/azure-load-balancer-internal"));
        Assert.assertEquals(localCRMEnvironment.getEnvironmentConfiguration().getSslConfigs().getSSLCertName(),
                updatedService.getMetadata().getAnnotations().get("service.beta.kubernetes.io/aws-load-balancer-ssl-cert"));
        Assert.assertEquals("https",
                updatedService.getMetadata().getAnnotations().get("service.beta.kubernetes.io/aws-load-balancer-ssl-ports"));
    }

    @Test
    public void testCronInstall() throws Exception {
        URLChartLoader chartLoader = new URLChartLoader();
        ChartOuterClass.Chart.Builder chart = chartLoader.load(this.getClass().getResource("/charts/capillary-base-cronjob"));
        new Expectations(helmService) {
            {
                helmService.getChart("capillary-base-cronjob");
                result = chart;
            }
        };
        Application application = createApplication("helmint-test-cron-1", ApplicationFamily.CRM);
        Deployment deployment = createDeployment(application);
        application.setApplicationType(Application.ApplicationType.SCHEDULED_JOB);
        deployment.setSchedule("*/1 * * * *");
        deployment.setImage("hello-world:latest");
        helmService.deploy(application, deployment);
        CronJob cronJob = kubernetesClient.batch().cronjobs().inNamespace("default").withName(application.getName()).get();
        Assert.assertEquals("hello-world:latest", cronJob.getSpec().getJobTemplate().getSpec().getTemplate().getSpec().getContainers().get(0).getImage());
        Assert.assertEquals("*/1 * * * *", cronJob.getSpec().getSchedule());
        Assert.assertEquals(10, cronJob.getSpec().getSuccessfulJobsHistoryLimit().longValue());
        Assert.assertEquals(10, cronJob.getSpec().getFailedJobsHistoryLimit().longValue());
    }

    @Test
    public void testHttpOnlyConfig() throws Exception {
        URLChartLoader chartLoader = new URLChartLoader();
        ChartOuterClass.Chart.Builder chart = chartLoader.load(this.getClass().getResource("/charts/capillary-base"));
        new Expectations(helmService) {
            {
                helmService.getChart("capillary-base");
                result = chart;
            }
        };

        Application application = createApplication("helmint-test-service-01", ApplicationFamily.CRM);
        List<Port> updatedPorts = new ArrayList<>();
        Deployment deployment = createDeployment(application);
        updatedPorts.add(new Port("http",80L,80L,Port.Protocol.HTTP));
        application.setPorts(updatedPorts);
        application.setLoadBalancerType(LoadBalancerType.INTERNAL);
        application.setDnsType(Application.DnsType.PRIVATE);
        helmService.deploy(application, deployment);
        //kubernetesClient.services().inNamespace("default").withName(application.getName()).delete();
        Service service = kubernetesClient.services().inNamespace("default").withName(application.getName()+ "-test").get();

        Assert.assertTrue(service.getMetadata().getAnnotations().containsKey("service.beta.kubernetes.io/aws-load-balancer-internal"));
        Assert.assertEquals("300",service.getMetadata().getAnnotations().get("service.beta.kubernetes.io/aws-load-balancer-connection-idle-timeout"));
        Assert.assertEquals("http",service.getMetadata().getAnnotations().get("service.beta.kubernetes.io/aws-load-balancer-backend-protocol"));
    }

    @After
    public void tearDown() throws Exception {
        destroyCluster();
    }

    private void createCluster(String version) throws IOException, InterruptedException {
        runCommand(new String[]{kindExecutable.getAbsolutePath(), "create", "cluster", "--name", clusterName, "--image", "kindest/node:" + version});
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
            String prefix = "osx";
            if (OS.toLowerCase().contains("linux") || OS.toLowerCase().contains("nix")) {
                prefix = "linux";
            }
            InputStream resourceAsStream = HelmServiceIntegrationTest.class.getResourceAsStream("/bin/"
                    + prefix + "/" + binaryName);
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
        environmentConfiguration.setPublicDnsConfiguration(new ExternalDnsConfiguration("", "", "", "local.public"));
        environmentConfiguration.setSslConfigs(new SSLConfigs("somecert"));
        environmentConfiguration.setCommonConfigs(ImmutableMap.of("commonconfig1", "commonconfig1value", "commonconfig2", "commonconfig2value"));
        environmentConfiguration.setCommonCredentials(ImmutableMap.of("commoncredential1", "commoncredential1value", "commoncredential2", "commoncredential2value"));
        environmentConfiguration.setKube2IamConfiguration(new Kube2IamConfiguration(true, "", ""));
        environmentConfiguration.setJmxSideCarEnabled(true);
        environmentConfiguration.setPreDeployTaskEnabled(true);
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
        String replacedVersion = version.replace(".", "-");
        name = name + replacedVersion;
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
                new EnvironmentVariable("KEY1", "VALUE1"),
                new EnvironmentVariable("zkPublish", "false"),
                new EnvironmentVariable("zkName", "TEST_ZK_SERVICE_NAME_1,TEST_ZK_SERVICE_NAME_2"));
        HPA hpa = new HPA(50, 1, 4);
        Deployment dep = new Deployment(application.getApplicationFamily(), application.getId(), "nginx:latest", "xyz",
                clusterName, environmentVariables, new Date(), PodSize.LARGE, hpa, null,
                false, "me");
        deploymentRepository.save(dep);
        return dep;
    }

    @Test
    public void testInstallNoPorts() throws Exception {
        URLChartLoader chartLoader = new URLChartLoader();
        ChartOuterClass.Chart.Builder chart = chartLoader.load(this.getClass().getResource("/charts/capillary-base"));
        new Expectations(helmService) {
            {
                helmService.getChart("capillary-base");
                result = chart;
            }
        };

        Application application = createApplication("helmint-test-noports-1", ApplicationFamily.CRM);
        application.setPorts(new ArrayList<>());
        Deployment deployment = createDeployment(application);
        helmService.deploy(application, deployment);
    }

    @Test
    public void testStatefulSetInstall() throws Exception {
        URLChartLoader chartLoader = new URLChartLoader();
        ChartOuterClass.Chart.Builder chart = chartLoader.load(this.getClass().getResource("/charts/capillary-base-statefulset"));
        new Expectations(helmService) {
            {
                helmService.getChart("capillary-base-statefulset");
                result = chart;
            }
        };
        Application application = createApplication("helmint-test-statefulset-1", ApplicationFamily.CRM);
        String applicationName = application.getName();
        application.setPorts(Arrays.asList(new Port("http",80L,80L,Port.Protocol.HTTP)));
        application.setApplicationType(Application.ApplicationType.STATEFUL_SET);
        application.setPvcList(Arrays.asList(new PVC("data-pvc",PVC.AccessMode.ReadOnlyMany,100,"/usr/share/test","mnt")));
        application.setLoadBalancerType(LoadBalancerType.INTERNAL);
        application.setDnsType(Application.DnsType.PRIVATE);

        // update secrets and env
        secretService.initializeApplicationSecrets(
                Arrays.asList(new ApplicationSecretRequest(application.getApplicationFamily(),
                        application.getId(), "CREDENTIAL1", "")));
        secretService.updateApplicationSecrets(clusterName, application.getApplicationFamily(),
                application.getId(),
                Arrays.asList(
                        new ApplicationSecret(clusterName, application.getApplicationFamily(), application.getId(),
                                "CREDENTIAL1", "CREDENTIAL_VALUE1",
                                ApplicationSecret.SecretStatus.FULFILLED)));

        secretService.initializeApplicationSecrets(
                Arrays.asList(new ApplicationSecretRequest(application.getApplicationFamily(),
                        application.getId(), "CREDENTIALFILE1", "",
                        ApplicationSecretRequest.SecretType.FILE, "")));
        secretService.updateApplicationSecrets(clusterName, application.getApplicationFamily(),
                application.getId(),
                Arrays.asList(
                        new ApplicationSecret(clusterName, application.getApplicationFamily(), application.getId(),
                                "CREDENTIALFILE1", Base64.getEncoder().encodeToString("CREDENTIALFILE1".getBytes()),
                                ApplicationSecret.SecretStatus.FULFILLED)));

        Deployment deployment = createDeployment(application);
        deployment.setImage("hello-world:latest");
        deployment.setHorizontalPodAutoscaler(null);
        helmService.deploy(application, deployment);
        StatefulSet statefulSet = kubernetesClient.apps().statefulSets().inNamespace("default").withName(application.getName()).get();
        Service service = kubernetesClient.services().inNamespace("default").withName(application.getName() + "-test").get();

        final List<Pod> pods = kubernetesClient.pods().inNamespace("default").list().getItems().stream().filter(
                pod -> pod.getMetadata().getAnnotations() != null &&
                        deployment.getId().equalsIgnoreCase(pod.getMetadata().getAnnotations().get("deploymentId"))
        ).filter(pod -> pod.getMetadata().getAnnotations().get("buildId").equalsIgnoreCase(deployment.getBuildId()))
                .filter(pod -> pod.getMetadata().getLabels().get("app").equalsIgnoreCase(application.getName()))
                .collect(Collectors.toList());

        Assert.assertEquals("data-pvc-vol", statefulSet.getSpec().getVolumeClaimTemplates().get(0).getMetadata().getName());
        Assert.assertTrue(pods.get(0).getSpec().getContainers().get(0).getEnv().stream().anyMatch(x -> x.getName().equals("CREDENTIAL1")));
        Assert.assertTrue(kubernetesClient.secrets().inNamespace("default").withName(applicationName+ "-file-credentials").get().getData().containsKey("credentialfile1"));
        Assert.assertFalse(kubernetesClient.secrets().inNamespace("default").withName(applicationName+ "-credentials").get().getData().containsKey("CREDENTIALFILE1"));
        Assert.assertTrue(kubernetesClient.secrets().inNamespace("default").withName(applicationName+ "-credentials").get().getData().containsKey("CREDENTIAL1"));
        Assert.assertTrue(service.getMetadata().getAnnotations().containsKey("service.beta.kubernetes.io/aws-load-balancer-internal"));
        Assert.assertEquals("300",service.getMetadata().getAnnotations().get("service.beta.kubernetes.io/aws-load-balancer-connection-idle-timeout"));
        Assert.assertEquals("http",service.getMetadata().getAnnotations().get("service.beta.kubernetes.io/aws-load-balancer-backend-protocol"));
    }


    @Test
    public void testInstallNoLoadBalancer() throws Exception {
        URLChartLoader chartLoader = new URLChartLoader();
        ChartOuterClass.Chart.Builder chart = chartLoader.load(this.getClass().getResource("/charts/capillary-base"));
        new Expectations(helmService) {
            {
                helmService.getChart("capillary-base");
                result = chart;
            }
        };

        Application application = createApplication("helmint-test-no-elb-1", ApplicationFamily.CRM);
        application.setLoadBalancerType(LoadBalancerType.NONE);
        Deployment deployment = createDeployment(application);
        helmService.deploy(application, deployment);

        Service service = kubernetesClient.services().inNamespace("default").withName(application.getName()).get();
        Assert.assertEquals("ClusterIP",service.getSpec().getType());
    }
}

