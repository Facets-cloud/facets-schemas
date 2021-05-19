package com.capillary.ops.cp.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.capillary.ops.cp.bo.*;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.requests.Cloud;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.repository.DeploymentLogRepository;
import com.capillary.ops.cp.repository.QASuiteResultRepository;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.deployer.bo.LogEvent;
import com.capillary.ops.deployer.bo.TokenPaginatedResponse;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.service.interfaces.ICodeBuildService;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcabi.aspects.Loggable;
import de.flapdoodle.embed.process.io.file.Files;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;
import software.amazon.awssdk.services.codebuild.CodeBuildClient;
import software.amazon.awssdk.services.codebuild.model.*;
import software.amazon.awssdk.services.codebuild.model.ResourceNotFoundException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * CodeBuild service to trigger TF Builds.
 */
@Component
@Loggable
@Profile("!dev")
public class AwsCodeBuildService implements TFBuildService {

    public static final String LOG_GROUP_NAME = "codebuild-test";

    @Value("${cc_deployment_bucket}")
    public String CC_STACK_SOURCE;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String CLUSTER_ID = "CLUSTER_ID";
    private static final String CLUSTER_REGION = "CLUSTER_REGION";

    @Value("${cc_aws_region}")
    public Region BUILD_REGION;
    @Value("${cc_codebuild_name}")
    public String BUILD_NAME;
    @Value("${cc_vpc_id}")
    public String CC_VPC_ID;
    @Value("${cc_route_table_id}")
    public String CC_ROUTE_TABLE_ID;
    @Value("${cc_vpc_cidr}")
    public String CC_VPC_CIDR;
    @Value("${cc_tf_state_bucket}")
    public String CC_TF_STATE_BUCKET;
    @Value("${cc_tf_dynamo_table}")
    private String CC_TF_DYNAMO_TABLE;
    @Value("${cc_tf_state_region}")
    private String CC_TF_STATE_REGION;

    public static final String HOST = "TF_VAR_cc_host";
    public static final String RELEASE_TYPE = "TF_VAR_release_type";
    public static final String CC_AUTH_TOKEN = "TF_VAR_cc_auth_token";
    public static final String CC_VPC_ID_LABEL = "TF_VAR_cc_vpc_id";
    public static final String CC_ROUTE_TABLE_ID_LABEL = "TF_VAR_route_table_id";
    public static final String CC_VPC_CIDR_LABEL = "TF_VAR_cc_vpc_cidr";
    public static final String CC_REGION_LABEL = "TF_VAR_cc_region";
    public static final String CC_TF_STATE_BUCKET_LABEL = "TF_VAR_cc_tf_state_bucket";
    private static final String CC_TF_STATE_REGION_LABEL = "TF_VAR_cc_tf_state_region";

    public static final String STACK_SUBDIRECTORY = "STACK_SUBDIRECTORY";
    public static final String SUBSTACK_SUBDIRECTORY_PREFIX = "SUBSTACK_SUBDIRECTORY_";
    public static final String STACK_NAME = "STACK_NAME";
    private static final String CLUSTER_NAME = "CLUSTER_NAME";
    private static final String CLOUD_TF_PROVIDER = "CLOUD_TF_PROVIDER";
    private static final String CC_TF_DYNAMO_TABLE_LABEL = "TF_VAR_cc_tf_dynamo_table";


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

    @Value("${cc_artifact_s3bucket}")
    private String artifactS3Bucket;

    @Value("${aws.s3bucket.testOutputBucket.region}")
    private String artifactS3BucketRegion;

    @Value("${aws.s3bucket.ccSubstackBucket.name}")
    private String substackS3Bucket;

    @Autowired
    private DeploymentLogRepository deploymentLogRepository;

    @Autowired
    private QASuiteResultRepository qaSuiteResultRepository;

    @Autowired
    private GitService gitService;

    @Autowired
    private StackService stackService;

    @Autowired
    private CloudTFImplementationSelector tfImplementationSelector;

    @Autowired
    private BuildSpecService buildSpecService;

    @PostConstruct
    public void test() {
        System.out.println("test");
    }

    /**
     * Deploy the latest build in the specified clusterId
     *
     * @param abstractCluster   Cluster Information
     * @param deploymentRequest Additional params
     * @param deploymentContext
     * @return
     */
    @Override
    public DeploymentLog deployLatest(AbstractCluster abstractCluster, DeploymentRequest deploymentRequest, DeploymentContext deploymentContext) {
        ReleaseType releaseType = deploymentRequest.getReleaseType();
        List<EnvironmentVariable> extraEnv = deploymentRequest.getExtraEnv().entrySet().stream()
                .map(x ->
                        EnvironmentVariable.builder()
                                .name(x.getKey())
                                .value(x.getValue())
                                .type(EnvironmentVariableType.PLAINTEXT).build())
                .collect(Collectors.toList());
        Optional<Stack> stackO = stackRepository.findById(abstractCluster.getStackName());
        if (!stackO.isPresent()) {
            throw new NotFoundException("The Stack for this cluster does not exist NOW");
        }
        Stack stack = stackO.get();

        List<Substack> substacks = stackService.getSubstacks(stack.getName());

        //DONE: Check if code build is defined for the said cloud
        List<EnvironmentVariable> environmentVariables = new ArrayList<>();
        environmentVariables.add(EnvironmentVariable.builder().name(CLUSTER_ID).value(abstractCluster.getId())
                .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(
                EnvironmentVariable.builder().name(CC_AUTH_TOKEN).value(authToken).type(EnvironmentVariableType.PLAINTEXT)
                        .build());
        environmentVariables.add(EnvironmentVariable.builder().name(STACK_NAME).value(abstractCluster.getStackName())
                .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(EnvironmentVariable.builder().name(CC_REGION_LABEL).value(BUILD_REGION.toString())
                .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(EnvironmentVariable.builder().name(CC_ROUTE_TABLE_ID_LABEL).value(CC_ROUTE_TABLE_ID)
                .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(EnvironmentVariable.builder().name(CC_VPC_ID_LABEL).value(CC_VPC_ID)
                .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(EnvironmentVariable.builder().name(CC_VPC_CIDR_LABEL).value(CC_VPC_CIDR)
                .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(EnvironmentVariable.builder().name(CC_TF_STATE_BUCKET_LABEL).value(CC_TF_STATE_BUCKET)
                .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(EnvironmentVariable.builder().name(CC_TF_DYNAMO_TABLE_LABEL).value(CC_TF_DYNAMO_TABLE)
                .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(EnvironmentVariable.builder().name(CC_TF_STATE_REGION_LABEL).value(CC_TF_STATE_REGION)
                .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(EnvironmentVariable.builder().name(CLUSTER_NAME).value(abstractCluster.getName())
                .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(EnvironmentVariable.builder().name(CLOUD_TF_PROVIDER).value(tfImplementationSelector.selectTFProvider(abstractCluster))
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

        if (!extraEnv.isEmpty()) {
            environmentVariables.addAll(extraEnv);
        }

        String buildName = "";
        switch (abstractCluster.getCloud()) {

            case AWS:
                environmentVariables.add(EnvironmentVariable.builder().name(CLUSTER_REGION).value(((AwsCluster)abstractCluster).getAwsRegion())
                        .type(EnvironmentVariableType.PLAINTEXT).build());
                buildName = BUILD_NAME;
                break;
            default:
                buildName = BUILD_NAME;
                break;
        }

        String primarySourceVersion = "master";

        if (!StringUtils.isEmpty(deploymentRequest.getOverrideCCVersion()) &&
                (abstractCluster.getStackName().equalsIgnoreCase("cc-stack-cctesting") || abstractCluster.getStackName().equalsIgnoreCase("cc-infra-testing"))) {
            primarySourceVersion = deploymentRequest.getOverrideCCVersion();
        }

        try {
            primarySourceVersion = gitService.getBranchHead("https://bitbucket.org/capillarymartjack/deisdeployer.git",
                    System.getenv("BITBUCKET_USERNAME"), System.getenv("BITBUCKET_PASSWORD"), primarySourceVersion);
        } catch (GitAPIException e) {
            // pass
        }

        String masterHead = "";

        try {
            masterHead = gitService.getBranchHead(stack.getVcsUrl(), stack.getUser(), stack.getAppPassword(), "master");
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }

        ProjectSourceVersion secondarySourceVersion =
                ProjectSourceVersion.builder().sourceIdentifier("STACK").sourceVersion(masterHead).build();


        if (abstractCluster.getCdPipelineParent() != null) {
            Optional<DeploymentLog> pipelineParentDeploymentOptional = Optional.empty();
            if (abstractCluster.getRequireSignOff()) {
                pipelineParentDeploymentOptional =
                        deploymentLogRepository.findFirstByClusterIdAndStatusAndDeploymentTypeAndSignedOffOrderByCreatedOnDesc(abstractCluster.getCdPipelineParent(),
                                StatusType.SUCCEEDED, DeploymentLog.DeploymentType.REGULAR, true);
            } else {
                pipelineParentDeploymentOptional =
                        deploymentLogRepository.findFirstByClusterIdAndStatusAndDeploymentTypeOrderByCreatedOnDesc(abstractCluster.getCdPipelineParent(),
                                StatusType.SUCCEEDED, DeploymentLog.DeploymentType.REGULAR);
            }

            if (pipelineParentDeploymentOptional.isPresent()) {
                DeploymentLog pipelineParentDeployment = pipelineParentDeploymentOptional.get();
                //primarySourceVersion = pipelineParentDeployment.getTfVersion();
                String stackSourceVersion = pipelineParentDeployment.getStackVersion();

                if (StringUtils.isEmpty(stackSourceVersion) || StringUtils.isEmpty(primarySourceVersion)) {
                    throw new RuntimeException("No reference build found");
                }
                secondarySourceVersion =
                        ProjectSourceVersion.builder().sourceIdentifier("STACK")
                                .sourceVersion(stackSourceVersion).build();
            } else {
                throw new RuntimeException("No reference build found");
            }
        }

        String deploymentContextVersion = md5sum(deploymentContext);

        // a regular trigger
        if (deploymentRequest.getOverrideBuildSteps() != null) {
            Optional<DeploymentLog> lastDeploymentOptional =
                    deploymentLogRepository.findFirstByClusterIdAndStatusAndDeploymentTypeOrderByCreatedOnDesc(abstractCluster.getId(),
                            StatusType.SUCCEEDED, DeploymentLog.DeploymentType.REGULAR);
            if (lastDeploymentOptional.isPresent()) {
                DeploymentLog lastDeployment = lastDeploymentOptional.get();
                if (secondarySourceVersion.sourceVersion().equalsIgnoreCase(lastDeployment.getStackVersion()) &&
                        deploymentContextVersion.equalsIgnoreCase(lastDeployment.getDeploymentContextVersion()) &&
                        primarySourceVersion.equalsIgnoreCase(lastDeployment.getTfVersion()) &&
                        CollectionUtils.isEmpty(deploymentRequest.getOverrideBuildSteps())
                ) {
                    DeploymentLog deploymentLog = getDeploymentLog(abstractCluster, deploymentRequest, secondarySourceVersion, deploymentContextVersion, UUID.randomUUID().toString(), primarySourceVersion);
                    deploymentLog.setStatus(StatusType.FAULT);
                    return deploymentLogRepository.save(deploymentLog);
                }
            }
        }

        ComputeType computeType = abstractCluster.getReleaseStream().equals(BuildStrategy.PROD) ?
                ComputeType.BUILD_GENERAL1_2_XLARGE : ComputeType.BUILD_GENERAL1_LARGE;

        List<ProjectSource> secondarySources = new ArrayList<>();
        secondarySources.add(ProjectSource.builder()
                .type(SourceType.valueOf(stack.getVcs().name()))
                .location(stack.getVcsUrl())
                .sourceIdentifier("STACK")
                .build());
        secondarySources.add(getDeploymentContextSource(deploymentContext));
        secondarySources.addAll(substacks.stream()
                .map(this::getSubstackSource)
                .collect(Collectors.toList()));

        StartBuildRequest startBuildRequest =
                StartBuildRequest.builder().projectName(buildName)
                        .environmentVariablesOverride(environmentVariables)
                        .computeTypeOverride(computeType)
                        .secondarySourcesOverride(secondarySources)
                        .secondarySourcesVersionOverride(secondarySourceVersion)
                        .sourceVersion(primarySourceVersion)
                        .buildspecOverride(getBuildSpec(deploymentRequest, abstractCluster.getCloud()))
                        .build();

        List<Build> runningBuilds = getRunningBuilds(abstractCluster, buildName);

        if (runningBuilds.size() > 0) {
            throw new IllegalStateException("Build is already in Progress: " + runningBuilds.get(0).id());
        }

        try {
            String buildId = getCodeBuildClient().startBuild(startBuildRequest).build().id();
            DeploymentLog log = getDeploymentLog(abstractCluster, deploymentRequest, secondarySourceVersion, deploymentContextVersion, buildId, primarySourceVersion);
            return deploymentLogRepository.save(log);

        } catch (ResourceNotFoundException ex) {
            throw new RuntimeException("No Build defined for " + abstractCluster.getCloud(), ex);
        }
    }

    private DeploymentLog getDeploymentLog(AbstractCluster cluster, DeploymentRequest deploymentRequest, ProjectSourceVersion secondarySourceVersion, String deploymentContextVersion, String buildId, String tfVersion) {
        DeploymentLog log = new DeploymentLog();
        if (deploymentRequest.getOverrideBuildSteps() != null
                && !deploymentRequest.getOverrideBuildSteps().isEmpty()) {
            log.setDeploymentType(DeploymentLog.DeploymentType.CUSTOM);
        } else if (deploymentRequest.getExtraEnv().containsKey("REDEPLOYMENT_BUILD_ID")) {
            log.setDeploymentType(DeploymentLog.DeploymentType.ROLLBACK);
        } else {
            log.setDeploymentType(DeploymentLog.DeploymentType.REGULAR);
        }
        log.setCodebuildId(buildId);
        log.setClusterId(cluster.getId());
        log.setDescription(deploymentRequest.getTag());
        log.setReleaseType(deploymentRequest.getReleaseType());
        log.setCreatedOn(new Date());
        log.setStackVersion(secondarySourceVersion.sourceVersion());
        //log.setDeploymentContext(deploymentContext);
        log.setTfVersion(tfVersion);
        log.setDeploymentContextVersion(deploymentContextVersion);
        log.setTriggeredBy(deploymentRequest.getTriggeredBy());
        log.setOverrideBuildSteps(deploymentRequest.getOverrideBuildSteps());
        return log;
    }

    private List<Build> getRunningBuilds(AbstractCluster cluster, String buildName) {
        ListBuildsForProjectRequest listBuildsForProjectRequest =
                ListBuildsForProjectRequest.builder().projectName(buildName).sortOrder(SortOrderType.DESCENDING).build();

        ListBuildsForProjectResponse listBuildsForProjectResponse =
                getCodeBuildClient().listBuildsForProject(listBuildsForProjectRequest);
        List<String> buildIds = listBuildsForProjectResponse.ids();
        List<String> shortListedBuilds = buildIds.stream().limit(10).collect(Collectors.toList());

        BatchGetBuildsRequest batchGetBuildsRequest = BatchGetBuildsRequest.builder().ids(shortListedBuilds).build();
        BatchGetBuildsResponse batchGetBuildsResponse = getCodeBuildClient().batchGetBuilds(batchGetBuildsRequest);
        List<Build> builds = batchGetBuildsResponse.builds();
        return builds.stream().filter(b -> b.buildStatus().equals(StatusType.IN_PROGRESS)).filter(
                b -> b.environment().environmentVariables().contains(
                        EnvironmentVariable.builder().name(CLUSTER_ID).value(cluster.getId())
                                .type(EnvironmentVariableType.PLAINTEXT).build())).collect(Collectors.toList());
    }

//    @Override
//    public StatusType getDeploymentStatus(String runId) {
//        return getBuild(runId).buildStatus();
//    }

    @Override
    public Build getBuild(String runId) {
        return codeBuildService.getBuild(BUILD_REGION, runId);
    }

    //    @Override
//    public Map<String, Build> getDeploymentStatuses(List<String> runIds) {
//        return codeBuildService.getBuilds(BUILD_REGION, runIds)
//                .stream().collect(Collectors.toMap(x->x.id(), x->x));
//    }
//
//    @Override
    private Map<String, Artifact> getDeploymentReport(String tfProvider, String runId) {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.valueOf(artifactS3BucketRegion)).build();
        try {
            String reportKey = String.format("%s/capillary-cloud-tf-apply/capillary-cloud-tf/%s/artifacts.json", runId.split(":")[1], tfProvider);
            String report = IOUtils.toString(amazonS3.getObject(artifactS3Bucket, reportKey).getObjectContent(), StandardCharsets.UTF_8.name());
            return new Gson().fromJson(report, new TypeToken<Map<String, Artifact>>() {
            }.getType());
        } catch (Throwable e) {
            return new HashMap<>();
        }
    }

    public TokenPaginatedResponse getBuildLogs(DeploymentLog deploymentLog, Optional<String> nextToken) {
        CodeBuildClient codeBuildClient = getCodeBuildClient();
        BatchGetBuildsResponse batchGetBuildsResponse = codeBuildClient.batchGetBuilds(BatchGetBuildsRequest.builder()
                .ids(deploymentLog.getCodebuildId())
                .build());
        Build build = batchGetBuildsResponse.builds().get(0);
        GetLogEventsRequest.Builder builder = GetLogEventsRequest.builder()
                .logGroupName(build.logs().groupName())
                .logStreamName(build.logs().streamName())
                .limit(100);
        if (nextToken == null || !nextToken.isPresent()) {
            builder.startFromHead(false);
        } else {
            builder.startFromHead(false);
            builder.nextToken(nextToken.get());
        }
        GetLogEventsResponse cloudWatchResponse = getCloudWatchLogsClient().getLogEvents(builder.build());
        List<OutputLogEvent> logEvents = cloudWatchResponse.events();
        List<LogEvent> logEventList = logEvents.stream()
                .map(x -> new LogEvent(x.timestamp(), x.message()))
                .collect(Collectors.toList());
        return new TokenPaginatedResponse(Lists.reverse(logEventList), cloudWatchResponse.nextBackwardToken(), build);

    }

    @Override
    public DeploymentLog loadDeploymentStatus(DeploymentLog deploymentLog, boolean loadBuildDetails) {
        // status is not present in db
        if (deploymentLog.getStatus() == null || deploymentLog.getStatus() == StatusType.IN_PROGRESS) {
            CodeBuildClient codeBuildClient = getCodeBuildClient();
            BatchGetBuildsResponse batchGetBuildsResponse =
                    codeBuildClient.batchGetBuilds(BatchGetBuildsRequest.builder()
                            .ids(deploymentLog.getCodebuildId()).build());
            Build build = batchGetBuildsResponse.builds().get(0);
            deploymentLog.setStatus(build.buildStatus());

            if (loadBuildDetails && !build.buildStatus().equals(StatusType.IN_PROGRESS)) {
                String streamName = build.logs().streamName();
                List<TerraformChange> terraformChanges = getTerraformChanges(streamName);
                deploymentLog.setStatus(build.buildStatus());
                deploymentLog.setChangesApplied(terraformChanges);
                Map<String, String> envVars
                        = build.environment().environmentVariables().stream().collect(Collectors.toMap(x -> x.name(), x -> x.value()));
                String tfProvider = envVars.getOrDefault("CLOUD_TF_PROVIDER", "tfaws");
                Map<String, Artifact> artifactMap = getDeploymentReport(tfProvider, build.id());
                List<AppDeployment> appDeployments = terraformChanges.stream()
                        .filter(x -> x.getResourcePath().contains("module.application.helm_release"))
                        .map(x -> new AppDeployment(x.getResourceKey(), artifactMap.get(x.getResourceKey())))
                        .distinct()
                        .collect(Collectors.toList());
                deploymentLog.setAppDeployments(appDeployments);
                deploymentLog.setTfVersion(build.resolvedSourceVersion());
                if (build.buildStatus().equals(StatusType.FAILED)) {
                    deploymentLog.setErrorLogs(getErrorLogs(streamName));
                }
                deploymentLogRepository.save(deploymentLog);
            }
        }

        if (!loadBuildDetails) {
            // reduce payload
            deploymentLog.setChangesApplied(null);
            deploymentLog.setAppDeployments(null);
            deploymentLog.setErrorLogs(null);
            //deploymentLog.setDeploymentContext(null);
        }

        return deploymentLog;
    }

    private List<String> getErrorLogs(String streamName) {
        CloudWatchLogsClient cloudWatchLogsClient = getCloudWatchLogsClient();
        FilterLogEventsResponse logEvents =
                cloudWatchLogsClient.filterLogEvents(FilterLogEventsRequest.builder()
                        .limit(10000).logGroupName(LOG_GROUP_NAME).logStreamNames(streamName)
                        .filterPattern("Error").build());
        Optional<FilteredLogEvent> earliestError = logEvents.events().stream().filter(x -> x.message().startsWith("Error: "))
                .min(Comparator.comparingLong(x -> x.timestamp()));

        if (earliestError.isPresent()) {
            return cloudWatchLogsClient.getLogEvents(GetLogEventsRequest.builder().logGroupName(LOG_GROUP_NAME)
                    .logStreamName(streamName).startTime(earliestError.get().timestamp()).build())
                    .events().stream().map(x -> x.message()).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public List<TerraformChange> getTerraformChanges(String codeBuildId) {
        CloudWatchLogsClient cloudWatchLogsClient = getCloudWatchLogsClient();
        FilterLogEventsResponse logEvents = cloudWatchLogsClient.filterLogEvents(FilterLogEventsRequest.builder()
                .limit(10000).logGroupName(LOG_GROUP_NAME)
                .logStreamNames(codeBuildId.replace(":", "/")).filterPattern(" complete after ").build());

        List<TerraformChange> changes = logEvents.events().stream()
                .filter(x -> !x.message().contains("module.overrides"))
                .filter(x -> !x.message().contains("module.application.null_resource.modified_applications"))
                .filter(x -> !x.message().contains("module.infra.module.ingress.kubernetes_secret.ingress-auth"))
                .filter(x -> !x.message().contains("module.application.null_resource.qa_deployment"))
                .map(x -> parseLogs(x.message()))
                .filter(x -> x != null).collect(Collectors.toList());
        return changes;
    }

    private TerraformChange parseLogs(String message) {
        try {
            Pattern p = Pattern.compile("^(.*): (.*) complete (.*)");
            Matcher matcher = p.matcher(message);
            if (matcher.find()) {
                String resourcePath = matcher.group(1);
                TerraformChange.TerraformChangeType changeType = TerraformChange.TerraformChangeType.valueOf(matcher.group(2));
                String resourceKey = null;
                Pattern p2 = Pattern.compile("^(.*)\\[\"(.*)\"\\]");
                Matcher m2 = p2.matcher(resourcePath);
                if (m2.find()) {
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

    private static String md5sum(Object input) {
        String dataJson = new Gson().toJson(input);
        byte[] data = dataJson.getBytes();
        try {
            byte[] md5bytes = MessageDigest.getInstance("MD5").digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : md5bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private ProjectSource getDeploymentContextSource(DeploymentContext deploymentContext) {
        String deploymentContextJson = new Gson().toJson(deploymentContext);
        byte[] data = deploymentContextJson.getBytes();
        try {
            String fileName = UUID.randomUUID().toString();
            File tempFile = Files.createTempFile(fileName + ".zip");
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(tempFile));
            ZipEntry e = new ZipEntry("deploymentcontext.json");
            out.putNextEntry(e);
            out.write(data, 0, data.length);
            out.closeEntry();
            out.close();
            AmazonS3 amazonS3 =
                    AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(BUILD_REGION.toString())).build();
            amazonS3.putObject(CC_STACK_SOURCE, tempFile.getName(), tempFile);
            tempFile.delete();
            return ProjectSource.builder().type(SourceType.S3)
                    .location(CC_STACK_SOURCE + "/" + tempFile.getName())
                    .sourceIdentifier("DEPLOYMENT_CONTEXT").build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private ProjectSource getSubstackSource(Substack substack) {
        return ProjectSource.builder()
                .type(SourceType.S3)
                .location(substackS3Bucket + "/" + substack.getArtifactPath())
                .sourceIdentifier("SUBSTACK_" + substack.getName())
                .build();
    }

    private String getBuildSpec(DeploymentRequest deploymentRequest, Cloud cloud) {
        try {
            BuildSpecService.YamlMap rawBuildSpecYaml = buildSpecService.getBuildSpecYaml(cloud);
            BuildSpecService.YamlMap buildSpecYaml = buildSpecService.overrideBuildSpec(rawBuildSpecYaml,
                    deploymentRequest.getOverrideBuildSteps(), deploymentRequest.getPreBuildSteps());
            return buildSpecService.toYamlString(buildSpecYaml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
