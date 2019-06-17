package com.capillary.ops.deployer.bo;

import java.util.Map;

public class EnvironmentConfiguration {
    private String kubernetesToken;
    private String nodeGroup;
    private String kubernetesApiEndpoint;
    private ExternalDnsConfiguration privateDnsConfiguration;
    private ExternalDnsConfiguration publicDnsConfiguration;
    private Map<String, String> commonConfigs;
    private Map<String, String> commonCredentials;

    public String getKubernetesToken() {
        return kubernetesToken;
    }

    public void setKubernetesToken(String kubernetesToken) {
        this.kubernetesToken = kubernetesToken;
    }

    public String getNodeGroup() {
        return nodeGroup;
    }

    public void setNodeGroup(String nodeGroup) {
        this.nodeGroup = nodeGroup;
    }

    public String getKubernetesApiEndpoint() {
        return kubernetesApiEndpoint;
    }

    public void setKubernetesApiEndpoint(String kubernetesApiEndpoint) {
        this.kubernetesApiEndpoint = kubernetesApiEndpoint;
    }

    public ExternalDnsConfiguration getPrivateDnsConfiguration() {
        return privateDnsConfiguration;
    }

    public void setPrivateDnsConfiguration(ExternalDnsConfiguration privateDnsConfiguration) {
        this.privateDnsConfiguration = privateDnsConfiguration;
    }

    public ExternalDnsConfiguration getPublicDnsConfiguration() {
        return publicDnsConfiguration;
    }

    public void setPublicDnsConfiguration(ExternalDnsConfiguration publicDnsConfiguration) {
        this.publicDnsConfiguration = publicDnsConfiguration;
    }

    public Map<String, String> getCommonConfigs() {
        return commonConfigs;
    }

    public void setCommonConfigs(Map<String, String> commonConfigs) {
        this.commonConfigs = commonConfigs;
    }

    public Map<String, String> getCommonCredentials() {
        return commonCredentials;
    }

    public void setCommonCredentials(Map<String, String> commonCredentials) {
        this.commonCredentials = commonCredentials;
    }
}
