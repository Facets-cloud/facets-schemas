package com.capillary.ops.service;

import com.capillary.ops.bo.codebuild.CodeBuildApplication;
import com.capillary.ops.bo.codebuild.CodeBuildDetails;

public interface CodeBuildService {
    CodeBuildApplication createApplication(CodeBuildApplication application);

    CodeBuildDetails createBuild(String applicationId, CodeBuildDetails details);

    CodeBuildDetails getBuildDetails(String buildId);

    CodeBuildDetails stopBuild(String buildId);
}
