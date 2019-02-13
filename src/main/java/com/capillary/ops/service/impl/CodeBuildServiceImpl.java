package com.capillary.ops.service.impl;

import com.capillary.ops.bo.codebuild.CodeBuildApplication;
import com.capillary.ops.bo.codebuild.CodeBuildDetails;
import com.capillary.ops.constants.CodeBuildConstants;
import com.capillary.ops.repository.CodeBuildApplicationRepository;
import com.capillary.ops.service.CodeBuildService;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.GetLogEventsRequest;
import software.amazon.awssdk.services.cloudwatchlogs.model.GetLogEventsResponse;
import software.amazon.awssdk.services.codebuild.CodeBuildClient;
import software.amazon.awssdk.services.codebuild.model.*;

@Service
public abstract class CodeBuildServiceImpl implements CodeBuildService {

  @Qualifier("codeBuildClient")
  @Autowired
  CodeBuildClient codeBuildClient;

  @Qualifier("cloudWatchLogsClient")
  @Autowired
  CloudWatchLogsClient cloudWatchLogsClient;

  @Autowired CodeBuildApplicationRepository repository;

  @Autowired Environment environment;

  @Override
  public CodeBuildApplication createApplication(CodeBuildApplication application) {

      /*CodeBuildApplication resApplication = repository.findByName(application.getName());
      if (resApplication == null) {
          repository.save(application);
      } else {
          return null;
      }*/

      CreateProjectRequest createProjectRequest =
        CreateProjectRequest.builder()
            .name(application.getId())
            .source(
                ProjectSource.builder()
                    .type(SourceType.valueOf(application.getSourceType().toString()))
                    .location(application.getRepoURL())
                    .build())
            .environment(
                ProjectEnvironment.builder()
                    .type(EnvironmentType.LINUX_CONTAINER)
                    .image(environment.getProperty(this.getContainerImageConfig()))
                    .imagePullCredentialsType(ImagePullCredentialsType.CODEBUILD)
                    .privilegedMode(true)
                    .build())
            .artifacts(ProjectArtifacts.builder().type(ArtifactsType.NO_ARTIFACTS).build())
            .cache(
                ProjectCache.builder()
                    .type(CacheType.S3)
                    .location(CodeBuildConstants.CACHE_BUCKET)
                    .build())
            .serviceRole(CodeBuildConstants.SERVICE_ROLE)
            .timeoutInMinutes(CodeBuildConstants.TIMEOUT)
            .logsConfig(
                LogsConfig.builder()
                    .cloudWatchLogs(
                        CloudWatchLogsConfig.builder()
                            .status(LogsConfigStatusType.ENABLED)
                            .groupName(CodeBuildConstants.CLOUDWATCH_GROUP)
                            .streamName(application.getId())
                            .build())
                    .s3Logs(S3LogsConfig.builder().status(LogsConfigStatusType.DISABLED).build())
                    .build())
            .build();

    codeBuildClient.createProject(createProjectRequest);
    return application.fromCreateProject(createProjectRequest);
  }

  @Override
  public CodeBuildApplication getApplication(String applicationId) {
    CodeBuildApplication projectMetadata = repository.findById(applicationId).get();
      BatchGetProjectsResponse batchGetProjectsResponse = codeBuildClient.batchGetProjects(BatchGetProjectsRequest.builder()
              .names(applicationId)
              .build());
      List<Project> projects = batchGetProjectsResponse.projects();
      if (projects != null && projects.isEmpty()) {
          return null;
      }
      return projectMetadata.fromGetProject(projects.get(0));
  }

  @Override
  public CodeBuildDetails createBuild(String applicationId, CodeBuildDetails details) {
    CodeBuildApplication application = repository.findById(applicationId).get();
    String buildSpec = this.createBuildSpec(application);
    StartBuildRequest startBuildRequest =
        StartBuildRequest.builder()
            .projectName(applicationId)
            .sourceVersion(details.getBranch())
            .buildspecOverride(buildSpec)
            .build();
    StartBuildResponse startBuildResponse = codeBuildClient.startBuild(startBuildRequest);
    return details.fromStartBuild(startBuildResponse);
  }

  @Override
  public CodeBuildDetails getBuildDetails(String buildId) {
    BatchGetBuildsResponse batchGetBuildsResponse =
        codeBuildClient.batchGetBuilds(BatchGetBuildsRequest.builder().ids(buildId).build());
    List<Build> builds = batchGetBuildsResponse.builds();
    if (builds.isEmpty())
      return null;
    Build build = builds.get(0);
    String streamName = build.id().replace(':', '/');
    GetLogEventsResponse logEvents = cloudWatchLogsClient.getLogEvents(GetLogEventsRequest.builder()
            .logGroupName("codebuild-test")
            .logStreamName(streamName)
            .startFromHead(true)
            .build());
    String nextForwardToken = logEvents.nextForwardToken();
    StringBuffer logBuffer = new StringBuffer(logEvents.events().stream().map(outputLogEvent -> outputLogEvent.message()).collect(Collectors.joining("\n")));
    while(!logEvents.events().isEmpty()) {
      logEvents = cloudWatchLogsClient.getLogEvents(GetLogEventsRequest.builder()
              .logGroupName("codebuild-test")
              .logStreamName(streamName)
              .nextToken(nextForwardToken)
              .build());
      nextForwardToken = logEvents.nextForwardToken();
      logBuffer.append(logEvents.events().stream().map(outputLogEvent -> outputLogEvent.message()).collect(Collectors.joining("\n")));
      //logEvents.events().stream().forEach(outputLogEvent ->  System.out.println(outputLogEvent.message()));
    }
    CodeBuildDetails response = new CodeBuildDetails().fromGetBuild(builds.get(0));
    response.setBuildLogs(logBuffer.toString());
    return response;
  }

  @Override
  public CodeBuildDetails stopBuild(String buildId) {
    StopBuildResponse stopBuildResponse =
        codeBuildClient.stopBuild(StopBuildRequest.builder().id(buildId).build());
    Build build = stopBuildResponse.build();
    return build == null ? null : new CodeBuildDetails().fromStopBuild(build);
  }

  protected abstract String createBuildSpec(CodeBuildApplication application);

}
