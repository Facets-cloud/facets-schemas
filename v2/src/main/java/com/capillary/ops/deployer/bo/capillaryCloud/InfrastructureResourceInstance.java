package com.capillary.ops.deployer.bo.capillaryCloud;

import org.springframework.data.annotation.Id;

public abstract class InfrastructureResourceInstance {
    @Id
    private String id;
    protected String infrastructureResourceName;
    protected String clusterName;

    public String getInfrastructureResourceName() {
        return infrastructureResourceName;
    }

    public void setInfrastructureResourceName(String infrastructureResourceName) {
        this.infrastructureResourceName = infrastructureResourceName;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
