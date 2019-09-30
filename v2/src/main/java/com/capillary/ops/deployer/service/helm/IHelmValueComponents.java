package com.capillary.ops.deployer.service.helm;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.bo.Environment;

import java.util.List;
import java.util.Map;

public interface IHelmValueComponents {
    public String getDeploymentId(Deployment deployment);
    public String getBuildId(Deployment deployment);
    public String getImage(Deployment deployment);
    public Integer getPodCPULimit(Deployment deployment);
    public Integer getPodMemoryLimit(Deployment deployment);
    public String getNodeGroup(Environment environment);
    public String getLbType(Application application);
    public List<Map<String, Object>> getPorts(Application application);
    public Map<String, String> getConfigMap(Environment environment, Application application, Deployment deployment);
    public Map<String, String> getCredentialsMap(Environment environment, Application application);
    public String getSSLCertificateName(Application application, Environment environment);
    public String getPrivateZoneDns(Application application, Environment environment);
    public String getPublicZoneDns(Application application, Environment environment);
    public Map<String, Object> getFamilySpecificAttributes(Application application, Deployment deployment);
    public Map<String, Object> getHPAConfigs(Deployment deployment);
    public Map<String, Object> getHealthCheckConfigs(Application application);
    public List<Map<String, Object>> getPVCList(Application application);
    public List<Map<String, Object>> getSecretFileMounts(Application application, Environment environment, Deployment deployment);
    public String getSchedule(Deployment deployment);
    public String getConcurrencyPolicy(Deployment deployment);
}
