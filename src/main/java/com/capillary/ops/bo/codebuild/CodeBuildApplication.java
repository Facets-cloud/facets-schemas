package com.capillary.ops.bo.codebuild;

import com.capillary.ops.bo.Application;
import software.amazon.awssdk.services.codebuild.model.CreateProjectRequest;

public class CodeBuildApplication extends Application {

    public enum ApplicationSource {
        GITHUB, BITBUCKET
    }

    ApplicationSource sourceType;

    public ApplicationSource getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = ApplicationSource.valueOf(sourceType);
    }

    public CodeBuildApplication fromCreateProject(CreateProjectRequest createProjectRequest) {
        return new CodeBuildApplication();
    }
}
