package com.capillary.ops.deployer.service.interfaces;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.LogEvent;
import com.capillary.ops.deployer.bo.TokenPaginatedResponse;

public interface ICodeBuildService {
    void createProject(Application application);

    String triggerBuild(Application application, Build build);

    TokenPaginatedResponse<LogEvent> getBuildLogs(Application application, String codeBuildId, String nextToken);

    software.amazon.awssdk.services.codebuild.model.Build getBuild(Application application, String codeBuildId);
}
