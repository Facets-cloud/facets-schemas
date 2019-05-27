package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.LogEvent;
import com.capillary.ops.deployer.bo.TokenPaginatedResponse;
import com.capillary.ops.deployer.service.interfaces.ICodeBuildService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.*;

@Profile("dev")
@Service
public class MockCodeBuildService implements ICodeBuildService {

    private static final Map<String, List<LogEvent>> logsStorage =
            Collections.synchronizedMap(new LinkedHashMap<>(10, 0.75f, true));

    @Override
    public void createProject(Application application) {
    }

    @Override
    public String triggerBuild(Application application, Build build) {
        return UUID.randomUUID().toString();
    }

    @Override
    public TokenPaginatedResponse<LogEvent> getBuildLogs(String codeBuildId, String nextToken) {
        List<LogEvent> logs = logsStorage.getOrDefault(codeBuildId, new ArrayList<>());
        if(logs.size() < 10) {
            logs.add(new LogEvent(new Date().getTime(), UUID.randomUUID().toString()));
            logsStorage.put(codeBuildId, logs);
        }
        return new TokenPaginatedResponse<>(logs, "");
    }

    @Override
    public software.amazon.awssdk.services.codebuild.model.Build getBuild(String codeBuildId) {
        List<LogEvent> logs = logsStorage.getOrDefault(codeBuildId, new ArrayList<>());
        StatusType status = StatusType.IN_PROGRESS;
        if (logs.size() >= 10) {
            status = StatusType.SUCCEEDED;
        }
        return software.amazon.awssdk.services.codebuild.model.Build.builder()
                .buildStatus(status).build();
    }
}
