package com.capillary.ops.cp.bo;

public class K8sConfig {

    private String kubernetesApiEndpoint;

    private String kubernetesToken;

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
}
