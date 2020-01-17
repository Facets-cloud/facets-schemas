package com.capillary.ops.deployer.bo.capillaryCloud;

import org.springframework.data.annotation.Id;

public class InfrastructureResource {
    @Id
    private String id;
    private String name;
    private InfrastructureResourceType type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
