package com.capillary.ops.cp.bo.requests;

import com.capillary.ops.cp.bo.BuildStrategy;
import java.util.TimeZone;

public abstract class ClusterRequest {

    private Cloud cloud;
    private String clusterName;
    private String stackName;
    private TimeZone tz;
    private BuildStrategy releaseStream;

    public ClusterRequest() {
    }

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

    public TimeZone getTz() {
        return tz;
    }

    public void setTz(TimeZone tz) {
        this.tz = tz;
    }

    public BuildStrategy getReleaseStream() {
        return releaseStream;
    }

    public void setReleaseStream(BuildStrategy releaseStream) {
        this.releaseStream = releaseStream;
    }
}
