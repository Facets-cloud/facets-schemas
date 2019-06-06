package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.LogEvent;
import com.capillary.ops.deployer.bo.TokenPaginatedResponse;
import com.capillary.ops.deployer.service.buildspecs.BuildSpec;
import com.capillary.ops.deployer.service.buildspecs.DotnetBuildSpec;
import com.capillary.ops.deployer.service.buildspecs.FreestyleDockerBuildSpec;
import com.capillary.ops.deployer.service.buildspecs.MavenBuildSpec;
import com.capillary.ops.deployer.service.interfaces.ICodeBuildService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.GetLogEventsRequest;
import software.amazon.awssdk.services.cloudwatchlogs.model.GetLogEventsResponse;
import software.amazon.awssdk.services.cloudwatchlogs.model.OutputLogEvent;
import software.amazon.awssdk.services.codebuild.CodeBuildClient;
import software.amazon.awssdk.services.codebuild.model.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Profile("!dev")
@Service
public class CodeBuildService implements ICodeBuildService {

    @Qualifier("codeBuildClient")
    @Autowired
    CodeBuildClient codeBuildClient;

    @Autowired
    Environment environment;

    @Autowired
    private CloudWatchLogsClient cloudWatchLogsClient;

    @Override
    public void createProject(Application application) {
        ProjectSource projectSource = ProjectSource.builder()
                .type(SourceType.valueOf(application.getVcsProvider().toString()))
                .location(application.getRepositoryUrl())
                .buildspec(createBuildSpec(application))
                .build();

        ProjectEnvironment projectEnvironment = ProjectEnvironment.builder()
                .type(EnvironmentType.LINUX_CONTAINER)
                .image(getBuildSpec(application).getBuildEnvironmentImage())
                .imagePullCredentialsType(ImagePullCredentialsType.CODEBUILD)
                .privilegedMode(true)
                .build();

        ProjectCache projectCache = ProjectCache.builder()
                .type(CacheType.S3)
                .location(environment.getProperty("codebuild.cacheBucket"))
                .build();

        String serviceRole = environment.getProperty("codebuild.serviceRole");
        String cloudWatchGroup = environment.getProperty("codebuild.cloudWatchGroup");
        LogsConfig logsConfig = LogsConfig.builder()
                .cloudWatchLogs(
                        CloudWatchLogsConfig.builder()
                                .status(LogsConfigStatusType.ENABLED)
                                .groupName(cloudWatchGroup)
                                .streamName(application.getId())
                                .build())
                .s3Logs(S3LogsConfig.builder().status(LogsConfigStatusType.DISABLED).build())
                .build();
        VpcConfig vpcConfig = VpcConfig.builder()
                .subnets(environment.getProperty("codebuild.subnet"))
                .securityGroupIds(environment.getProperty("codebuild.securityGroup"))
                .vpcId(environment.getProperty("codebuild.vpcId"))
                .build();
        CreateProjectRequest createProjectRequest =
                CreateProjectRequest.builder()
                        .name(application.getName())
                        .source(projectSource)
                        .environment(projectEnvironment)
                        .artifacts(ProjectArtifacts.builder().type(ArtifactsType.NO_ARTIFACTS).build())
                        .cache(projectCache)
                        .serviceRole(serviceRole)
                        .timeoutInMinutes(60)
                        .logsConfig(logsConfig)
                        .vpcConfig(vpcConfig)
                        .build();

        codeBuildClient.createProject(createProjectRequest);
    }

    private String createBuildSpec(Application application) {
        BuildSpec buildSpec = getBuildSpec(application);
        YAMLMapper yamlMapper = new YAMLMapper();
        yamlMapper.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true);
        yamlMapper.configure(YAMLGenerator.Feature.SPLIT_LINES, false);
        try {
            String buildSpecString = yamlMapper.writeValueAsString(buildSpec);
            return buildSpecString;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private BuildSpec getBuildSpec(Application application) {
        switch (application.getBuildType()) {
            case MVN:
                return new MavenBuildSpec(application);
            case FREESTYLE_DOCKER:
                return new FreestyleDockerBuildSpec(application);
            case DOTNET_CORE:
                return new DotnetBuildSpec(application);
            default:
                throw new NotImplementedException();
        }
    }

    @Override
    public String triggerBuild(Application application, Build build) {

        List<EnvironmentVariable> environmentVariables = new ArrayList<>();
        if(build.getEnvironmentVariables() != null) {
            build.getEnvironmentVariables().entrySet().stream().forEach(x -> {
                EnvironmentVariable environmentVariable =
                        EnvironmentVariable.builder()
                                .name(x.getKey())
                                .value(x.getValue())
                                .type(EnvironmentVariableType.PLAINTEXT)
                                .build();
                environmentVariables.add(environmentVariable);
            });
        }
        StartBuildRequest startBuildRequest =
                StartBuildRequest.builder()
                        .projectName(application.getName())
                        .sourceVersion(build.getTag())
                        .environmentVariablesOverride(environmentVariables)
                        .build();
        StartBuildResponse startBuildResponse = codeBuildClient.startBuild(startBuildRequest);
        return startBuildResponse.build().id();
    }

    @Override
    public TokenPaginatedResponse<LogEvent> getBuildLogs(String codeBuildId, String nextToken) {
        software.amazon.awssdk.services.codebuild.model.Build build = getBuild(codeBuildId);
        String groupName = build.logs().groupName();
        String streamName = build.logs().streamName();

        if (groupName == null || streamName == null) {
            return new TokenPaginatedResponse<>(new ArrayList<>(), "");
        }

        GetLogEventsRequest.Builder builder = GetLogEventsRequest.builder()
                .logGroupName(groupName)
                .logStreamName(streamName)
                .limit(100);
        if(nextToken == null || nextToken.isEmpty()) {
            builder.startFromHead(true);
        } else {
            builder.nextToken(nextToken);
        }
        GetLogEventsResponse cloudWatchResponse = cloudWatchLogsClient.getLogEvents(builder.build());
        List<OutputLogEvent> logEvents = cloudWatchResponse.events();
        List<LogEvent> logEventList = logEvents.stream()
                .map(x -> new LogEvent(x.timestamp(), x.message())).collect(Collectors.toList());
        return new TokenPaginatedResponse(logEventList, cloudWatchResponse.nextForwardToken());
    }

    @Override
    public software.amazon.awssdk.services.codebuild.model.Build getBuild(String codeBuildId) {
        BatchGetBuildsResponse batchGetBuildsResponse =
                codeBuildClient.batchGetBuilds(BatchGetBuildsRequest.builder().ids(codeBuildId).build());
        List<software.amazon.awssdk.services.codebuild.model.Build> builds = batchGetBuildsResponse.builds();
        if (builds.isEmpty()) return null;
        software.amazon.awssdk.services.codebuild.model.Build build = builds.get(0);
        return build;
    }
}
