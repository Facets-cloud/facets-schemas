package com.capillary.ops.deployer.bo;

import org.springframework.data.annotation.Id;

public class Build {

    public static enum BuildStatus {
        RUNNING,
        COMPLETED,
        FAILED
    }

    @Id
    private String id;
    private String applicationName;
    private String tag;
    private BuildStatus status;
    private String logs;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public BuildStatus getStatus() {
        return status;
    }

    public void setStatus(BuildStatus status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }
}
