package com.capillary.ops.deployer.service.interfaces;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.LogEvent;
import com.capillary.ops.deployer.bo.TokenPaginatedResponse;

import java.util.List;

public interface ICodeBuildService {
    void createProject(Application application);

    String triggerBuild(Application application, Build build, boolean testBuild);

    TokenPaginatedResponse<LogEvent> getBuildLogs(Application application, String codeBuildId, String nextToken);

    software.amazon.awssdk.services.codebuild.model.Build getBuild(Application application, String codeBuildId);

    List<software.amazon.awssdk.services.codebuild.model.Build> getBuilds(Application application, List<String> codeBuildIds);

    void deleteProject(Application application);
}
