package com.capillary.ops.bo;

public abstract class AbstractInfrastructureResource {

    private String appName;

    private Environments environment;

    private InstanceType instanceType;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Environments getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environments environment) {
        this.environment = environment;
    }
}
