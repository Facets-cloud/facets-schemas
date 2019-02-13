package com.capillary.ops.bo.codebuild;

import java.util.List;
import software.amazon.awssdk.services.codebuild.model.Build;
import software.amazon.awssdk.services.codebuild.model.StartBuildResponse;

public class CodeBuildDetails {

  private String buildId;

  private String branch;

  private String buildStatus;

  private String buildLogs;

  public CodeBuildDetails(List<Build> builds) {}

  public CodeBuildDetails() {}

  public String getBranch() {
    return branch;
  }

  public void setBranch(String branch) {
    this.branch = branch;
  }

  public String getBuildId() {
    return buildId;
  }

  public void setBuildId(String buildId) {
    this.buildId = buildId;
  }

  public String getBuildStatus() {
    return buildStatus;
  }

  public void setBuildStatus(String buildStatus) {
    this.buildStatus = buildStatus;
  }

  public String getBuildLogs() {
    return buildLogs;
  }

  public void setBuildLogs(String buildLogs) {
    this.buildLogs = buildLogs;
  }

  public CodeBuildDetails fromStartBuild(StartBuildResponse startBuildResponse) {
    CodeBuildDetails buildDetails = new CodeBuildDetails();
    Build build = startBuildResponse.build();
    buildDetails.setBranch(build.sourceVersion());
    buildDetails.setBuildId(build.id());
    buildDetails.setBuildStatus(build.buildStatusAsString());
    return buildDetails;
  }

  public CodeBuildDetails fromGetBuild(Build build) {
    CodeBuildDetails buildDetails = new CodeBuildDetails();
    buildDetails.setBranch(build.sourceVersion());
    buildDetails.setBuildId(build.id());
    buildDetails.setBuildStatus(build.buildStatusAsString());
    return buildDetails;
  }

  public CodeBuildDetails fromStopBuild(Build build) {
    CodeBuildDetails buildDetails = new CodeBuildDetails();
    buildDetails.setBranch(build.sourceVersion());
    buildDetails.setBuildId(build.id());
    buildDetails.setBuildStatus(build.buildStatusAsString());
    return buildDetails;
  }
}
