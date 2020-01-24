package com.capillary.ops.deployer.bo.capillaryCloud;

import org.springframework.data.annotation.Id;

public class InfrastructureResource {

    public InfrastructureResource() {
    }

    public InfrastructureResource(String name, InfrastructureResourceType type) {
        this.name = name;
        this.type = type;
    }

    private String name;
    private InfrastructureResourceType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InfrastructureResourceType getType() {
        return type;
    }

    public void setType(InfrastructureResourceType type) {
        this.type = type;
    }
}
