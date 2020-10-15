package com.capillary.ops.deployer.bo;

public class EnvironmentMetaData {

    private Boolean isCapCloud = false;
    private String capillaryCloudClusterName;

    public EnvironmentMetaData(EnvironmentType environmentType, String name, ApplicationFamily applicationFamily) {
        this.environmentType = environmentType;
        this.name = name;
        this.applicationFamily = applicationFamily;
    }

    public EnvironmentMetaData(String capillaryCloudClusterName, EnvironmentType environmentType, String name, ApplicationFamily applicationFamily) {
        this.capillaryCloudClusterName = capillaryCloudClusterName;
        this.environmentType = environmentType;
        this.name = name;
        this.applicationFamily = applicationFamily;
    }

    public EnvironmentMetaData() {
    }

    private EnvironmentType environmentType;
    private String name;
    private ApplicationFamily applicationFamily;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnvironmentType getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(EnvironmentType environmentType) {
        this.environmentType = environmentType;
    }

    public ApplicationFamily getApplicationFamily() {
        return applicationFamily;
    }

    public void setApplicationFamily(ApplicationFamily applicationFamily) {
        this.applicationFamily = applicationFamily;
    }

    public String getCapillaryCloudClusterName() {
        return capillaryCloudClusterName;
    }

    public void setCapillaryCloudClusterName(String capillaryCloudClusterName) {
        this.capillaryCloudClusterName = capillaryCloudClusterName;
    }

    public Boolean getCapCloud() {
        return isCapCloud;
    }

    public void setCapCloud(Boolean capCloud) {
        isCapCloud = capCloud;
    }

    @Override
    public String toString() {
        return "EnvironmentMetaData{" +
                "isCapCloud=" + isCapCloud +
                ", capillaryCloudClusterName='" + capillaryCloudClusterName + '\'' +
                ", environmentType=" + environmentType +
                ", name='" + name + '\'' +
                ", applicationFamily=" + applicationFamily +
                '}';
    }
}
