package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.repository.EnvironmentRepository;
import com.capillary.ops.deployer.service.interfaces.IHelmService;
import com.google.common.collect.Lists;
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
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    @Autowired
    private EnvironmentRepository environmentRepository;

    private static final Logger logger = LoggerFactory.getLogger(HelmService.class);

    @Override
    public void deploy(Environment environment, String releaseName, String chartName, Map<String, Object> valueMap) {
        logger.info("Installing " + chartName + " with values " + valueMap.toString());
        ReleaseManager releaseManager = getReleaseManager(environment);
        Iterator<ListReleasesResponse> listReleasesResponseIterator =
                releaseManager.list(ListReleasesRequest.newBuilder().setFilter("^" + releaseName + "$").build());

        try {
            if (listReleasesResponseIterator.hasNext() && listReleasesResponseIterator.next().getReleasesCount() > 0) {
                releaseManager.close();
                upgrade(environment, releaseName, chartName, valueMap);
            } else {
                releaseManager.close();
                install(environment, releaseName, chartName, valueMap);
            }
        } catch (Exception e) {
            logger.error("error happened while trying to install or upgrade application", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deploy(Application application, Deployment deployment) {
        ApplicationFamily applicationFamily = application.getApplicationFamily();
        String environmentName = deployment.getEnvironment();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        String applicationName = application.getName();

        logger.info("deploying application: {}, for environment: {}", applicationName, environmentName);
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

    public boolean doesReleaseExist(ApplicationFamily applicationFamily, Environment environment, String releaseName) {
        ReleaseManager releaseManager = getReleaseManager(environment);
        Iterator<ListReleasesResponse> responseIterator = releaseManager.list(ListReleasesRequest.newBuilder()
                .setFilter("^" + releaseName + "$")
                .setNamespace("default")
                .build());
        boolean ret = responseIterator.hasNext();
        try {
            releaseManager.close();
        } catch (IOException e) {
            logger.error("error closing releasemanager", e);
        }
        return ret;
    }

    @Override
    public String getReleaseName(Application application, Environment environment) {
        return environment.getEnvironmentConfiguration().getNodeGroup().isEmpty() ?
                application.getName() :
                environment.getEnvironmentConfiguration().getNodeGroup() + "-" + application.getName();
    }

    private void install(Environment environment, String releaseName, String chartName, Map<String, Object> valueMap) throws Exception {
        DirectoryChartLoader chartLoader = new DirectoryChartLoader();
        Chart.Builder chart = chartLoader.load(Paths.get("/charts/" + chartName));
        ReleaseManager releaseManager = getReleaseManager(environment);
        String valuesYaml = new Yaml().dump(valueMap);

        final InstallReleaseRequest.Builder requestBuilder = InstallReleaseRequest.newBuilder();
        requestBuilder.setTimeout(300L);
        requestBuilder.setName(releaseName); // Set the Helm release name
        requestBuilder.setWait(false); // Wait for Pods to be ready
        requestBuilder.getValuesBuilder().setRaw(valuesYaml);

        final Future<InstallReleaseResponse> releaseFuture = releaseManager.install(requestBuilder, chart);
        final Release release = releaseFuture.get().getRelease();

        releaseManager.close();
    }

    private void install(Application application, Deployment deployment) throws Exception {
        ApplicationFamily applicationFamily = application.getApplicationFamily();
        String environmentName = deployment.getEnvironment();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        Map<String, Object> valueMap = getValuesYaml(environment, application, deployment);
        String releaseName = getReleaseName(application, environment);
        install(environment, releaseName, "capillary-base", valueMap);
    }

    private void upgrade(Environment environment, String releaseName, String chartName, Map<String, Object> valueMap) throws Exception {
        DirectoryChartLoader chartLoader = new DirectoryChartLoader();
        Chart.Builder chart = chartLoader.load(Paths.get("/charts/capillary-base"));
        ReleaseManager releaseManager = getReleaseManager(environment);
        String valuesYaml = new Yaml().dump(valueMap);

        final UpdateReleaseRequest.Builder requestBuilder = UpdateReleaseRequest.newBuilder();
        requestBuilder.setTimeout(300L);
        requestBuilder.setName(releaseName); // Set the Helm release name
        requestBuilder.setWait(false); // Wait for Pods to be ready
        requestBuilder.getValuesBuilder().setRaw(valuesYaml);
        requestBuilder.setForce(true);

        final Future<UpdateReleaseResponse> releaseFuture = releaseManager.update(requestBuilder, chart);
        final Release release = releaseFuture.get().getRelease();

        releaseManager.close();
    }

    private void upgrade(Application application, Deployment deployment) throws Exception {
        ApplicationFamily applicationFamily = application.getApplicationFamily();
        String environmentName = deployment.getEnvironment();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        Map<String, Object> valueMap = getValuesYaml(environment, application, deployment);
        String releaseName = getReleaseName(application, environment);
        upgrade(environment, releaseName, "capillary-base", valueMap);
    }

    private Map<String, Object> getValuesYaml(Environment environment, Application application, Deployment deployment) {
        final Map<String, Object> yaml = new LinkedHashMap<>();
        yaml.put("deploymentId", deployment.getId());
        yaml.put("buildId", deployment.getBuildId());
        yaml.put("image", deployment.getImage());
        yaml.put("podCPULimit",deployment.getPodSize().getCpu());
        yaml.put("podMemoryLimit",deployment.getPodSize().getMemory());
        String nodeGroup = environment.getEnvironmentConfiguration().getNodeGroup();
        if(!nodeGroup.isEmpty()) {
            yaml.put("nodeSelector", nodeGroup);
        }
        yaml.put("lbType", application.getLoadBalancerType().name().toLowerCase());
        List<Map<String, Object>> ports = application.getPorts().stream().map(this::getPortMap).collect(Collectors.toList());
        yaml.put("ports", ports);

        yaml.put("configurations", getConfigMap(environment, application, deployment));
        yaml.put("credentials", getCredentialsMap(environment, application));

        String dnsPrefix = application.getDnsPrefix();
        addZoneDns(application, yaml, Application.DnsType.PRIVATE, getPrivateZoneDns(environment, dnsPrefix));
        addZoneDns(application, yaml, Application.DnsType.PUBLIC, getPublicZoneDns(environment, dnsPrefix));

        yaml.putAll(getFamilySpecificAttributes(application, deployment));
        yaml.putAll(getHPAConfigs(deployment));
        if(application.getHealthCheck() != null) {
            yaml.putAll(getHealthCheckConfigs(application));
        }

        logger.info("loaded values for release: {}", yaml);
        return yaml;
    }

    private void addZoneDns(Application application, Map<String, Object> yaml, Application.DnsType dnsType, String zoneDns) {
        if (dnsType.equals(application.getDnsType()) && zoneDns != null) {
            Map<String, Object> aws = new HashMap<>();
            aws.put("domainName", zoneDns);
            yaml.put("aws", aws);
        }
    }

    private Map<String, String> getConfigMap(Environment environment, Application application, Deployment deployment) {
        Map<String, String> configMap = new HashMap<>();
        configMap.putAll(deployment.getConfigurationsMap());
        configMap.putAll(application.getCommonConfigs());
        configMap.putAll(environment.getEnvironmentConfiguration().getCommonConfigs());
        return configMap;
    }

    private Map<String, String> getCredentialsMap(Environment environment, Application application) {
        List<ApplicationSecret> savedSecrets = secretService.getApplicationSecrets(
                environment.getEnvironmentMetaData().getName(),
                application.getApplicationFamily(),
                application.getId());

        Map<String, String> secretMap = Maps.newHashMapWithExpectedSize(savedSecrets.size());
        if(savedSecrets != null) {
            savedSecrets.stream().filter(x -> x.getSecretStatus()
                    .equals(ApplicationSecret.SecretStatus.FULFILLED))
                    .forEach(x -> secretMap.put(x.getSecretName(), x.getSecretValue()));
        }
        secretMap.putAll(environment.getEnvironmentConfiguration().getCommonCredentials());
        return secretMap;
    }

    private Map<String, Object> getFamilySpecificAttributes(Application application, Deployment deployment) {
        Map<String, Object> valueFields = new HashMap<>();
        switch (application.getApplicationFamily()) {
            case CRM:
                logger.info("loading CRM specific attributes");
                if(deployment.getConfigurationsMap().containsKey("crmModuleName")) {
                    String crmModuleName = deployment.getConfigurationsMap().get("crmModuleName");
                    valueFields.put("crmConfigurations", getCRMConfigsFromModule(crmModuleName));
                }
                break;
            case ECOMMERCE:
                Map<String, Object> capabilities = new HashMap<>();
                if (shouldMountCifs(application)) {
                    capabilities.put("add", Lists.newArrayList("SYS_ADMIN", "DAC_READ_SEARCH"));
                    capabilities.put("drop", Lists.newArrayList());
                } else {
                    capabilities.put("add", Lists.newArrayList());
                    capabilities.put("drop", Lists.newArrayList());
                }
                valueFields.put("capabilities", capabilities);
                break;
        }
        return valueFields;
    }

    private Map<String, Object> getHPAConfigs(Deployment deployment){
        Map<String, Object> valueFields = new HashMap<>();
        if(deployment.getHorizontalPodAutoscaler() != null){
            valueFields.put("hpaEnabled","true");
            valueFields.put("hpaMinReplicas",deployment.getHorizontalPodAutoscaler().getMinReplicas());
            valueFields.put("hpaMaxReplicas",deployment.getHorizontalPodAutoscaler().getMaxReplicas());
            valueFields.put("hpaMetricThreshold",deployment.getHorizontalPodAutoscaler().getThreshold());
        }
        return valueFields;
    }

    private Map<String, Object> getHealthCheckConfigs(Application application){
        Map<String, Object> valueFields = new HashMap<>();
        HealthCheck healthCheck = application.getHealthCheck();
        if(healthCheck.getLivenessProbe() != null){
            if(!StringUtils.isEmpty(healthCheck.getLivenessProbe().getHttpCheckEndpoint())){
                valueFields.put("enableLivenessHTTP","true");
                valueFields.put("livenessPort",healthCheck.getLivenessProbe().getPort());
                valueFields.put("livenessHTTPEndpoint",healthCheck.getLivenessProbe().getHttpCheckEndpoint());
                valueFields.put("livenessInitialDelay",healthCheck.getLivenessProbe().getInitialDelaySeconds());
                valueFields.put("livenessPeriod",healthCheck.getLivenessProbe().getPeriodSeconds());
            }else {
                valueFields.put("enableLivenessTCP","true");
                valueFields.put("livenessPort",healthCheck.getLivenessProbe().getPort());
                valueFields.put("livenessInitialDelay",healthCheck.getLivenessProbe().getInitialDelaySeconds());
                valueFields.put("livenessPeriod",healthCheck.getLivenessProbe().getPeriodSeconds());
            }
        }

        if(healthCheck.getReadinessProbe() != null){
            if(!StringUtils.isEmpty(healthCheck.getReadinessProbe().getHttpCheckEndpoint())){
                valueFields.put("enableReadinessHTTP","true");
                valueFields.put("readinessPort",healthCheck.getReadinessProbe().getPort());
                valueFields.put("readinessHTTPEndpoint",healthCheck.getReadinessProbe().getHttpCheckEndpoint());
                valueFields.put("readinessInitialDelay",healthCheck.getReadinessProbe().getInitialDelaySeconds());
                valueFields.put("readinessPeriod",healthCheck.getReadinessProbe().getPeriodSeconds());
            }else {
                valueFields.put("enableReadinessTCP","true");
                valueFields.put("readinessPort",healthCheck.getReadinessProbe().getPort());
                valueFields.put("readinessInitialDelay",healthCheck.getReadinessProbe().getInitialDelaySeconds());
                valueFields.put("readinessPeriod",healthCheck.getReadinessProbe().getPeriodSeconds());
            }
        }
        return valueFields;
    }

    private boolean shouldMountCifs(Application application) {
        String mountCifs = application.getCommonConfigs().get("MOUNT_CIFS");
        return mountCifs != null && Boolean.valueOf(mountCifs);
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
                        .withMasterUrl(environment.getEnvironmentConfiguration().getKubernetesApiEndpoint())
                        .withOauthToken(environment.getEnvironmentConfiguration().getKubernetesToken())
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
    public String getPublicZoneDns(Environment environment, String applicationPrefix) {
        ExternalDnsConfiguration publicDnsConfiguration = environment.getEnvironmentConfiguration().getPublicDnsConfiguration();
        if(publicDnsConfiguration == null) {
            return null;
        }

        return environment.getEnvironmentMetaData().getName() + "-" +
                new StringJoiner(".")
                .add(applicationPrefix)
                .add(publicDnsConfiguration.getZoneDns())
                .toString();
    }

    public String getPrivateZoneDns(Environment environment, String applicationPrefix) {
        ExternalDnsConfiguration privateDnsConfiguration =
                environment.getEnvironmentConfiguration().getPrivateDnsConfiguration();
        if(privateDnsConfiguration == null) {
            return null;
        }

        return applicationPrefix + "." + privateDnsConfiguration.getZoneDns();
    }

    private boolean anyItemNullOrEmpty(List<String> items) {
        if (items.contains(null) || items.contains("")) {
            logger.info("found one of the fields null or empty");
            return true;
        }
        return false;
    }

}