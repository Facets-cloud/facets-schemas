package com.capillary.ops.deployer.service.helm;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.service.IAMService;
import com.capillary.ops.deployer.service.SecretService;
import com.capillary.ops.deployer.service.interfaces.IIAMService;
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
public abstract class AbstractValueProvider {

    @Autowired
    private SecretService secretService;

    @Autowired
    private IIAMService iamService;

    public String getImage(Deployment deployment) {
        return deployment.getImage();
    }

    public Double getPodCPULimit(Deployment deployment) {
        return deployment.getPodSize().getCpu();
    }

    public Double getPodMemoryLimit(Deployment deployment) {
        return deployment.getPodSize().getMemory();
    }
    
    public String getNodeGroup(Environment environment) {
        String nodeGroup = environment.getEnvironmentConfiguration().getNodeGroup();
        return StringUtils.isEmpty(nodeGroup) ? null : nodeGroup;
    }

    public String getLbType(Application application) {
        return application.getLoadBalancerType().name().toLowerCase();
    }
    
    public List<Map<String, Object>> getPorts(Application application) {
        return application.getPorts().stream().map(this::getPortMap).collect(Collectors.toList());
    }
    
    public Map<String, String> getConfigMap(Environment environment, Application application, Deployment deployment) {
        Map<String, String> configMap = new HashMap<>();
        configMap.putAll(deployment.getConfigurationsMap());
        configMap.putAll(application.getCommonConfigs());
        if(environment.getEnvironmentConfiguration().getCommonConfigs() != null) {
            configMap.putAll(environment.getEnvironmentConfiguration().getCommonConfigs());
        }
        return configMap;
    }

    public Map<String, Object> getCredentialsMap(Environment environment, Application application) {
        Map<String, Object> credentialsList = new HashMap<>();
        List<ApplicationSecret> applicationSecrets = secretService.getApplicationSecrets(
                environment.getEnvironmentMetaData().getName(), application.getApplicationFamily(), application.getId());
        Map<String, ApplicationSecretRequest> requests = secretService.getApplicationSecretRequests(application.getApplicationFamily(), application.getId())
                .stream().collect(Collectors.toMap(ApplicationSecretRequest::getSecretName, Function.identity()));
        Map<String, ApplicationSecret> envSecrets = applicationSecrets.stream()
                .filter(x -> requests.get(x.getSecretName()).getSecretType() != null && requests.get(x.getSecretName()).getSecretType().equals(ApplicationSecretRequest.SecretType.ENVIRONMENT))
                .collect(Collectors.toMap(ApplicationSecret::getSecretName, Function.identity()));

        envSecrets.values().stream().forEach(x -> {
            if (envSecrets.containsKey(x.getSecretName())) {
                credentialsList.put(x.getSecretName().toLowerCase(),envSecrets.get(x.getSecretName()).getSecretValue());
            }
        });
        return credentialsList;
    }

    public String getSSLCertificateName(Application application, Environment environment) {
        List<Map<String, Object>> ports = this.getPorts(application);
        if (ports.stream().anyMatch(p -> p.get("name").equals("https"))
                && environment.getEnvironmentConfiguration().getSslConfigs() !=null
                && application.getLoadBalancerType().name().toLowerCase().equals("external")) {
            return environment.getEnvironmentConfiguration().getSslConfigs().getSSLCertName();
        }

        return null;
    }

    public String getPrivateZoneDns(Application application, Environment environment) {
        String dnsPrefix = application.getDnsPrefix();
        ExternalDnsConfiguration privateDnsConfiguration =
                environment.getEnvironmentConfiguration().getPrivateDnsConfiguration();
        if(privateDnsConfiguration != null && Application.DnsType.PRIVATE.equals(application.getDnsType())) {
            return dnsPrefix + "." + privateDnsConfiguration.getZoneDns();
        }

        return null;
    }

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
    
    public Map<String, Object> getFamilySpecificAttributes(Application application, Deployment deployment) {
        Map<String, Object> valueFields = new HashMap<>();
        switch (application.getApplicationFamily()) {
            case CRM:
                if(deployment.getConfigurationsMap().containsKey("zkPublish") && deployment.getConfigurationsMap().containsKey("zkName")) {
                    valueFields.put("zkPublish", deployment.getConfigurationsMap().get("zkPublish"));
                    valueFields.put("zkName", deployment.getConfigurationsMap().get("zkName"));
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
    
    public Map<String, Object> getHealthCheckConfigs(Application application) {
        if (application.getHealthCheck() == null) {
            return null;
        }

        Map<String, Object> probeFields = new HashMap<>();
        HealthCheck healthCheck = application.getHealthCheck();
        if(healthCheck.getLivenessProbe() != null &&
                healthCheck.getLivenessProbe().getPort() != 0){
            if(!StringUtils.isEmpty(healthCheck.getLivenessProbe().getHttpCheckEndpoint())){
                probeFields.put("enableLivenessHTTP","true");
                probeFields.put("livenessHTTPEndpoint",healthCheck.getLivenessProbe().getHttpCheckEndpoint());
            }else {
                probeFields.put("enableLivenessTCP","true");
            }
            probeFields.put("livenessPort",healthCheck.getLivenessProbe().getPort());
            probeFields.put("livenessInitialDelay",healthCheck.getLivenessProbe().getInitialDelaySeconds());
            probeFields.put("livenessPeriod",healthCheck.getLivenessProbe().getPeriodSeconds());
            probeFields.put("livenessFailureThreshold",healthCheck.getLivenessProbe().getFailureThreshold());
            probeFields.put("livenessSuccessThreshold", 1);
            probeFields.put("livenessTimeout",healthCheck.getLivenessProbe().getTimeout());
        }

        if(healthCheck.getReadinessProbe() != null && healthCheck.getReadinessProbe().getPort() != 0){
            if(!StringUtils.isEmpty(healthCheck.getReadinessProbe().getHttpCheckEndpoint())){
                probeFields.put("enableReadinessHTTP","true");
                probeFields.put("readinessHTTPEndpoint",healthCheck.getReadinessProbe().getHttpCheckEndpoint());
            }else {
                probeFields.put("enableReadinessTCP","true");
            }
            probeFields.put("readinessPort",healthCheck.getReadinessProbe().getPort());
            probeFields.put("readinessInitialDelay",healthCheck.getReadinessProbe().getInitialDelaySeconds());
            probeFields.put("readinessPeriod",healthCheck.getReadinessProbe().getPeriodSeconds());
            probeFields.put("readinessFailureThreshold",healthCheck.getLivenessProbe().getFailureThreshold());
            probeFields.put("readinessSuccessThreshold", 1);
            probeFields.put("readinessTimeout",healthCheck.getLivenessProbe().getTimeout());
        }
        return probeFields;
    }

    
    public List<Map<String, Object>> getPVCList(Application application) {
        if (application.getPvcList() == null || application.getPvcList().isEmpty()) {
            return null;
        }

        return application.getPvcList()
                .parallelStream()
                .map(this::getPvcValues)
                .collect(Collectors.toList());
    }

    
    public List<Map<String, Object>> getSecretFileMounts(Application application, Environment environment, Deployment deployment) {
        List<Map<String, Object>> secretFileMountsList = new ArrayList<>();
        List<ApplicationSecret> applicationSecrets = secretService.getApplicationSecrets(
                environment.getEnvironmentMetaData().getName(), application.getApplicationFamily(), application.getId());
        Map<String, ApplicationSecretRequest> requests = secretService.getApplicationSecretRequests(application.getApplicationFamily(), application.getId())
                .stream().collect(Collectors.toMap(ApplicationSecretRequest::getSecretName, Function.identity()));
        Map<String, ApplicationSecret> fileSecrets = applicationSecrets.stream()
                .filter(x -> requests.get(x.getSecretName()).getSecretType() != null && requests.get(x.getSecretName()).getSecretType().equals(ApplicationSecretRequest.SecretType.FILE))
                .collect(Collectors.toMap(ApplicationSecret::getSecretName, Function.identity()));

        fileSecrets.values().stream().forEach(x -> {
            if (fileSecrets.containsKey(x.getSecretName())) {
                Map<String, Object> secretFileMountYaml = new HashMap<>();
                secretFileMountYaml.put("name", x.getSecretName().toLowerCase());
                secretFileMountYaml.put("value", fileSecrets.get(x.getSecretName()).getSecretValue());
                secretFileMountsList.add(secretFileMountYaml);
            }
        });

        return secretFileMountsList;
    }

    public String getApplicationIAMRole(Application application, Environment environment){
        return iamService.getApplicationRole(application, environment);
    }

    
    public String getSchedule(Deployment deployment) {
        return deployment.getSchedule();
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

    public Map<String, Object> getPortDetails(Application application) {
        final Map<String, Object> configs = new HashMap<>();
        boolean httpPresent = application.getPorts().stream().anyMatch(p -> p.getProtocol().equals(Port.Protocol.HTTP));
        boolean httpsPresent = application.getPorts().stream().anyMatch(p -> p.getProtocol().equals(Port.Protocol.HTTPS));
        boolean tcpPresent = application.getPorts().stream().anyMatch(p -> p.getProtocol().equals(Port.Protocol.TCP));

        if (tcpPresent) {
            configs.put("protocolGroup", "tcp");
        } else {
            if (httpPresent && !httpsPresent) {
                configs.put("protocolGroup", "httpOnly");
            } else if(httpsPresent && !httpPresent){
                configs.put("protocolGroup", "httpsOnly");
            } else if(httpPresent && httpsPresent){
                configs.put("protocolGroup", "http&https");
            }
        }
        return configs;
    }

    protected boolean addField(String key, Object value, Map<String, Object> yaml) {
        if (value != null) {
            yaml.put(key, value);
            return true;
        }
        return false;
    }

    protected boolean addFields(Map<String, Object> value, Map<String, Object> yaml) {
        if (value != null) {
            yaml.putAll(value);
            return true;
        }
        return false;
    }

    public Map<String, Object> addBaseDetails(Application application, Environment environment, Deployment deployment) {
        Map<String, Object> yaml = new HashMap<>();
        this.addField("deploymentId", deployment.getId(), yaml);
        this.addField("buildId", deployment.getBuildId(), yaml);
        this.addField("image", getImage(deployment), yaml);
        this.addField("podCPULimit", getPodCPULimit(deployment), yaml);
        this.addField("podMemoryLimit", getPodMemoryLimit(deployment), yaml);
        this.addField("nodeSelector", getNodeGroup(environment), yaml);
        this.addField("ports", getPorts(application), yaml);
        this.addField("configurations", getConfigMap(environment, application, deployment), yaml);
        this.addField("credentials", getCredentialsMap(environment, application), yaml);
        this.addField("secretFileMounts", getSecretFileMounts(application, environment, deployment), yaml);
        this.addField("roleName", getApplicationIAMRole(application,environment), yaml);
        return yaml;
    }

    abstract public Map<String, Object> getValues(Application application, Environment environment, Deployment deployment);
}
