package com.capillary.ops;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.GetLogEventsRequest;
import software.amazon.awssdk.services.cloudwatchlogs.model.GetLogEventsResponse;
import software.amazon.awssdk.services.codebuild.CodeBuildClient;
import software.amazon.awssdk.services.codebuild.model.*;

public class AWSCodebuildTest {
  public static void main(String[] args) throws Exception {
    FileInputStream configInputStream =
        new FileInputStream("/etc/capillary/codebuildcredentials.ini");
    Properties properties = new Properties();
    properties.load(configInputStream);
    String access = properties.getProperty("access");
    String secret = properties.getProperty("secret");
    AwsBasicCredentials credentials = AwsBasicCredentials.create(access, secret);
    CodeBuildClient codeBuildClient =
        CodeBuildClient.builder()
            .region(Region.of(properties.getProperty("region")))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build();

    CloudWatchLogsClient cloudWatchClient = CloudWatchLogsClient.builder()
            .region(Region.of(properties.getProperty("region")))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build();
    /*CodeBuildClient codeBuildClient = CodeBuildClient.builder()
    .region(Region.of(properties.getProperty("region")))
    .credentialsProvider(ProfileCredentialsProvider.builder().profileName("freemium").build())
    .build();*/

    // String buildId = build(codeBuildClient);
    // test(codeBuildClient);
    // createProject(codeBuildClient);
    // System.out.println(getBuildSpec());
    // intouchapi-codebuild:5351bf48-688e-41b6-938e-f2fc09f0c22b
    //buildStatusLoop(codeBuildClient, "intouchapi-codebuild:5351bf48-688e-41b6-938e-f2fc09f0c22b");
    getCloudWatchLogs(cloudWatchClient, "intouchapi/844b9fdc-58e4-4c4e-ad2d-4b4858090a53");

    codeBuildClient.close();
  }

  public static String build(CodeBuildClient codeBuildClient) throws Exception {

    String buildSpec = getBuildSpec();

    StartBuildRequest startBuildRequest =
        StartBuildRequest.builder()
            .projectName("intouchapi-codebuild")
            .sourceVersion("k8s-codebuild")
            .buildspecOverride(buildSpec)
            .environmentTypeOverride(EnvironmentType.LINUX_CONTAINER)
            .imageOverride("486456986266.dkr.ecr.us-east-1.amazonaws.com/mavendocker:v1.4")
            .privilegedModeOverride(true)
            .build();
    StartBuildResponse startBuildResponse = codeBuildClient.startBuild(startBuildRequest);
    return startBuildResponse.build().id();
  }

  public static void createProject(CodeBuildClient codeBuildClient) {
    CreateProjectRequest createProjectRequest =
        CreateProjectRequest.builder()
            .name("sdk-test")
            .source(
                ProjectSource.builder()
                    .type(SourceType.GITHUB)
                    .location("https://github.com/Capillary/api")
                    .build())
            .environment(
                ProjectEnvironment.builder()
                    .type(EnvironmentType.LINUX_CONTAINER)
                    .image("486456986266.dkr.ecr.us-east-1.amazonaws.com/mavendocker:v1.4")
                    .imagePullCredentialsType(ImagePullCredentialsType.CODEBUILD)
                    .privilegedMode(true)
                    .build())
            .artifacts(ProjectArtifacts.builder().type(ArtifactsType.NO_ARTIFACTS).build())
            .cache(ProjectCache.builder().type(CacheType.S3).location("capbuildcache").build())
            .serviceRole("cap-codebuild-role")
            .timeoutInMinutes(180)
            .logsConfig(
                LogsConfig.builder()
                    .cloudWatchLogs(
                        CloudWatchLogsConfig.builder()
                            .status(LogsConfigStatusType.ENABLED)
                            .groupName("codebuild-test")
                            .streamName("intouchapi")
                            .build())
                    .s3Logs(S3LogsConfig.builder().status(LogsConfigStatusType.DISABLED).build())
                    .build())
            .build();

    codeBuildClient.createProject(createProjectRequest);
  }

  public static void test(CodeBuildClient codeBuildClient) throws Exception {
    ListCuratedEnvironmentImagesRequest.builder();
    codeBuildClient
        .listCuratedEnvironmentImages(ListCuratedEnvironmentImagesRequest.builder().build())
        .platforms()
        .stream()
        .forEach(System.out::println);
  }

  public static String getBuildSpec() throws Exception {
    InputStream buildSpecStream =
        AWSCodebuildTest.class.getClassLoader().getResourceAsStream("buildSpec.json");
    JsonNode jsonNode = new ObjectMapper().readTree(buildSpecStream);
    YAMLMapper yamlMapper = new YAMLMapper();
    yamlMapper.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true);
    yamlMapper.configure(YAMLGenerator.Feature.SPLIT_LINES, false);
    return yamlMapper.writeValueAsString(jsonNode);
  }

  public static StatusType getBuildStatus(CodeBuildClient codeBuildClient, String buildId) {
    BatchGetBuildsResponse batchGetBuildsResponse =
        codeBuildClient.batchGetBuilds(BatchGetBuildsRequest.builder().ids(buildId).build());
    Build build = batchGetBuildsResponse.builds().get(0);
    return build.buildStatus();
  }

  public static void buildStatusLoop(CodeBuildClient codeBuildClient, String buildId) {
    while (true) {
      StatusType status = getBuildStatus(codeBuildClient, buildId);
      System.out.println("Status is " + status);
      if (status != StatusType.IN_PROGRESS) break;
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        System.out.println("Interrupted");
      }
    }
  }

  public static void getCloudWatchLogs(CloudWatchLogsClient cloudWatchLogsClient, String buildId) {
      GetLogEventsResponse logEvents = cloudWatchLogsClient.getLogEvents(GetLogEventsRequest.builder()
              .logGroupName("codebuild-test")
              .logStreamName(buildId)
              .startFromHead(true)
              .build());
      String nextForwardToken = logEvents.nextForwardToken();
      logEvents.events().stream().forEach(outputLogEvent ->  System.out.println(outputLogEvent.message()));

      /*logEvents = cloudWatchLogsClient.getLogEvents(GetLogEventsRequest.builder()
              .logGroupName("codebuild-test")
              .logStreamName(buildId)
              .nextToken(nextForwardToken)
              .build());
      System.out.println("**********************"+nextForwardToken);
      logEvents.events().stream().forEach(outputLogEvent ->  System.out.println(outputLogEvent.message()));*/
  }
}

class Temp {
  String version = "0.2";
  List<String> list = new ArrayList<>();
  String path = "\"/root/.m2/**/*\"";

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public List<String> getList() {
    return list;
  }

  public void setList(List<String> list) {
    this.list = list;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
