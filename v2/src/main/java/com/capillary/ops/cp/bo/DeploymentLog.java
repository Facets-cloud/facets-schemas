package com.capillary.ops.cp.bo;

import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
public class DeploymentLog {

    public static enum DeploymentType {
        REGULAR,
        CUSTOM,
        ROLLBACK
    }

    @Id
    private String id;

    @JsonIgnore
    @Indexed(name = "cluster_id_index", unique = false)
    private String clusterId;

    @Indexed(unique = true, name = "codebuildId_idx")
    private String codebuildId;

    private String description;

    private Date createdOn = new Date();

    private ReleaseType releaseType;

    private StatusType status;

    private List<TerraformChange> changesApplied;

    private List<AppDeployment> appDeployments;

    private List<String> errorLogs;

    private DeploymentType deploymentType = DeploymentType.REGULAR;

    private String stackVersion;

    private String tfVersion;

    private Boolean signedOff = false;

    private String deploymentContextVersion;

    private String triggeredBy;

    private List<String> overrideBuildSteps = new ArrayList<>();

    private boolean isTestDeployment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ReleaseType getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(ReleaseType releaseType) {
        this.releaseType = releaseType;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodebuildId() {
        return codebuildId;
    }

    public void setCodebuildId(String codebuildId) {
        this.codebuildId = codebuildId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public List<TerraformChange> getChangesApplied() {
        return changesApplied;
    }

    public void setChangesApplied(List<TerraformChange> changesApplied) {
        this.changesApplied = changesApplied;
    }

    public List<AppDeployment> getAppDeployments() {
        return appDeployments;
    }

    public void setAppDeployments(List<AppDeployment> appDeployments) {
        this.appDeployments = appDeployments;
    }

    public DeploymentType getDeploymentType() {
        if (deploymentType == null) {
            return DeploymentType.REGULAR;
        }
        return deploymentType;
    }

    public void setDeploymentType(DeploymentType deploymentType) {
        this.deploymentType = deploymentType;
    }

    public String getStackVersion() {
        return stackVersion;
    }

    public void setStackVersion(String stackVersion) {
        this.stackVersion = stackVersion;
    }

    public String getTfVersion() {
        return tfVersion;
    }

    public void setTfVersion(String tfVersion) {
        this.tfVersion = tfVersion;
    }

    public Boolean getSignedOff() {
        return signedOff;
    }

    public void setSignedOff(Boolean signedOff) {
        this.signedOff = signedOff;
    }

    public List<String> getErrorLogs() {
        return errorLogs;
    }

    public void setErrorLogs(List<String> errorLogs) {
        this.errorLogs = errorLogs;
    }

    public String getDeploymentContextVersion() {
        return deploymentContextVersion;
    }

    public void setDeploymentContextVersion(String deploymentContextVersion) {
        this.deploymentContextVersion = deploymentContextVersion;
    }

    public List<String> getOverrideBuildSteps() {
        return overrideBuildSteps;
    }

    public void setOverrideBuildSteps(List<String> overrideBuildSteps) {
        this.overrideBuildSteps = overrideBuildSteps;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public boolean isTestDeployment() {
        return isTestDeployment;
    }

    public void setTestDeployment(boolean testDeployment) {
        isTestDeployment = testDeployment;
    }
}
