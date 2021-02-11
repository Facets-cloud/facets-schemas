package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.exceptions.NotImplementedException;
import com.capillary.ops.deployer.service.buildspecs.*;
import com.capillary.ops.deployer.service.interfaces.ICodeBuildService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.GetLogEventsRequest;
import software.amazon.awssdk.services.cloudwatchlogs.model.GetLogEventsResponse;
import software.amazon.awssdk.services.cloudwatchlogs.model.OutputLogEvent;
import software.amazon.awssdk.services.codebuild.CodeBuildClient;
import software.amazon.awssdk.services.codebuild.model.EnvironmentType;
import software.amazon.awssdk.services.codebuild.model.EnvironmentVariable;
import software.amazon.awssdk.services.codebuild.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Profile("!dev && !helminttest")
@Service
public class CodeBuildService implements ICodeBuildService {

    @Autowired
    Environment environment;

    @Autowired
    @Qualifier("codebuildExecutorService")
    private ExecutorService codebuildExecutor;

    @Autowired
    private RegistryService registryService;

    private static final Logger logger = LoggerFactory.getLogger(CodeBuildService.class);

    private static final int BUILD_TIMEOUT_MINUTES = 60;

    @Override
    public void createProject(Application application) {
        ProjectSpec projectSpec = getProjectSpec(application);
        BuildSpec buildSpec = projectSpec.getBuildSpec();

        CreateProjectRequest.Builder createProjectRequestBuilder = CreateProjectRequest.builder()
                .name(application.getName())
                .source(projectSpec.getProjectSource())
                .environment(projectSpec.getProjectEnvironment())
                .artifacts(projectSpec.getProjectArtifacts())
                .serviceRole(projectSpec.getServiceRole())
                .timeoutInMinutes(BUILD_TIMEOUT_MINUTES)
                .logsConfig(projectSpec.getLogsConfig());

        if(buildSpec.buildInVpc()) {
            createProjectRequestBuilder.vpcConfig(projectSpec.getVpcConfig());
        }

        if(buildSpec.useCache()) {
            createProjectRequestBuilder.cache(projectSpec.getProjectCache());
        }

        CreateProjectRequest createProjectRequest = createProjectRequestBuilder.build();
        getCodeBuildClient(buildSpec.getAwsRegion()).createProject(createProjectRequest);
    }

    @Override
    public void updateProject(Application application) {
        ProjectSpec projectSpec = getProjectSpec(application);
        BuildSpec buildSpec = projectSpec.getBuildSpec();

        UpdateProjectRequest.Builder updateProjectRequestBuilder = UpdateProjectRequest.builder()
                .name(application.getName())
                .source(projectSpec.getProjectSource())
                .environment(projectSpec.getProjectEnvironment())
                .artifacts(projectSpec.getProjectArtifacts())
                .serviceRole(projectSpec.getServiceRole())
                .timeoutInMinutes(BUILD_TIMEOUT_MINUTES)
                .logsConfig(projectSpec.getLogsConfig());

        if(buildSpec.buildInVpc()) {
            updateProjectRequestBuilder.vpcConfig(projectSpec.getVpcConfig());
        }

        if(buildSpec.useCache()) {
            updateProjectRequestBuilder.cache(projectSpec.getProjectCache());
        }

        UpdateProjectRequest updateProjectRequest = updateProjectRequestBuilder.build();
        getCodeBuildClient(buildSpec.getAwsRegion()).updateProject(updateProjectRequest);
    }

    private ProjectSpec getProjectSpec(Application application) {
        BuildSpec buildSpec = getBuildSpec(application);
        ProjectSource projectSource = ProjectSource.builder()
                .type(SourceType.valueOf(application.getVcsProvider().toString()))
                .location(application.getRepositoryUrl())
                .buildspec(createBuildSpec(application, false))
                .build();

        ProjectEnvironment projectEnvironment = ProjectEnvironment.builder()
                .type(buildSpec.getBuildEnvironmentType())
                .image(buildSpec.getBuildEnvironmentImage())
                .imagePullCredentialsType(ImagePullCredentialsType.CODEBUILD)
                .privilegedMode(buildSpec.getBuildEnvironmentType().equals(EnvironmentType.LINUX_CONTAINER))
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

        ProjectArtifacts projectArtifacts = ProjectArtifacts.builder()
                .type(ArtifactsType.S3)
                .namespaceType(ArtifactNamespace.BUILD_ID)
                .location("deployer-test-build-output")
                .packaging(ArtifactPackaging.ZIP)
                .build();

        return new ProjectSpec(buildSpec, projectSource, projectEnvironment, projectCache, serviceRole, logsConfig,
                vpcConfig, projectArtifacts);
    }

    private String createBuildSpec(Application application, boolean testSpec){
        return createBuildSpec(application, testSpec, false, false);
    }
    private String createBuildSpec(Application application, boolean testSpec, boolean tagBuild, boolean addAdditionalRegistries) {
        BuildSpec buildSpec = getBuildSpec(application, testSpec, tagBuild, addAdditionalRegistries);
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
        return getBuildSpec(application, false);
    }

    private BuildSpec getBuildSpec(Application application, boolean testBuild){
        return getBuildSpec(application, testBuild, false, false);
    }

    private BuildSpec getBuildSpec(Application application, boolean testBuild, boolean tagBuild, boolean addAdditionalRegistries) {
        List<Registry> registries = registryService.getBuildRegistries(application, testBuild, tagBuild, addAdditionalRegistries);

        switch (application.getBuildType()) {
            case MVN:
                return new MavenBuildSpec(application, testBuild, registries);
            case FREESTYLE_DOCKER:
                return new FreestyleDockerBuildSpec(application, testBuild, registries);
            case DOTNET_CORE:
                return new DotnetBuildSpec(application, testBuild, registries);
            case MVN_IONIC:
                return new MavenIonicBuildSpec(application, testBuild, registries);
            case JDK6_MAVEN2:
                return new JDK6Maven2BuildSpec(application, testBuild, registries);
            case MJ_NUGET:
                return new MJNugetBuildSpec(application, testBuild, registries);
            case DOTNET_CORE3:
                return new Dotnet3BuildSpec(application, testBuild, registries);
            case DOTNET_CORE22:
                return new Dotnet22BuildSpec(application, testBuild, registries);
            case SBT:
                return new SbtBuildSpec(application, testBuild, registries);
            case NPM:
                return new NPMBuildSpec(application, testBuild, registries);
            case NPM_UI:
                return new NPMUIBuildSpec(application, testBuild, registries);
            case JAVA8_LIBRARY:
                return new JavaLibararyMavenBuildSpec(application, testBuild, registries);
            default:
                throw new NotImplementedException("");
        }
    }

    @Override
    public String triggerBuild(Application application, Build build, boolean testBuild) {
        BuildSpec buildSpec = getBuildSpec(application, testBuild);
        List<EnvironmentVariable> environmentVariables = new ArrayList<>();
        if(build.getEnvironmentVariables() != null) {
            build.getEnvironmentVariables().entrySet().forEach(x -> {
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
                        .buildspecOverride(createBuildSpec(application, testBuild, build.isPromotable(), true))
                        .build();
        return getCodeBuildClient(buildSpec.getAwsRegion()).startBuild(startBuildRequest).build().id();
    }

    @Override
    public TokenPaginatedResponse<LogEvent> getBuildLogs(Application application, String codeBuildId, String nextToken) {
        BuildSpec buildSpec = getBuildSpec(application);
        software.amazon.awssdk.services.codebuild.model.Build build = getBuild(application, codeBuildId);
        String groupName = build.logs().groupName();
        String streamName = build.logs().streamName();

        if (groupName == null || streamName == null) {
            logger.info("group name or stream name was null, returning empty response");
            return new TokenPaginatedResponse<>(new ArrayList<>(), "");
        }

        GetLogEventsRequest.Builder builder = GetLogEventsRequest.builder()
                .logGroupName(groupName)
                .logStreamName(streamName)
                .limit(100);
        if(nextToken == null || nextToken.isEmpty()) {
            builder.startFromHead(false);
        } else {
            builder.startFromHead(false);
            builder.nextToken(nextToken);
        }

        GetLogEventsResponse cloudWatchResponse = getCloudWatchLogsClient(buildSpec.getAwsRegion()).getLogEvents(builder.build());
        List<OutputLogEvent> logEvents = cloudWatchResponse.events();
        List<LogEvent> logEventList = logEvents.stream()
                .map(x -> new LogEvent(x.timestamp(), x.message()))
                .collect(Collectors.toList());
        return new TokenPaginatedResponse(Lists.reverse(logEventList), cloudWatchResponse.nextBackwardToken());
    }

    @Override
    public software.amazon.awssdk.services.codebuild.model.Build getBuild(Application application, String codeBuildId) {
        BuildSpec buildSpec = getBuildSpec(application);
        return getBuild(buildSpec.getAwsRegion(), codeBuildId);
    }

    @Override
    public software.amazon.awssdk.services.codebuild.model.Build getBuild(Region region, String codeBuildId) {
        Future<BatchGetBuildsResponse> future = codebuildExecutor.submit(() ->
                getCodeBuildClient(region).batchGetBuilds(BatchGetBuildsRequest.builder()
                        .ids(codeBuildId)
                        .build()));

        BatchGetBuildsResponse batchGetBuildsResponse = null;
        try {
            batchGetBuildsResponse = future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("error happened while getting codebuild details", e);
            logger.info("retrying once");
            try {
                Thread.sleep(500);
                return getBuild(region, codeBuildId);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

        List<software.amazon.awssdk.services.codebuild.model.Build> builds = batchGetBuildsResponse.builds();
        if (builds.isEmpty()) return null;
        return builds.get(0);
    }

    @Override
    public List<software.amazon.awssdk.services.codebuild.model.Build> getBuilds(Application application, List<String> codeBuildIds) {
        BuildSpec buildSpec = getBuildSpec(application);
        return getBuilds(buildSpec.getAwsRegion(), codeBuildIds);
    }

    @Override
    public List<software.amazon.awssdk.services.codebuild.model.Build> getBuilds(Region region, List<String> codeBuildIds) {
        List<List<String>> partition = Lists.partition(codeBuildIds, 90);
        logger.info("total size of builds: {}, total partitions created: {}", codeBuildIds.size(), partition.size());
        List<software.amazon.awssdk.services.codebuild.model.Build> batchedBuilds = Lists.newArrayListWithExpectedSize(codeBuildIds.size());
        partition.forEach(x -> {
            logger.info("getting builds for {} codebuild ids", x.size());

            BatchGetBuildsResponse batchGetBuildsResponse = null;
            try {
                batchGetBuildsResponse = codebuildExecutor.submit(() -> getCodeBuildClient(region).batchGetBuilds(BatchGetBuildsRequest.builder()
                        .ids(x)
                        .build())).get();
            } catch (Throwable e) {
                logger.error("error happened while getting codebuild details", e);
                throw new RuntimeException(e);
            }
            batchedBuilds.addAll(batchGetBuildsResponse.builds());
        });

        return batchedBuilds;
    }

    @Override
    public void deleteProject(Application application) {
        getCodeBuildClient(getBuildSpec(application).getAwsRegion())
                .deleteProject(DeleteProjectRequest.builder().name(application.getName()).build());
    }

    private CodeBuildClient getCodeBuildClient(Region region) {
        return CodeBuildClient.builder()
                        .region(region)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build();
    }

    private CloudWatchLogsClient getCloudWatchLogsClient(Region region) {
        return CloudWatchLogsClient.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
