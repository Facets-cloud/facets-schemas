package com.capillary.ops.deployer.service.interfaces;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.LogEvent;

import java.util.List;

public interface ICodeBuildService {
    void createProject(Application application);

    String triggerBuild(Application application, Build build);

    List<LogEvent> getBuildLogs(String codeBuildId);

    software.amazon.awssdk.services.codebuild.model.Build getBuild(String codeBuildId);
}
