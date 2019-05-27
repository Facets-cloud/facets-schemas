package com.capillary.ops.deployer.bo;

public class Environment {
    private String name;
    private String kubernetesApiEndpoint;
    private String kubernetesToken;
    private String nodeGroup;
    private String privateZoneId;
    private String publicZoneId;
    private String privateZoneDns;
    private String publicZoneDns;
    private String clusterPrefix;
    private String awsAccessKeyId;
    private String awsSecretAccessKey;
    private String adPassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKubernetesApiEndpoint() {
        return kubernetesApiEndpoint;
    }

    public void setKubernetesApiEndpoint(String kubernetesApiEndpoint) {
        this.kubernetesApiEndpoint = kubernetesApiEndpoint;
    }

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

    public String getPrivateZoneId() {
        return privateZoneId;
    }

    public void setPrivateZoneId(String privateZoneId) {
        this.privateZoneId = privateZoneId;
    }

    public String getPublicZoneId() {
        return publicZoneId;
    }

    public void setPublicZoneId(String publicZoneId) {
        this.publicZoneId = publicZoneId;
    }

    public String getPrivateZoneDns() {
        return privateZoneDns == null ? "" : privateZoneDns;
    }

    public void setPrivateZoneDns(String privateZoneDns) {
        this.privateZoneDns = privateZoneDns;
    }

    public String getPublicZoneDns() {
        return publicZoneDns;
    }

    public void setPublicZoneDns(String publicZoneDns) {
        this.publicZoneDns = publicZoneDns;
    }

    public String getClusterPrefix() {
        return clusterPrefix;
    }

    public void setClusterPrefix(String clusterPrefix) {
        this.clusterPrefix = clusterPrefix;
    }

    public String getAwsAccessKeyId() {
        return awsAccessKeyId;
    }

    public void setAwsAccessKeyId(String awsAccessKeyId) {
        this.awsAccessKeyId = awsAccessKeyId;
    }

    public String getAwsSecretAccessKey() {
        return awsSecretAccessKey;
    }

    public void setAwsSecretAccessKey(String awsSecretAccessKey) {
        this.awsSecretAccessKey = awsSecretAccessKey;
    }

    public String getAdPassword() {
        return adPassword;
    }

    public void setAdPassword(String adPassword) {
        this.adPassword = adPassword;
    }
}
