package com.capillary.ops.cp.bo;

public class InstancePort {
    private String name;

    private Integer containerPort;

    private Integer lbPort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getContainerPort() {
        return containerPort;
    }

    public void setContainerPort(Integer containerPort) {
        this.containerPort = containerPort;
    }

    public Integer getLbPort() {
        return lbPort;
    }

    public void setLbPort(Integer lbPort) {
        this.lbPort = lbPort;
    }
}
