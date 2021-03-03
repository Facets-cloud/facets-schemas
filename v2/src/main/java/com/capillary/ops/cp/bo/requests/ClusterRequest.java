package com.capillary.ops.cp.bo.requests;

import com.capillary.ops.cp.bo.BuildStrategy;
import io.swagger.annotations.ApiParam;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public abstract class ClusterRequest {

    private Cloud cloud;
    private String clusterName;
    private String stackName;
    private String cdPipelineParent;
    private Boolean requireSignOff;

    @ApiParam(type = "string")
    private TimeZone tz;

    private BuildStrategy releaseStream;
    private Map<String, String> clusterVars = new HashMap<>();
    private Double k8sRequestsToLimitsRatio;
    private Map<ReleaseType, String> schedules;
    private String autoSignOffSchedule;
    private Boolean enableAutoSignOff;

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

    public Map<String, String> getClusterVars() {
        return clusterVars;
    }

    public void setClusterVars(Map<String, String> clusterVars) {
        this.clusterVars = clusterVars;
    }

    public Double getK8sRequestsToLimitsRatio() {
        return k8sRequestsToLimitsRatio;
    }

    public void setK8sRequestsToLimitsRatio(Double k8sRequestsToLimitsRatio) {
        this.k8sRequestsToLimitsRatio = k8sRequestsToLimitsRatio;
    }

    public Map<ReleaseType, String> getSchedules() {
        return schedules;
    }

    public void setSchedules(Map<ReleaseType, String> schedules) {
        this.schedules = schedules;
    }

    public String getCdPipelineParent() {
        return cdPipelineParent;
    }

    public void setCdPipelineParent(String cdPipelineParent) {
        this.cdPipelineParent = cdPipelineParent;
    }

    public Boolean getRequireSignOff() {
        return requireSignOff;
    }

    public void setRequireSignOff(Boolean requireSignOff) {
        this.requireSignOff = requireSignOff;
    }

    public String getAutoSignOffSchedule() {
        return autoSignOffSchedule;
    }

    public void setAutoSignOffSchedule(String autoSignOffSchedule) {
        this.autoSignOffSchedule = autoSignOffSchedule;
    }

    public Boolean getEnableAutoSignOff() {
        return enableAutoSignOff;
    }

    public void setEnableAutoSignOff(Boolean enableAutoSignOff) {
        this.enableAutoSignOff = enableAutoSignOff;
    }
}
