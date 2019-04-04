package com.capillary.ops.deployer.bo;

public class Environment {
    private String name;
    private String kubernetesApiEndpoint;
    private String kubernetesToken;
    private String nodeGroup;

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
}
