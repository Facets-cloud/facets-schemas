package com.capillary.ops.cp.bo.requests;

public abstract class ClusterRequest {

    private Cloud cloud;
    private String clusterName;
    private String stackName;

    public ClusterRequest(Cloud aws) {
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public Cloud getCloud() {
        return cloud;
    }

    public String getStackName() {
        return stackName;
    }

    public void setStackName(String stackName) {
        this.stackName = stackName;
    }
}
