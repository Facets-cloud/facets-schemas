package com.capillary.ops.service.impl;

import com.capillary.ops.bo.codebuild.BuildSpec;
import com.capillary.ops.bo.codebuild.CodeBuildApplication;
import com.capillary.ops.bo.codebuild.CodeBuildDetails;
import com.capillary.ops.constants.CodeBuildConstants;
import com.capillary.ops.repository.CodeBuildApplicationRepository;
import com.capillary.ops.service.CodeBuildService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.codebuild.CodeBuildClient;
import software.amazon.awssdk.services.codebuild.model.*;

@Service
public class CodeBuildServiceImpl implements CodeBuildService {

  @Qualifier("codeBuildClient")
  @Autowired
  CodeBuildClient codeBuildClient;

  @Autowired CodeBuildApplicationRepository repository;

  @Autowired Environment environment;

  @Override
  public CodeBuildApplication createApplication(CodeBuildApplication application) {

      CodeBuildApplication resApplication = repository.findByName(application.getName());
      if (resApplication == null) {
          repository.save(application);
      } else {
          return null;
      }

      CreateProjectRequest createProjectRequest =
        CreateProjectRequest.builder()
            .name(application.getName())
            .source(
                ProjectSource.builder()
                    .type(SourceType.valueOf(application.getSourceType().toString()))
                    .location(application.getRepoURL())
                    .build())
            .environment(
                ProjectEnvironment.builder()
                    .type(EnvironmentType.LINUX_CONTAINER)
                    .image(environment.getProperty("codebuild.mavenimage.path"))
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
                            .streamName(application.getName())
                            .build())
                    .s3Logs(S3LogsConfig.builder().status(LogsConfigStatusType.DISABLED).build())
                    .build())
            .build();

    codeBuildClient.createProject(createProjectRequest);
    return application.fromCreateProject(createProjectRequest);
  }

  @Override
  public CodeBuildApplication getApplication(String applicationId) {
      BatchGetProjectsResponse batchGetProjectsResponse = codeBuildClient.batchGetProjects(BatchGetProjectsRequest.builder()
              .names(applicationId)
              .build());
      List<Project> projects = batchGetProjectsResponse.projects();
      if (projects != null && projects.isEmpty()) {
          return null;
      }
      CodeBuildApplication projectMetadata = repository.findByName(applicationId);
      return projectMetadata.fromGetProject(projects.get(0));
  }

  @Override
  public CodeBuildDetails createBuild(String applicationId, CodeBuildDetails details) {
    CodeBuildApplication application = repository.findByName(applicationId);
    if (application == null) {
      return null;
    }
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
    return builds.isEmpty() ? null : new CodeBuildDetails().fromGetBuild(builds.get(0));
  }

  @Override
  public CodeBuildDetails stopBuild(String buildId) {
    StopBuildResponse stopBuildResponse =
        codeBuildClient.stopBuild(StopBuildRequest.builder().id(buildId).build());
    Build build = stopBuildResponse.build();
    return build == null ? null : new CodeBuildDetails().fromStopBuild(build);
  }

  private String createBuildSpec(CodeBuildApplication application) {
    BuildSpec buildSpec = new BuildSpec();
    String buildSpecString = null;
    buildSpec.setVersion("0.2");

    List<String> installCommands = new ArrayList<>();
    Map<String, Object> installPhase = new HashMap<>();
    installCommands.add(
        "nohup dockerd --host=unix:///var/run/docker.sock --host=tcp://0.0.0.0:2375 --storage-driver=overlay&");
    installCommands.add("timeout 15 sh -c \"until docker info; do echo .; sleep 1; done\"");
    installPhase.put("commands", installCommands);

    List<String> preBuildCommands = new ArrayList<>();
    Map<String, Object> preBuildPhase = new HashMap<>();
    preBuildPhase.put("commands", preBuildCommands);

    List<String> buildCommands = new ArrayList<>();
    Map<String, Object> buildPhase = new HashMap<>();
    buildCommands.add("$(aws ecr get-login --region us-east-1 --no-include-email)");
    buildCommands.add(String.format("cd %s", application.getProjectFolder()));
    if(application.getApplicationType() == CodeBuildApplication.ApplicationType.MAVEN_JAVA) {
        buildCommands.add("mvn clean package -Dmaven.test.failure.ignore=false -DskipFormat=true -Dmaven.test.skip=true -U -Pdocker-cibuild");
    }
    buildPhase.put("commands", buildCommands);

    List<String> postBuildCommands = new ArrayList<>();
    Map<String, Object> postBuildPhase = new HashMap<>();
    postBuildPhase.put("commands", postBuildCommands);

    Map<String, Object> phases = new HashMap<>();
    phases.put("install", installPhase);
    phases.put("pre_build", preBuildPhase);
    phases.put("build", buildPhase);
    phases.put("post_build", postBuildPhase);

    Map<String, Object> cache = new HashMap<>();
    List<String> cachePaths = new ArrayList<>();
    if(application.getApplicationType() == CodeBuildApplication.ApplicationType.MAVEN_JAVA) {
        cachePaths.add("/root/.m2/**/*");
    }
    cache.put("paths", cachePaths);

    buildSpec.setPhases(phases);
    buildSpec.setCache(cache);

    // JsonTree objectMapper = new ObjectMapper().rea;
    YAMLMapper yamlMapper = new YAMLMapper();
    yamlMapper.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true);
    yamlMapper.configure(YAMLGenerator.Feature.SPLIT_LINES, false);
    try {
      buildSpecString = yamlMapper.writeValueAsString(buildSpec);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return buildSpecString;
  }
}
