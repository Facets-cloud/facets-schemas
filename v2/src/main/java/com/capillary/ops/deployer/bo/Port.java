package com.capillary.ops.deployer.bo;

public class Port {

    public enum Protocol {
        TCP,
        HTTP,
        HTTPS
    }

    public Port() {
    }

    public Port(String name, Long containerPort, Long lbPort) {
        this.name = name;
        this.containerPort = containerPort;
        this.lbPort = lbPort;
    }

    public Port(String name, Long containerPort, Long lbPort, Protocol protocol) {
        this.name = name;
        this.containerPort = containerPort;
        this.lbPort = lbPort;
        this.protocol = protocol;
    }

    String name;
    Long containerPort;
    Long lbPort;
    Protocol protocol = Protocol.TCP;

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

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }
}
