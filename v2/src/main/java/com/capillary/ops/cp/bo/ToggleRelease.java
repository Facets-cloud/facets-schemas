package com.capillary.ops.cp.bo;

public class ToggleRelease {
    private String stackName;

    private boolean pauseReleases;

    public String getStackName() {
        return stackName;
    }

    public void setStackName(String stackName) {
        this.stackName = stackName;
    }

    public boolean isPauseReleases() {
        return pauseReleases;
    }

    public void setPauseReleases(boolean pauseReleases) {
        this.pauseReleases = pauseReleases;
    }
}
