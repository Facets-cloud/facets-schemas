package com.capillary.ops.deployer.bo.capillaryCloud;

import org.springframework.data.annotation.Id;

public abstract class InfrastructureResourceInstance {
    @Id
    private String id;
    protected String infrastructureResourceId;
    protected String clusterId;

    public String getInfrastructureResourceId() {
        return infrastructureResourceId;
    }

    public void setInfrastructureResourceId(String infrastructureResourceId) {
        this.infrastructureResourceId = infrastructureResourceId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
