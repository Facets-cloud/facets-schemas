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
    @NotNull
    private String tag;
    private StatusType status;
    private Map<String, String> environmentVariable;
    private Long timestamp = new Date().getTime();
    private String image;

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

    public Map<String, String> getEnvironmentVariable() {
        return environmentVariable;
    }

    public void setEnvironmentVariable(Map<String, String> environmentVariable) {
        this.environmentVariable = environmentVariable;
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
}
