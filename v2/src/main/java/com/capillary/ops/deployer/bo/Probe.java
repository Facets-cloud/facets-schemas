package com.capillary.ops.deployer.bo;

public class Probe {

    private int port;

    private int initialDelaySeconds;

    private int periodSeconds;

    private String httpCheckEndpoint;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getInitialDelaySeconds() {
        return initialDelaySeconds;
    }

    public void setInitialDelaySeconds(int initialDelaySeconds) {
        this.initialDelaySeconds = initialDelaySeconds;
    }

    public int getPeriodSeconds() {
        return periodSeconds;
    }

    public void setPeriodSeconds(int periodSeconds) {
        this.periodSeconds = periodSeconds;
    }

    public String getHttpCheckEndpoint() {
        return httpCheckEndpoint;
    }

    public void setHttpCheckEndpoint(String httpCheckEndpoint) {
        this.httpCheckEndpoint = httpCheckEndpoint;
    }

}
