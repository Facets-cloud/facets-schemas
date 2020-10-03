package com.capillary.ops.cp.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.capillary.ops.cp.bo.*;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.repository.DeploymentLogRepository;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.service.interfaces.ICodeBuildService;
import com.google.gson.Gson;
import com.jcabi.aspects.Loggable;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.FilterLogEventsRequest;
import software.amazon.awssdk.services.cloudwatchlogs.model.FilterLogEventsResponse;
import software.amazon.awssdk.services.codebuild.CodeBuildClient;
import software.amazon.awssdk.services.codebuild.model.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * CodeBuild service to trigger TF Builds.
 */
@Component
@Loggable
public class AwsCodeBuildService implements TFBuildService {

    public static final String LOG_GROUP_NAME = "codebuild-test";
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String CLUSTER_ID = "CLUSTER_ID";

    private static final Region BUILD_REGION = Region.US_WEST_1;
    public static final String BUILD_NAME = "capillary-cloud-tf-apply";
    public static final String HOST = "TF_VAR_cc_host";
    public static final String RELEASE_TYPE = "TF_VAR_release_type";
    public static final String CC_AUTH_TOKEN = "TF_VAR_cc_auth_token";
    public static final String STACK_SUBDIRECTORY = "STACK_SUBDIRECTORY";
    public static final String STACK_NAME = "STACK_NAME";
    private static final String CLUSTER_NAME = "CLUSTER_NAME";

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

    @Autowired
    private GitService gitService;
    /**
     * Deploy the latest build in the specified clusterId
     *
     * @param cluster     Cluster Information
     * @param deploymentRequest Additional params
     * @param deploymentContext
     * @return
     */
    @Override
    public DeploymentLog deployLatest(AbstractCluster cluster, DeploymentRequest deploymentRequest, DeploymentContext deploymentContext)
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
        environmentVariables.add(EnvironmentVariable.builder().name(CLUSTER_NAME).value(cluster.getName())
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

        String primarySourceVersion = "master";

        String masterHead = "";

        try {
            masterHead = gitService.getBranchHead(stack.getVcsUrl(), stack.getUser(), stack.getAppPassword(), "master");
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }

        ProjectSourceVersion secondarySourceVersion =
                ProjectSourceVersion.builder().sourceIdentifier("STACK").sourceVersion(masterHead).build();


        if (cluster.getCdPipelineParent() != null) {
            Boolean fetchedSourceVersions = false;
            List<DeploymentLog> pipelineParentDeployments =
                    deploymentLogRepository.findFirst50ByClusterIdOrderByCreatedOnDesc(cluster.getCdPipelineParent());
            for (DeploymentLog d: pipelineParentDeployments) {
                Build build = getBuild(d.getCodebuildId());
                if (StatusType.SUCCEEDED.equals(build.buildStatus())) {
                    primarySourceVersion = build.resolvedSourceVersion();
                    String stackSourceVersion = build.secondarySourceVersions().stream()
                            .filter(x -> "STACK".equalsIgnoreCase(x.sourceIdentifier()))
                            .findFirst().get().sourceVersion();
                    secondarySourceVersion =
                            ProjectSourceVersion.builder().sourceIdentifier("STACK")
                                    .sourceVersion(stackSourceVersion).build();
                    fetchedSourceVersions= true;
                    break;
                }
            }
            if(!fetchedSourceVersions) {
                throw new RuntimeException("No reference build found");
            }
        }

        StartBuildRequest startBuildRequest =
            StartBuildRequest.builder().projectName(buildName)
                    .environmentVariablesOverride(environmentVariables)
                    .secondarySourcesOverride(ProjectSource.builder()
                            .type(SourceType.valueOf(stack.getVcs().name()))
                            .location(stack.getVcsUrl())
                            .sourceIdentifier("STACK")
                            .build())
                    .secondarySourcesVersionOverride(secondarySourceVersion)
                    .sourceVersion(primarySourceVersion)
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
            log.setDeploymentContext(deploymentContext);
            return deploymentLogRepository.save(log);

        } catch (ResourceNotFoundException ex) {
            throw new RuntimeException("No Build defined for " + cluster.getCloud(), ex);
        }
    }

//    @Override
//    public StatusType getDeploymentStatus(String runId) {
//        return getBuild(runId).buildStatus();
//    }

    private Build getBuild(String runId) {
        return codeBuildService.getBuild(BUILD_REGION, runId);
    }

//    @Override
//    public Map<String, Build> getDeploymentStatuses(List<String> runIds) {
//        return codeBuildService.getBuilds(BUILD_REGION, runIds)
//                .stream().collect(Collectors.toMap(x->x.id(), x->x));
//    }
//
//    @Override
    private Map<String, Object> getDeploymentReport(String runId) {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.valueOf(artifactS3BucketRegion)).build();
        try {
            String reportKey = String.format("%s/capillary-cloud-tf-apply/capillary-cloud-tf/tfaws/report.json", runId.split(":")[1]);
            String report = IOUtils.toString(amazonS3.getObject(artifactS3Bucket, reportKey).getObjectContent(), StandardCharsets.UTF_8.name());
            return new Gson().fromJson(report, HashMap.class);
        } catch (Throwable e) {
            return new HashMap<>();
        }
    }

    @Override
    public DeploymentLog loadDeploymentStatus(DeploymentLog deploymentLog, boolean loadBuildDetails) {
        // status is present in db, return as is
        if(deploymentLog.getStatus() != null && deploymentLog.getStatus() != StatusType.IN_PROGRESS) {
            if(! loadBuildDetails) {
                // reduce payload
                deploymentLog.setChangesApplied(null);
            }
        }
        // if not
        else {
            CodeBuildClient codeBuildClient = getCodeBuildClient();
            BatchGetBuildsResponse batchGetBuildsResponse =
                    codeBuildClient.batchGetBuilds(BatchGetBuildsRequest.builder()
                            .ids(deploymentLog.getCodebuildId()).build());
            Build build = batchGetBuildsResponse.builds().get(0);
            deploymentLog.setStatus(build.buildStatus());

            if (loadBuildDetails) {
                String groupName = build.logs().groupName();
                String streamName = build.logs().streamName();
                List<TerraformChange> terraformChanges = getTerraformChanges(groupName, streamName);
                deploymentLog.setStatus(build.buildStatus());
                deploymentLog.setChangesApplied(terraformChanges);
                deploymentLogRepository.save(deploymentLog);
            }
        }

        return deploymentLog;
    }

    @Override
    public List<TerraformChange> getTerraformChanges(String codeBuildId) {
        CloudWatchLogsClient cloudWatchLogsClient = getCloudWatchLogsClient();
        FilterLogEventsResponse logEvents = cloudWatchLogsClient.filterLogEvents(FilterLogEventsRequest.builder()
                .limit(10000).logGroupName(LOG_GROUP_NAME)
                .logStreamNames(codeBuildId).filterPattern(" complete after ").build());
        return logEvents.events().stream()
                .filter(x -> !x.message().contains("module.overrides"))
                .map(x -> parseLogs(x.message()))
                .filter(x -> x != null).collect(Collectors.toList());
    }

    private TerraformChange parseLogs(String message) {
        try {
            Pattern p = Pattern.compile("^(.*): (.*) complete (.*)");
            Matcher matcher = p.matcher(message);
            if(matcher.find()) {
                String resourcePath = matcher.group(1);
                TerraformChange.TerraformChangeType changeType = TerraformChange.TerraformChangeType.valueOf(matcher.group(2));
                String resourceKey = null;
                Pattern p2 = Pattern.compile("^(.*)\\[\"(.*)\"\\]");
                Matcher m2 = p2.matcher(resourcePath);
                if(m2.find()) {
                    resourcePath = m2.group(1);
                    resourceKey = m2.group(2);
                }
                return new TerraformChange(resourcePath, resourceKey, changeType);
            } else {
                return null;
            }
        } catch (Throwable t) {
            return null;
        }
    }

    private CodeBuildClient getCodeBuildClient() {
        return CodeBuildClient.builder().region(BUILD_REGION).credentialsProvider(DefaultCredentialsProvider.create())
            .build();
    }

    private CloudWatchLogsClient getCloudWatchLogsClient() {
        return CloudWatchLogsClient.builder().region(BUILD_REGION).credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

}
