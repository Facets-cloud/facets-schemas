package com.capillary.ops.deployer.bo;

import org.springframework.data.annotation.Id;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

public class Build {

    @Id
    private String id;
    private String codeBuildId;
    @NotNull
    private String applicationId;
    private ApplicationFamily applicationFamily;
    @NotNull
    private String tag;
    private StatusType status;
    private Map<String, String> environmentVariables;
    private Long timestamp = new Date().getTime();
    private String image;
    private String triggeredBy;
    private String description;
    private boolean promoted = false;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

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

    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void setEnvironmentVariables(Map<String, String> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public ApplicationFamily getApplicationFamily() {
        return applicationFamily;
    }

    public void setApplicationFamily(ApplicationFamily applicationFamily) {
        this.applicationFamily = applicationFamily;
    }
}
