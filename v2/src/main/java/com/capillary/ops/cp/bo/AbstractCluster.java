package com.capillary.ops.cp.bo;

import com.capillary.ops.cp.bo.requests.Cloud;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * Cluster agnostic definition of a cluster
 */
@Document
@CompoundIndex(def = "{'name':1, 'stackName':1}", name = "uniqueNamePerStack")
public abstract class AbstractCluster {

    @Id
    private String id;

    @NotNull
    private String name;

    @NotNull
    private Cloud cloud;

    @NotNull
    private String tz;

    @NotNull
    private String stackName;

    @NotNull
    private BuildStrategy releaseStream;

    private String cdPipelineParent;

    private Boolean requireSignOff;

    private String autoSignOffSchedule;

    private Boolean enableAutoSignOff = false;

    @Transient
    private Map<String, String> commonEnvironmentVariables = new HashMap<>();

    @JsonIgnore
    private Map<String, String> userInputVars = new HashMap<>();

    @Transient
    private Map<String, String> secrets;

    private Map<ReleaseType, String> schedules = new HashMap<>();

    private double k8sRequestsToLimitsRatio = 1;

    public AbstractCluster(String name, Cloud cloud) {
        this.name = name;
        this.cloud = cloud;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Map<String, String> getCommonEnvironmentVariables() {
        return commonEnvironmentVariables.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void setCommonEnvironmentVariables(Map<String, String> commonEnvironmentVariables) {
        this.commonEnvironmentVariables = commonEnvironmentVariables;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(TimeZone tz) {
        this.tz = tz.getID();
    }

    public BuildStrategy getReleaseStream() {
        return releaseStream;
    }

    public void setReleaseStream(BuildStrategy releaseStream) {
        this.releaseStream = releaseStream;
    }

    public Map<String, String> getUserInputVars() {
        return userInputVars;
    }

    public void setUserInputVars(Map<String, String> userInputVars) {
        this.userInputVars = userInputVars;
    }

    public void setSecrets(Map<String, String> secrets) {
        this.secrets = secrets;
    }

    public Map<String, String> getSecrets() {
        return secrets;
    }

    public double getK8sRequestsToLimitsRatio() {
        return k8sRequestsToLimitsRatio;
    }

    public void setK8sRequestsToLimitsRatio(double k8sRequestsToLimitsRatio) {
        this.k8sRequestsToLimitsRatio = k8sRequestsToLimitsRatio;
    }

    public Map<ReleaseType, String> getSchedules() {
        return schedules;
    }

    public void setSchedules(Map<ReleaseType, String> schedules) {
        if (schedules != null) {
            this.schedules = schedules;
        }
    }

    public String getCdPipelineParent() {
        return cdPipelineParent;
    }

    public void setCdPipelineParent(String cdPipelineParent) {
        this.cdPipelineParent = cdPipelineParent;
    }

    public Boolean getRequireSignOff() {
        if(requireSignOff == null) {
            return false;
        }
        return requireSignOff;
    }

    public void setRequireSignOff(Boolean requireSignOff) {
        this.requireSignOff = requireSignOff;
    }

    public String getAutoSignOffSchedule() {
        if(! enableAutoSignOff) {
            return null;
        }
        return autoSignOffSchedule;
    }

    public void setAutoSignOffSchedule(String autoSignOffSchedule) {
        if(autoSignOffSchedule != null) {
            this.autoSignOffSchedule = autoSignOffSchedule;
        }
    }

    public Boolean getEnableAutoSignOff() {
        return enableAutoSignOff;
    }

    public void setEnableAutoSignOff(Boolean enableAutoSignOff) {
        if (enableAutoSignOff != null) {
            this.enableAutoSignOff = enableAutoSignOff;
        }
    }

}
