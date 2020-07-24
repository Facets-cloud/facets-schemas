package com.capillary.ops.cp.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.repository.DeploymentLogRepository;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.service.CodeBuildService;
import com.capillary.ops.deployer.service.interfaces.ICodeBuildService;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.jcabi.aspects.Loggable;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.codebuild.CodeBuildClient;
import software.amazon.awssdk.services.codebuild.model.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CodeBuild service to trigger TF Builds.
 */
@Component
@Loggable
public class AwsCodeBuildService implements TFBuildService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String CLUSTER_ID = "CLUSTER_ID";

    private static final Region BUILD_REGION = Region.US_WEST_1;
    public static final String BUILD_NAME = "capillary-cloud-tf-apply";
    public static final String HOST = "TF_VAR_cc_host";
    public static final String RELEASE_TYPE = "TF_VAR_release_type";
    public static final String CC_AUTH_TOKEN = "TF_VAR_cc_auth_token";
    public static final String STACK_SUBDIRECTORY = "STACK_SUBDIRECTORY";
    public static final String STACK_NAME = "STACK_NAME";

    @Value("${internalApiAuthToken}")
    private String authToken;

    @Value("${deployer.scheduler.host}")
    private String hostName;

    @Autowired
    private HttpServletRequest requestContext;

    @Autowired
    private StackRepository stackRepository;

    @Autowired
    private ICodeBuildService codeBuildService;

    @Value("${aws.s3bucket.testOutputBucket.name}")
    private String artifactS3Bucket;

    @Value("${aws.s3bucket.testOutputBucket.region}")
    private String artifactS3BucketRegion;

    @Autowired
    private DeploymentLogRepository deploymentLogRepository;
    /**
     * Deploy the latest build in the specified clusterId
     *
     * @param cluster     Cluster Information
     * @param deploymentRequest Additional params
     * @return
     */
    @Override
    public DeploymentLog deployLatest(AbstractCluster cluster, DeploymentRequest deploymentRequest)
    {
        ReleaseType releaseType = deploymentRequest.getReleaseType();
        List<EnvironmentVariable> extraEnv = deploymentRequest.getExtraEnv();
        Optional<Stack> stackO = stackRepository.findById(cluster.getStackName());
        if (!stackO.isPresent()) {
            throw new NotFoundException("The Stack for this cluster does not exist NOW");
        }
        Stack stack = stackO.get();
        //DONE: Check if code build is defined for the said cloud
        List<EnvironmentVariable> environmentVariables = new ArrayList<>();
        environmentVariables.add(EnvironmentVariable.builder().name(CLUSTER_ID).value(cluster.getId())
            .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(
            EnvironmentVariable.builder().name(CC_AUTH_TOKEN).value(authToken).type(EnvironmentVariableType.PLAINTEXT)
                .build());
        environmentVariables.add(EnvironmentVariable.builder().name(STACK_NAME).value(cluster.getStackName())
            .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(EnvironmentVariable.builder().name(STACK_SUBDIRECTORY)
                .value(stack.getRelativePath()).type(EnvironmentVariableType.PLAINTEXT)
                .build());
        try {
            environmentVariables.add(EnvironmentVariable.builder().name(HOST).value(requestContext.getHeader("HOST"))
                .type(EnvironmentVariableType.PLAINTEXT).build());
        } catch (Throwable t) {
            logger.error("Not in Request context", t);
            environmentVariables.add(
                EnvironmentVariable.builder().name(HOST).value(hostName).type(EnvironmentVariableType.PLAINTEXT)
                    .build());

        }
        environmentVariables.add(EnvironmentVariable.builder().name(RELEASE_TYPE).value(releaseType.name())
            .type(EnvironmentVariableType.PLAINTEXT).build());

        if(!extraEnv.isEmpty()){
            environmentVariables.addAll(extraEnv);
        }

        String buildName = "";
        switch (cluster.getCloud()) {

            case AWS:
                buildName = BUILD_NAME;
                break;
        }
        StartBuildRequest startBuildRequest =
            StartBuildRequest.builder().projectName(buildName)
                    .environmentVariablesOverride(environmentVariables)
                    .secondarySourcesOverride(ProjectSource.builder()
                            .type(SourceType.valueOf(stack.getVcs().name()))
                            .location(stack.getVcsUrl())
                            .sourceIdentifier("STACK")
                            .build())
                    .build();

        ListBuildsForProjectRequest listBuildsForProjectRequest =
            ListBuildsForProjectRequest.builder().projectName(buildName).sortOrder(SortOrderType.DESCENDING).build();

        ListBuildsForProjectResponse listBuildsForProjectResponse =
            getCodeBuildClient().listBuildsForProject(listBuildsForProjectRequest);
        List<String> buildIds = listBuildsForProjectResponse.ids();
        List<String> shortListedBuilds = buildIds.stream().limit(10).collect(Collectors.toList());

        BatchGetBuildsRequest batchGetBuildsRequest = BatchGetBuildsRequest.builder().ids(shortListedBuilds).build();
        BatchGetBuildsResponse batchGetBuildsResponse = getCodeBuildClient().batchGetBuilds(batchGetBuildsRequest);
        List<Build> builds = batchGetBuildsResponse.builds();
        List<Build> runningBuilds = builds.stream().filter(b -> b.buildStatus().equals(StatusType.IN_PROGRESS)).filter(
            b -> b.environment().environmentVariables().contains(
                EnvironmentVariable.builder().name(CLUSTER_ID).value(cluster.getId())
                    .type(EnvironmentVariableType.PLAINTEXT).build())).collect(Collectors.toList());

        if (runningBuilds.size() > 0) {
            throw new IllegalStateException("Build is already in Progress: " + runningBuilds.get(0).id());
        }

        try {
            String buildId = getCodeBuildClient().startBuild(startBuildRequest).build().id();
            DeploymentLog log = new DeploymentLog();
            log.setCodebuildId(buildId);
            log.setClusterId(cluster.getId());
            log.setDescription(deploymentRequest.getTag());
            log.setReleaseType(deploymentRequest.getReleaseType());
            log.setCreatedOn(new Date());
            return deploymentLogRepository.save(log);

        } catch (ResourceNotFoundException ex) {
            throw new RuntimeException("No Build defined for " + cluster.getCloud(), ex);
        }
    }

    @Override
    public StatusType getDeploymentStatus(String runId) {
        return codeBuildService.getBuild(BUILD_REGION, runId).buildStatus();
    }

    @Override
    public Map<String, StatusType> getDeploymentStatuses(List<String> runIds) {
        return codeBuildService.getBuilds(BUILD_REGION, runIds)
                .stream().collect(Collectors.toMap(x->x.id(), x->x.buildStatus()));
    }

    @Override
    public Map<String, Object> getDeploymentReport(String runId) {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.valueOf(artifactS3BucketRegion)).build();
        try {
            String reportKey = String.format("%s/capillary-cloud-tf-apply/capillary-cloud-tf/tfaws/report.json", runId.split(":")[1]);
            String report = IOUtils.toString(amazonS3.getObject(artifactS3Bucket, reportKey).getObjectContent(), StandardCharsets.UTF_8.name());
            return new Gson().fromJson(report, HashMap.class);
        } catch (Throwable e) {
            return new HashMap<>();
        }
    }

    private CodeBuildClient getCodeBuildClient() {
        return CodeBuildClient.builder().region(BUILD_REGION).credentialsProvider(DefaultCredentialsProvider.create())
            .build();
    }

}
