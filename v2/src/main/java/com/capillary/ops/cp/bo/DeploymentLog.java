package com.capillary.ops.cp.bo;

import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class DeploymentLog {

    @Id
    private String id;

    @JsonIgnore
    @Indexed(name = "cluster_id_index", unique = false)
    private String clusterId;

    private String codebuildId;

    private String description;

    private Date createdOn = new Date();

    private ReleaseType releaseType;


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
}
