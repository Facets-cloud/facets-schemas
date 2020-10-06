package com.capillary.ops.cp.bo;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppDeployment that = (AppDeployment) o;
        return appName.equals(that.appName) &&
                artifact.equals(that.artifact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appName, artifact);
    }
}
