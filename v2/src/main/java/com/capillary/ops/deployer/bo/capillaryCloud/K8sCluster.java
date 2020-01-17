package com.capillary.ops.deployer.bo.capillaryCloud;

public class K8sCluster extends InfrastructureResourceInstance {
    private String token;
    private String apiEndpoint;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }
}
