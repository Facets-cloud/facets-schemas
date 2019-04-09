package com.capillary.ops.deployer.bo;

public class Port {
    String name;
    Long containerPort;
    Long lbPort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getContainerPort() {
        return containerPort;
    }

    public void setContainerPort(Long containerPort) {
        this.containerPort = containerPort;
    }

    public Long getLbPort() {
        return lbPort;
    }

    public void setLbPort(Long lbPort) {
        this.lbPort = lbPort;
    }
}
