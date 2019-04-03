package com.capillary.ops.bo.codebuild;

import com.capillary.ops.bo.Application;
import software.amazon.awssdk.services.codebuild.model.CreateProjectRequest;
import software.amazon.awssdk.services.codebuild.model.Project;

public class CodeBuildApplication extends Application {

  public CodeBuildApplication() {}

  public CodeBuildApplication(ApplicationSource sourceType, ApplicationType applicationType, String namespace) {
    this.sourceType = sourceType;
    this.applicationType = applicationType;
    this.namespace = namespace;
  }

  public enum ApplicationSource {
    GITHUB,
    BITBUCKET
  }

  public enum ApplicationType {
    MAVEN_JAVA,
    DOCKER,
    NETCORE
  }

  ApplicationSource sourceType;

  ApplicationType applicationType;

  String namespace = "default";

  public ApplicationSource getSourceType() {
    return sourceType;
  }

  public void setSourceType(String sourceType) {
    this.sourceType = ApplicationSource.valueOf(sourceType);
  }

  public ApplicationType getApplicationType() {
    return applicationType;
  }

  public void setApplicationType(String applicationType) {
    this.applicationType = ApplicationType.valueOf(applicationType);
  }

  public CodeBuildApplication fromCreateProject(CreateProjectRequest createProjectRequest) {
    return this;
  }

  public CodeBuildApplication fromGetProject(Project project) {
      return this;
  }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
