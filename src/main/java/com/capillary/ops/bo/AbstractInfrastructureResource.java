package com.capillary.ops.bo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

public abstract class AbstractInfrastructureResource {

    @Id
    @JsonIgnore
    private String id;

    private String appName;

    private String resourceName;

    /**
     * Environment where the resource should be deployed, e.g., NIGHTLY, STAGE, PROD
     */
    private Environments environment;

    /**
     * Defines cpu and memory limits for the resource
     */
    private String instanceType;

    /**
     * Size of the physical volume
     */
    private Integer volumeSize;

    /**
     * Name of helm deployment
     * Should not be exposed to the end user
     */
    @JsonIgnore
    private String deploymentName;

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

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public Integer getVolumeSize() {
        return volumeSize;
    }

    public void setVolumeSize(Integer volumeSize) {
        this.volumeSize = volumeSize;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getDeploymentName() {
        return deploymentName;
    }

    public void setDeploymentName(String deploymentName) {
        this.deploymentName = deploymentName;
    }
}
