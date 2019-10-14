package com.capillary.ops.deployer.bo;

public class Probe {

    public Probe() {
    }

    public Probe(int port, int initialDelaySeconds, int periodSeconds, String httpCheckEndpoint, int successThreshold, int failureThreshold, int timeout) {
        this.port = port;
        this.initialDelaySeconds = initialDelaySeconds;
        this.periodSeconds = periodSeconds;
        this.httpCheckEndpoint = httpCheckEndpoint;
        this.successThreshold = successThreshold;
        this.failureThreshold = failureThreshold;
        this.timeout = timeout;
    }

    private int port;

    private int initialDelaySeconds;

    private int periodSeconds;

    private String httpCheckEndpoint;

    private int successThreshold;

    private int failureThreshold;

    private int timeout;

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

    public int getSuccessThreshold() {
        if(successThreshold == 0) {
            return 1;
        }
        return successThreshold;
    }

    public void setSuccessThreshold(int successThreshold) {
        this.successThreshold = successThreshold;
    }

    public int getFailureThreshold() {
        if(failureThreshold == 0) {
            return 3;
        }
        return failureThreshold;
    }

    public void setFailureThreshold(int failureThreshold) {
        this.failureThreshold = failureThreshold;
    }

    public int getTimeout() {
        if(timeout == 0) {
            return 1;
        }
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
