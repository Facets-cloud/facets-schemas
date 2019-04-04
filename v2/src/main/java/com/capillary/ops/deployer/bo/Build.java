package com.capillary.ops.deployer.bo;

public class Build {

    public static enum BuildStatus {
        RUNNING,
        COMPLETED,
        FAILED
    }

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

}
