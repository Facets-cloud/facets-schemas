package com.capillary.ops.deployer.bo;

public class Environment {
    private String name;
    private String kubernetesApiEndpoint;
    private String kubernetesToken;
    private String nodeGroup;
    private String privateZoneId;
    private String publicZoneId;
    private String awsAccessKey;
    private String awsSecretKey;

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

    public String getAwsAccessKey() {
        return awsAccessKey;
    }

    public void setAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
    }

    public String getAwsSecretKey() {
        return awsSecretKey;
    }

    public void setAwsSecretKey(String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
    }
}
