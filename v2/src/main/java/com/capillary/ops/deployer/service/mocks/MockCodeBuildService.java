package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.LogEvent;
import com.capillary.ops.deployer.service.interfaces.ICodeBuildService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Profile("dev")
@Service
public class MockCodeBuildService implements ICodeBuildService {

    @Override
    public void createProject(Application application) {
    }

    @Override
    public String triggerBuild(Application application, Build build) {
        return "somebuildid";
    }

    @Override
    public List<LogEvent> getBuildLogs(String codeBuildId) {
        return Arrays.asList(new LogEvent(new Date().getTime(), "log1"),
                new LogEvent(new Date().getTime(), "log2"));
    }

    @Override
    public software.amazon.awssdk.services.codebuild.model.Build getBuild(String codeBuildId) {
        return software.amazon.awssdk.services.codebuild.model.Build.builder().buildStatus(StatusType.SUCCEEDED).build();
    }
}
