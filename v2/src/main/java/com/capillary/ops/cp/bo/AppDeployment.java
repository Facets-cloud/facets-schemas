package com.capillary.ops.cp.bo;

public class AppDeployment {
    private String appName;

    private Artifact artifact;

    public AppDeployment(String appName, Artifact artifact) {
        this.appName = appName;
        this.artifact = artifact;
    }

    public AppDeployment() {
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }
}
