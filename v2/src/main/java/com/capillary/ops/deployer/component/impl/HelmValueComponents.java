package com.capillary.ops.deployer.component.impl;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.component.IHelmValueComponents;
import com.capillary.ops.deployer.service.SecretService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.capillary.ops.deployer.bo.Deployment.*;

@Component
public class HelmValueComponents implements IHelmValueComponents {

    @Autowired
    private SecretService secretService;

    @Override
    public String getDeploymentId(Deployment deployment) {
        return deployment.getId();
    }

    @Override
    public String getBuildId(Deployment deployment) {
        return deployment.getBuildId();
    }

    @Override
    public String getImage(Deployment deployment) {
        return deployment.getImage();
    }

    @Override
    public Integer getPodCPULimit(Deployment deployment) {
        return deployment.getPodSize().getCpu();
    }

    @Override
    public Integer getPodMemoryLimit(Deployment deployment) {
        return deployment.getPodSize().getMemory();
    }

    @Override
    public String getNodeGroup(Environment environment) {
        String nodeGroup = environment.getEnvironmentConfiguration().getNodeGroup();
        return StringUtils.isEmpty(nodeGroup) ? null : nodeGroup;
    }

    @Override
    public String getLbType(Application application) {
        return application.getLoadBalancerType().name().toLowerCase();
    }

    @Override
    public List<Map<String, Object>> getPorts(Application application) {
        return application.getPorts().stream().map(this::getPortMap).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> getConfigMap(Environment environment, Application application, Deployment deployment) {
        Map<String, String> configMap = new HashMap<>();
        configMap.putAll(deployment.getConfigurationsMap());
        configMap.putAll(application.getCommonConfigs());
        configMap.putAll(environment.getEnvironmentConfiguration().getCommonConfigs());
        return configMap;
    }

    @Override
    public Map<String, String> getCredentialsMap(Environment environment, Application application) {
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

    @Override
    public String getSSLCertificateName(Application application, Environment environment) {
        List<Map<String, Object>> ports = this.getPorts(application);
        if (ports.stream().anyMatch(p -> p.get("name").equals("https"))
                && environment.getEnvironmentConfiguration().getSslConfigs() !=null
                && application.getLoadBalancerType().name().toLowerCase().equals("external")) {
            return environment.getEnvironmentConfiguration().getSslConfigs().getSSLCertName();
        }

        return null;
    }

    @Override
    public String getPrivateZoneDns(Application application, Environment environment) {
        String dnsPrefix = application.getDnsPrefix();
        ExternalDnsConfiguration privateDnsConfiguration =
                environment.getEnvironmentConfiguration().getPrivateDnsConfiguration();
        if(privateDnsConfiguration != null && Application.DnsType.PRIVATE.equals(application.getDnsType())) {
            return dnsPrefix + "." + privateDnsConfiguration.getZoneDns();
        }

        return null;
    }

    @Override
    public String getPublicZoneDns(Application application, Environment environment) {
        String dnsPrefix = application.getDnsPrefix();
        ExternalDnsConfiguration publicDnsConfiguration =
                environment.getEnvironmentConfiguration().getPublicDnsConfiguration();
        if(publicDnsConfiguration != null && Application.DnsType.PUBLIC.equals(application.getDnsType())) {
            return environment.getEnvironmentMetaData().getName() + "-" +
                    new StringJoiner(".")
                            .add(dnsPrefix)
                            .add(publicDnsConfiguration.getZoneDns())
                            .toString();
        }

        return null;
    }

    @Override
    public Map<String, Object> getFamilySpecificAttributes(Application application, Deployment deployment) {
        Map<String, Object> valueFields = new HashMap<>();
        switch (application.getApplicationFamily()) {
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

    @Override
    public Map<String, Object> getHPAConfigs(Deployment deployment) {
        Map<String, Object> valueFields = new HashMap<>();
        if(deployment.getHorizontalPodAutoscaler() != null){
            valueFields.put("hpaEnabled","true");
            valueFields.put("hpaMinReplicas",deployment.getHorizontalPodAutoscaler().getMinReplicas());
            valueFields.put("hpaMaxReplicas",deployment.getHorizontalPodAutoscaler().getMaxReplicas());
            valueFields.put("hpaMetricThreshold",deployment.getHorizontalPodAutoscaler().getThreshold());
        }
        return valueFields;
    }

    @Override
    public Map<String, Object> getHealthCheckConfigs(Application application) {
        if (application.getHealthCheck() == null) {
            return null;
        }

        Map<String, Object> probeFields = new HashMap<>();
        HealthCheck healthCheck = application.getHealthCheck();
        if(healthCheck.getLivenessProbe() != null){
            if(!StringUtils.isEmpty(healthCheck.getLivenessProbe().getHttpCheckEndpoint())){
                probeFields.put("enableLivenessHTTP","true");
                probeFields.put("livenessPort",healthCheck.getLivenessProbe().getPort());
                probeFields.put("livenessHTTPEndpoint",healthCheck.getLivenessProbe().getHttpCheckEndpoint());
                probeFields.put("livenessInitialDelay",healthCheck.getLivenessProbe().getInitialDelaySeconds());
                probeFields.put("livenessPeriod",healthCheck.getLivenessProbe().getPeriodSeconds());
                probeFields.put("livenessFailureThreshold",healthCheck.getLivenessProbe().getFailureThreshold());
                probeFields.put("livenessSuccessThreshold",healthCheck.getLivenessProbe().getSuccessThreshold());
                probeFields.put("livenessTimeout",healthCheck.getLivenessProbe().getTimeout());
            }else {
                probeFields.put("enableLivenessTCP","true");
                probeFields.put("livenessPort",healthCheck.getLivenessProbe().getPort());
                probeFields.put("livenessInitialDelay",healthCheck.getLivenessProbe().getInitialDelaySeconds());
                probeFields.put("livenessPeriod",healthCheck.getLivenessProbe().getPeriodSeconds());
                probeFields.put("livenessFailureThreshold",healthCheck.getLivenessProbe().getFailureThreshold());
                probeFields.put("livenessSuccessThreshold",healthCheck.getLivenessProbe().getSuccessThreshold());
                probeFields.put("livenessTimeout",healthCheck.getLivenessProbe().getTimeout());
            }
        }

        if(healthCheck.getReadinessProbe() != null){
            if(!StringUtils.isEmpty(healthCheck.getReadinessProbe().getHttpCheckEndpoint())){
                probeFields.put("enableReadinessHTTP","true");
                probeFields.put("readinessPort",healthCheck.getReadinessProbe().getPort());
                probeFields.put("readinessHTTPEndpoint",healthCheck.getReadinessProbe().getHttpCheckEndpoint());
                probeFields.put("readinessInitialDelay",healthCheck.getReadinessProbe().getInitialDelaySeconds());
                probeFields.put("readinessPeriod",healthCheck.getReadinessProbe().getPeriodSeconds());
                probeFields.put("readinessFailureThreshold",healthCheck.getLivenessProbe().getFailureThreshold());
                probeFields.put("readinessSuccessThreshold",healthCheck.getLivenessProbe().getSuccessThreshold());
                probeFields.put("readinessTimeout",healthCheck.getLivenessProbe().getTimeout());
            }else {
                probeFields.put("enableReadinessTCP","true");
                probeFields.put("readinessPort",healthCheck.getReadinessProbe().getPort());
                probeFields.put("readinessInitialDelay",healthCheck.getReadinessProbe().getInitialDelaySeconds());
                probeFields.put("readinessPeriod",healthCheck.getReadinessProbe().getPeriodSeconds());
                probeFields.put("readinessFailureThreshold",healthCheck.getLivenessProbe().getFailureThreshold());
                probeFields.put("readinessSuccessThreshold",healthCheck.getLivenessProbe().getSuccessThreshold());
                probeFields.put("readinessTimeout",healthCheck.getLivenessProbe().getTimeout());
            }
        }
        return probeFields;
    }

    @Override
    public List<Map<String, Object>> getPVCList(Application application) {
        if (application.getPvcList() == null || application.getPvcList().isEmpty()) {
            return null;
        }

        return application.getPvcList()
                .parallelStream()
                .map(this::getPvcValues)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getSecretFileMounts(Application application, Environment environment, Deployment deployment) {
        if (!secretVolumesExist(deployment)) {
            return null;
        }

        List<Map<String, Object>> secretFileMountsList = new ArrayList<>();
        List<ApplicationSecret> applicationSecrets = secretService.getApplicationSecrets(
                environment.getEnvironmentMetaData().getName(), application.getApplicationFamily(), application.getId());
        Map<String, ApplicationSecret> fileSecrets = applicationSecrets.parallelStream()
                .filter(x -> x.getSecretType() != null && x.getSecretType().equals(ApplicationSecret.SecretType.FILE))
                .collect(Collectors.toMap(ApplicationSecret::getSecretName, Function.identity()));

        List<SecretFileMount> secretFileMounts = deployment.getSecretFileMounts();
        secretFileMounts.parallelStream().forEach(x -> {
            if (fileSecrets.containsKey(x.getSecretRef())) {
                Map<String, Object> secretFileMountYaml = new HashMap<>();
                secretFileMountYaml.put("name", x.getSecretRef());
                secretFileMountYaml.put("mountPath", x.getMountPath());
                secretFileMountYaml.put("value", fileSecrets.get(x.getSecretRef()).getSecretValue());
                secretFileMountsList.add(secretFileMountYaml);
            }
        });

        return secretFileMountsList;
    }

    @Override
    public String getSchedule(Deployment deployment) {
        return deployment.getSchedule();
    }

    @Override
    public String getConcurrencyPolicy(Deployment deployment) {
        ConcurrencyPolicy concurrencyPolicy = deployment.getConcurrencyPolicy();
        return concurrencyPolicy == null ? null : concurrencyPolicy.name();
    }

    private boolean secretVolumesExist(Deployment deployment) {
        List<SecretFileMount> secretFileMounts = deployment.getSecretFileMounts();
        return secretFileMounts != null && secretFileMounts.size() > 0;
    }

    private Map<String, Object> getPvcValues(PVC pvc) {
        Map<String, Object> pvcYaml = new HashMap<>();
        pvcYaml.put("name", pvc.getName());
        pvcYaml.put("accessMode", pvc.getAccessMode());
        pvcYaml.put("storageSize", pvc.getStorageSize());
        pvcYaml.put("volumeDirectory", pvc.getVolumeDirectory());
        pvcYaml.put("mountPath", pvc.getMountPath());

        return pvcYaml;
    }

    private boolean shouldMountCifs(Application application) {
        String mountCifs = application.getCommonConfigs().get("MOUNT_CIFS");
        return mountCifs != null && Boolean.valueOf(mountCifs);
    }

    private Map<String, Object> getPortMap(Port port) {
        final Map<String, Object> portMap = new LinkedHashMap<>();
        portMap.put("name", port.getName());
        portMap.put("containerPort", port.getContainerPort());
        portMap.put("lbPort", port.getLbPort());
        return portMap;
    }
}
