package com.capillary.ops.cp.bo;

import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.deployer.bo.Deployment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Document
public class DeploymentLog {

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

    private DeploymentContext deploymentContext;

    List<TerraformChange> changesApplied;

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

    public DeploymentContext getDeploymentContext() {
        return deploymentContext;
    }

    public void setDeploymentContext(DeploymentContext deploymentContext) {
        this.deploymentContext = deploymentContext;
    }

    public List<TerraformChange> getChangesApplied() {
        return changesApplied;
    }

    public void setChangesApplied(List<TerraformChange> changesApplied) {
        this.changesApplied = changesApplied;
    }
}
