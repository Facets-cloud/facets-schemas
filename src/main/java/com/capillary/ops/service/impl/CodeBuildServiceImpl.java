package com.capillary.ops.service.impl;

import com.capillary.ops.bo.codebuild.CodeBuildApplication;
import com.capillary.ops.bo.codebuild.CodeBuildDetails;
import com.capillary.ops.constants.CodeBuildConstants;
import com.capillary.ops.service.CodeBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import software.amazon.awssdk.services.codebuild.CodeBuildClient;
import software.amazon.awssdk.services.codebuild.model.*;

import java.util.List;

public class CodeBuildServiceImpl implements CodeBuildService {

    @Qualifier("codeBuildClient")
    @Autowired
    CodeBuildClient codeBuildClient;

    @Override
    public CodeBuildApplication createApplication(CodeBuildApplication application) {
        CreateProjectRequest createProjectRequest =
                CreateProjectRequest.builder()
                        .name(application.getName())
                        .source(ProjectSource.builder()
                                .type(SourceType.valueOf(application.getSourceType().toString()))
                                .location(application.getRepoURL())
                                .build())
                        .environment(ProjectEnvironment.builder()
                                .type(EnvironmentType.LINUX_CONTAINER)
                                .image(CodeBuildConstants.IMAGE_PATH)
                                .imagePullCredentialsType(ImagePullCredentialsType.CODEBUILD)
                                .privilegedMode(true)
                                .build())
                        .artifacts(ProjectArtifacts.builder()
                                .type(ArtifactsType.NO_ARTIFACTS)
                                .build())
                        .cache(ProjectCache.builder()
                                .type(CacheType.S3)
                                .location(CodeBuildConstants.CACHE_BUCKET)
                                .build())
                        .serviceRole(CodeBuildConstants.SERVICE_ROLE)
                        .timeoutInMinutes(CodeBuildConstants.TIMEOUT)
                        .logsConfig(LogsConfig.builder()
                                .cloudWatchLogs(CloudWatchLogsConfig.builder()
                                        .status(LogsConfigStatusType.ENABLED)
                                        .groupName(CodeBuildConstants.CLOUDWATCH_GROUP)
                                        .streamName(application.getName())
                                        .build())
                                .s3Logs(S3LogsConfig.builder()
                                        .status(LogsConfigStatusType.DISABLED)
                                        .build())
                                .build())
                        .build();

        codeBuildClient.createProject(createProjectRequest);
        return application.fromCreateProject(createProjectRequest);
    }

    @Override
    public CodeBuildDetails createBuild(String applicationId, CodeBuildDetails details) {
        String buildSpec = this.createBuildSpec();
        StartBuildRequest startBuildRequest = StartBuildRequest.builder()
                .projectName(applicationId)
                .sourceVersion(details.getBranch())
                .buildspecOverride(buildSpec)
                .build();
        StartBuildResponse startBuildResponse = codeBuildClient.startBuild(startBuildRequest);
        return details.fromStartBuild(startBuildResponse);
    }

    @Override
    public CodeBuildDetails getBuildDetails(String buildId) {
        BatchGetBuildsResponse batchGetBuildsResponse = codeBuildClient.batchGetBuilds(BatchGetBuildsRequest.builder()
                .ids(buildId)
                .build());
        List<Build> builds = batchGetBuildsResponse.builds();
        return builds.isEmpty() ? null : new CodeBuildDetails().fromGetBuild(builds.get(0));
    }

    @Override
    public CodeBuildDetails stopBuild(String buildId) {
        StopBuildResponse stopBuildResponse = codeBuildClient.stopBuild(StopBuildRequest.builder()
                .id(buildId)
                .build());
        Build build = stopBuildResponse.build();
        return build == null ? null : new CodeBuildDetails().fromStopBuild(build);
    }

    private String createBuildSpec() {
        return null;
    }
}
