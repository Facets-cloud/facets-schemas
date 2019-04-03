package com.capillary.ops.bo.helm;

import java.util.List;

public class Port {

    public static boolean areValid(List<Port> ports) {
        return true;
    }

    public enum Protocol {
        TCP,
        HTTP
    }

    public Port() {}

    public Port(int internalPort, int externalPort, Protocol protocol) {
        this.internalPort = internalPort;
        this.externalPort = externalPort;
        this.protocol = protocol;
    }

    private int internalPort;

    private int externalPort;

    private Protocol protocol;

    public int getInternalPort() {
        return internalPort;
    }

    public void setInternalPort(int internalPort) {
        this.internalPort = internalPort;
    }

    public int getExternalPort() {
        return externalPort;
    }

    public void setExternalPort(int externalPort) {
        this.externalPort = externalPort;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }
}
