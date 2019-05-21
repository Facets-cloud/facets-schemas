package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.service.interfaces.IHelmService;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import hapi.chart.ChartOuterClass.Chart;
import hapi.release.ReleaseOuterClass.Release;
import hapi.services.tiller.Tiller.*;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.microbean.helm.ReleaseManager;
import org.microbean.helm.Tiller;
import org.microbean.helm.chart.DirectoryChartLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@Profile("!dev")
public class HelmService implements IHelmService {
    @Autowired
    private SecretService secretService;

    private static final Logger logger = LoggerFactory.getLogger(HelmService.class);

    @Override
    public void deploy(Application application, Deployment deployment) {
        Environment environment = application.getApplicationFamily().getEnvironment(deployment.getEnvironment());
        String applicationName = application.getName();

        logger.info("deploying application: {}, for environment: {}", applicationName, environment.getName());
        ReleaseManager releaseManager = getReleaseManager(environment);
        logger.debug("fetching the helm release manager");
        String releaseName = getReleaseName(application, environment);
        logger.info("deploying with release name: {}", releaseName);
        Iterator<ListReleasesResponse> listReleasesResponseIterator =
                releaseManager.list(ListReleasesRequest.newBuilder().setFilter("^" + releaseName + "$").build());

        try {
            if (listReleasesResponseIterator.hasNext() && listReleasesResponseIterator.next().getReleasesCount() > 0) {
                logger.info("application {} already exists, upgrading", applicationName);
                releaseManager.close();
                upgrade(application, deployment);
            } else {
                logger.info("deploying application {} for the first time", applicationName);
                releaseManager.close();
                install(application, deployment);
            }
        } catch (Exception e) {
            logger.error("got error while deploying application: {}", applicationName, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getReleaseName(Application application, Environment environment) {
        return environment.getNodeGroup().isEmpty() ?
                application.getName() :
                environment.getNodeGroup() + "-" + application.getName();
    }

    private void install(Application application, Deployment deployment) throws Exception {
        DirectoryChartLoader chartLoader = new DirectoryChartLoader();
        Chart.Builder chart = chartLoader.load(Paths.get("/charts/capillary-base"));
        logger.debug("loaded the base chart");

        Environment environment = application.getApplicationFamily().getEnvironment(deployment.getEnvironment());
        ReleaseManager releaseManager = getReleaseManager(environment);
        String valuesYaml = getValuesYaml(environment, application, deployment);
        logger.info("fetched the values for helm chart");

        String releaseName = getReleaseName(application, environment);
        logger.info("deploying with release name: {}", releaseName);
        final InstallReleaseRequest.Builder requestBuilder = InstallReleaseRequest.newBuilder();
        requestBuilder.setTimeout(300L);
        requestBuilder.setName(releaseName); // Set the Helm release name
        requestBuilder.setWait(false); // Wait for Pods to be ready
        requestBuilder.getValuesBuilder().setRaw(valuesYaml);
        final Future<InstallReleaseResponse> releaseFuture = releaseManager.install(requestBuilder, chart);
        final Release release = releaseFuture.get().getRelease();
        releaseManager.close();
    }

    private void upgrade(Application application, Deployment deployment) throws Exception {
        DirectoryChartLoader chartLoader = new DirectoryChartLoader();
        Chart.Builder chart = chartLoader.load(Paths.get("/charts/capillary-base"));
        logger.debug("loaded the base chart for upgrade step");

        Environment environment = application.getApplicationFamily().getEnvironment(deployment.getEnvironment());
        ReleaseManager releaseManager = getReleaseManager(environment);
        String valuesYaml = getValuesYaml(environment, application, deployment);
        logger.info("fetched the values for helm chart for upgrade step");

        String releaseName = getReleaseName(application, environment);
        logger.info("deploying the upgrade with release name: {}", releaseName);

        final UpdateReleaseRequest.Builder requestBuilder = UpdateReleaseRequest.newBuilder();
        requestBuilder.setTimeout(300L);
        requestBuilder.setName(releaseName); // Set the Helm release name
        requestBuilder.setWait(false); // Wait for Pods to be ready
        requestBuilder.getValuesBuilder().setRaw(valuesYaml);
        final Future<UpdateReleaseResponse> releaseFuture = releaseManager.update(requestBuilder, chart);
        final Release release = releaseFuture.get().getRelease();
        releaseManager.close();
    }

    private String getValuesYaml(Environment environment, Application application, Deployment deployment) {
        final Map<String, Object> yaml = new LinkedHashMap<>();
        yaml.put("image", deployment.getImage());
        yaml.put("podCPULimit",deployment.getPodSize().getCpu());
        yaml.put("podMemoryLimit",deployment.getPodSize().getMemory());
        String nodeGroup = application.getApplicationFamily().getEnvironment(deployment.getEnvironment()).getNodeGroup();
        if(!nodeGroup.isEmpty()) {
            yaml.put("nodeSelector", nodeGroup);
        }
        yaml.put("lbType", application.getLoadBalancerType().name().toLowerCase());
        List<Map<String, Object>> ports = application.getPorts().stream().map(this::getPortMap).collect(Collectors.toList());
        yaml.put("ports", ports);
        logger.info("loaded values for release: {}", yaml);

        yaml.put("configurations", deployment.getConfigurations());
        yaml.put("credentials", getCredentialsMap(environment, application));
        yaml.entrySet().addAll(getFamilySpecificAttributes(application, deployment).entrySet());
        final String yamlString = new Yaml().dump(yaml);
        return yamlString;
    }

    private Map<String, String> getCredentialsMap(Environment environment, Application application) {
        List<ApplicationSecret> savedSecrets = secretService.getApplicationSecrets(
                environment.getName(),
                application.getApplicationFamily(),
                application.getId());

        Map<String, String> secretMap = Maps.newHashMapWithExpectedSize(savedSecrets.size());
        savedSecrets.forEach(x -> secretMap.put(x.getSecretName(), ""));

        return secretMap;
    }

    private Map<String, Object> getFamilySpecificAttributes(Application application, Deployment deployment) {
        Map<String, Object> valueFields = new HashMap<>();
        switch (application.getApplicationFamily()) {
            case CRM:
                logger.info("loading CRM specific attributes");
                if(deployment.getConfigurations().containsKey("crmModuleName")) {
                    String crmModuleName = deployment.getConfigurations().get("crmModuleName");
                    valueFields.put("crmConfigurations", getCRMConfigsFromModule(crmModuleName));
                }
        }
        return valueFields;
    }

    private Map<String, Object> getPortMap(Port port) {
        final Map<String, Object> out = new LinkedHashMap<>();
        out.put("name", port.getName());
        out.put("containerPort", port.getContainerPort());
        out.put("lbPort", port.getLbPort());
        return out;
    }

    private ReleaseManager getReleaseManager(Environment environment) {
        DefaultKubernetesClient kubernetesClient = new DefaultKubernetesClient(
                new ConfigBuilder()
                        .withMasterUrl(environment.getKubernetesApiEndpoint())
                        .withOauthToken(environment.getKubernetesToken())
                        .withTrustCerts(true)
                        .withWebsocketTimeout(60*1000)
                        .withConnectionTimeout(30*1000)
                        .withRequestTimeout(30*1000)
                        .build());
        try {
            ReleaseManager releaseManager = new ReleaseManager(new Tiller(kubernetesClient));
            return releaseManager;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getCRMConfigsFromModule(String crmModuleName) {
        HashMap<String, List<String>> moduleDependencyList = new HashMap<>();
        Gson gson = new Gson();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("/etc/deployer/conf/sd-auth-conf.json"));
        } catch (Exception ex) {
            return Collections.emptyList();
        }
        Map<String, Object> jsonObject = (Map) gson.fromJson(br, Object.class);
        for (Object service : jsonObject.keySet()) {
            for (Object module : ((Map) jsonObject.get(service)).keySet()) {
                List<String> serviceList = new ArrayList<>();
                if (moduleDependencyList.get(module.toString()) != null) {
                    serviceList = moduleDependencyList.get(module.toString());
                }
                serviceList.add(service.toString());
                moduleDependencyList.put(module.toString(), serviceList);
            }
        }
        return moduleDependencyList.get(crmModuleName);
    }

    @Override
    public void rollback(Application application, Environment environment) {
        ReleaseManager releaseManager = getReleaseManager(environment);
        String releaseName = getReleaseName(application,environment);
        Iterator<ListReleasesResponse> listReleasesResponseIterator =
                releaseManager.list(ListReleasesRequest.newBuilder().setFilter("^" + releaseName + "$").build());
        int releaseStatus = listReleasesResponseIterator.next().getReleasesCount();

        try {
            if (releaseStatus > 0) {
                logger.info("rollback {} to previous release",releaseName);
                final RollbackReleaseRequest.Builder rollbackRequest = RollbackReleaseRequest.newBuilder();
                rollbackRequest.setName(releaseName);
                rollbackRequest.setTimeout(300L);
                rollbackRequest.setWait(false);
                rollbackRequest.setRecreate(true);
                rollbackRequest.setVersion(0); // 0 -> Rollback to previous revision
                final Future<RollbackReleaseResponse> rollbackFuture = releaseManager.rollback(rollbackRequest.build());
                final Release release = rollbackFuture.get().getRelease();
                releaseManager.close();
            } else {
                releaseManager.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}