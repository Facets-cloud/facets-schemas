package com.capillary.ops.bo.helm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BuildStatus {

  public BuildStatus() {}

  public BuildStatus(String appName, Type status, Long buildId) {
    this.appName = appName;
    this.status = status;
    this.buildId = buildId;
  }

  public BuildStatus(String appName, Long buildId, String failureReason) {
    this.appName = appName;
    this.buildId = buildId;
    this.failureReason = failureReason;
    this.status = Type.FAILURE;
  }

  public enum Type {
    SUCCESS,
    PENDING,
    FAILURE
  }

  private String appName;

  private Long buildId;

  private Type status;

  private String failureReason;

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getFailureReason() {
    return failureReason;
  }

  public void setFailureReason(String failureReason) {
    this.failureReason = failureReason;
  }

  public Long getBuildId() {
    return buildId;
  }

  public void setBuildId(Long buildId) {
    this.buildId = buildId;
  }

  public Type getStatus() {
    return status;
  }

  public void setStatus(Type status) {
    this.status = status;
  }
}
