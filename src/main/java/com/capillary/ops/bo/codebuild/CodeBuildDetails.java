package com.capillary.ops.bo.codebuild;

import software.amazon.awssdk.services.codebuild.model.Build;
import software.amazon.awssdk.services.codebuild.model.StartBuildResponse;

import java.util.List;

public class CodeBuildDetails {

    private String buildId;

    private String branch;

    private String buildStatus;

    public CodeBuildDetails(List<Build> builds) {

    }

    public CodeBuildDetails() {

    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public CodeBuildDetails fromStartBuild(StartBuildResponse startBuildResponse) {
        return null;
    }

    public CodeBuildDetails fromGetBuild(Build builds) {
        return null;
    }

    public CodeBuildDetails fromStopBuild(Build build) {
        return null;
    }
}
