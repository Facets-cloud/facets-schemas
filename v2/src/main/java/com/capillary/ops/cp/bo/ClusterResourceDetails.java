package com.capillary.ops.cp.bo;

import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.Map;

@Document
public class ClusterResourceDetails {

    @JsonIgnore
    @Id
    private String id;

    @JsonIgnore
    @Indexed(name = "codebuild_id_index",unique = true)
    private String codeBuildId;

    @JsonIgnore
    private String clusterId;

    private StatusType status;

    private Map<String, String> resourceDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodeBuildId() {
        return codeBuildId;
    }

    public void setCodeBuildId(String codeBuildId) {
        this.codeBuildId = codeBuildId;
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

    public Map<String, String> getResourceDetails() {
        return resourceDetails;
    }

    public void setResourceDetails(Map<String, String> resourceDetails) {
        this.resourceDetails = resourceDetails;
    }
}
