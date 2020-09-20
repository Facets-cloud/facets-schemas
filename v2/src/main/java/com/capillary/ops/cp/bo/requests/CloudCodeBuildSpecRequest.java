package com.capillary.ops.cp.bo.requests;

public class CloudCodeBuildSpecRequest {
    private String clusterId;

    private CloudCodeBuildSpecBuildPhase buildPhase;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public CloudCodeBuildSpecBuildPhase getBuildPhase() {
        return buildPhase;
    }

    public void setBuildPhase(CloudCodeBuildSpecBuildPhase buildPhase) {
        this.buildPhase = buildPhase;
    }
}
