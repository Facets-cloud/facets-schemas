package com.capillary.ops.deployer.bo;

import org.springframework.data.annotation.Id;
import software.amazon.awssdk.services.cloudwatchlogs.model.OutputLogEvent;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.List;

public class Build {

    @Id
    private String id;
    private String codeBuildId;
    private String applicationId;
    private String tag;
    private StatusType status;

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

}
