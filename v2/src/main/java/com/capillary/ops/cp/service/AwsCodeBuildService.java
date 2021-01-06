package com.capillary.ops.cp.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.capillary.ops.App;
import com.capillary.ops.cp.bo.*;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.repository.DeploymentLogRepository;
import com.capillary.ops.cp.repository.QASuiteResultRepository;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.cp.repository.SubstackRepository;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.service.interfaces.ICodeBuildService;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
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
import org.yaml.snakeyaml.Yaml;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;
import software.amazon.awssdk.services.codebuild.CodeBuildClient;
import software.amazon.awssdk.services.codebuild.model.*;
import software.amazon.awssdk.services.codebuild.model.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
    public static final String CC_STACK_SOURCE = "cc-stack-source";
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String CLUSTER_ID = "CLUSTER_ID";

    private static final Region BUILD_REGION = Region.US_WEST_1;
    public static final String BUILD_NAME = "capillary-cloud-tf-apply";
    public static final String HOST = "TF_VAR_cc_host";
    public static final String RELEASE_TYPE = "TF_VAR_release_type";
    public static final String CC_AUTH_TOKEN = "TF_VAR_cc_auth_token";
    public static final String STACK_SUBDIRECTORY = "STACK_SUBDIRECTORY";
    public static final String SUBSTACK_SUBDIRECTORY_PREFIX = "SUBSTACK_SUBDIRECTORY_";
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
        List<EnvironmentVariable> extraEnv = deploymentRequest.getExtraEnv().entrySet().stream()
                .map(x ->
                        EnvironmentVariable.builder()
                                .name(x.getKey())
                                .value(x.getValue())
                                .type(EnvironmentVariableType.PLAINTEXT).build())
                .collect(Collectors.toList());
        Optional<Stack> stackO = stackRepository.findById(cluster.getStackName());
        if (!stackO.isPresent()) {
            throw new NotFoundException("The Stack for this cluster does not exist NOW");
        }
        Stack stack = stackO.get();

        List<Substack> substacks = stackService.getSubstacks(stack.getName());

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

        if (!StringUtils.isEmpty(deploymentRequest.getOverrideCCVersion()) &&
                cluster.getStackName().equalsIgnoreCase("cc-stack-cctesting")) {
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


        if (cluster.getCdPipelineParent() != null) {
            Optional<DeploymentLog> pipelineParentDeploymentOptional = Optional.empty();
            if(cluster.getRequireSignOff()) {
                pipelineParentDeploymentOptional =
                        deploymentLogRepository.findFirstByClusterIdAndStatusAndDeploymentTypeAndSignedOffOrderByCreatedOnDesc(cluster.getCdPipelineParent(),
                                StatusType.SUCCEEDED, DeploymentLog.DeploymentType.REGULAR, true);
            } else {
                pipelineParentDeploymentOptional =
                        deploymentLogRepository.findFirstByClusterIdAndStatusAndDeploymentTypeOrderByCreatedOnDesc(cluster.getCdPipelineParent(),
                                StatusType.SUCCEEDED, DeploymentLog.DeploymentType.REGULAR);
            }

            if (pipelineParentDeploymentOptional.isPresent()) {
                DeploymentLog pipelineParentDeployment = pipelineParentDeploymentOptional.get();
                //primarySourceVersion = pipelineParentDeployment.getTfVersion();
                String stackSourceVersion = pipelineParentDeployment.getStackVersion();

                if(StringUtils.isEmpty(stackSourceVersion) || StringUtils.isEmpty(primarySourceVersion)) {
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
                    deploymentLogRepository.findFirstByClusterIdAndStatusAndDeploymentTypeOrderByCreatedOnDesc(cluster.getId(),
                    StatusType.SUCCEEDED, DeploymentLog.DeploymentType.REGULAR);
            if (lastDeploymentOptional.isPresent()) {
                DeploymentLog lastDeployment = lastDeploymentOptional.get();
                if(secondarySourceVersion.sourceVersion().equalsIgnoreCase(lastDeployment.getStackVersion()) &&
                        deploymentContextVersion.equalsIgnoreCase(lastDeployment.getDeploymentContextVersion()) &&
                        primarySourceVersion.equalsIgnoreCase(lastDeployment.getTfVersion()) &&
                        CollectionUtils.isEmpty(deploymentRequest.getOverrideBuildSteps())
                ) {
                    DeploymentLog deploymentLog = getDeploymentLog(cluster, deploymentRequest, secondarySourceVersion, deploymentContextVersion, UUID.randomUUID().toString(), primarySourceVersion);
                    deploymentLog.setStatus(StatusType.FAULT);
                    return deploymentLogRepository.save(deploymentLog);
                }
            }
        }

        ComputeType computeType = cluster.getReleaseStream().equals(BuildStrategy.PROD) ?
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
                    .buildspecOverride(getBuildSpec(deploymentRequest))
                    .build();

        List<Build> runningBuilds = getRunningBuilds(cluster, buildName);

        if (runningBuilds.size() > 0) {
            throw new IllegalStateException("Build is already in Progress: " + runningBuilds.get(0).id());
        }

        try {
            String buildId = getCodeBuildClient().startBuild(startBuildRequest).build().id();
            DeploymentLog log = getDeploymentLog(cluster, deploymentRequest, secondarySourceVersion, deploymentContextVersion, buildId, primarySourceVersion);
            return deploymentLogRepository.save(log);

        } catch (ResourceNotFoundException ex) {
            throw new RuntimeException("No Build defined for " + cluster.getCloud(), ex);
        }
    }

    private DeploymentLog getDeploymentLog(AbstractCluster cluster, DeploymentRequest deploymentRequest, ProjectSourceVersion secondarySourceVersion, String deploymentContextVersion, String buildId, String tfVersion) {
        DeploymentLog log = new DeploymentLog();
        if(deploymentRequest.getOverrideBuildSteps() != null
                && !deploymentRequest.getOverrideBuildSteps().isEmpty()) {
            log.setDeploymentType(DeploymentLog.DeploymentType.CUSTOM);
        } else if (deploymentRequest.getExtraEnv().containsKey("REDEPLOYMENT_BUILD_ID")) {
            log.setDeploymentType(DeploymentLog.DeploymentType.ROLLBACK);
        }
        else {
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
    private Map<String, Artifact> getDeploymentReport(String runId) {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.valueOf(artifactS3BucketRegion)).build();
        try {
            String reportKey = String.format("%s/capillary-cloud-tf-apply/capillary-cloud-tf/tfaws/artifacts.json", runId.split(":")[1]);
            String report = IOUtils.toString(amazonS3.getObject(artifactS3Bucket, reportKey).getObjectContent(), StandardCharsets.UTF_8.name());
            return new Gson().fromJson(report, new TypeToken<Map<String, Artifact>>(){}.getType());
        } catch (Throwable e) {
            return new HashMap<>();
        }
    }

    @Override
    public DeploymentLog loadDeploymentStatus(DeploymentLog deploymentLog, boolean loadBuildDetails) {
        // status is not present in db
        if(deploymentLog.getStatus() == null || deploymentLog.getStatus() == StatusType.IN_PROGRESS) {
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
                Map<String, Artifact> artifactMap = getDeploymentReport(build.id());
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

        if(! loadBuildDetails) {
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

        if(earliestError.isPresent()) {
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
            File tempFile = Files.createTempFile( fileName + ".zip");
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(tempFile));
            ZipEntry e = new ZipEntry("deploymentcontext.json");
            out.putNextEntry(e);
            out.write(data, 0, data.length);
            out.closeEntry();
            out.close();
            AmazonS3 amazonS3 =
                    AmazonS3ClientBuilder.standard().withRegion(Regions.valueOf(artifactS3BucketRegion)).build();
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

    private String getBuildSpec(DeploymentRequest deploymentRequest) {
        try {
            String buildSpecYaml =
                CharStreams.toString(
                    new InputStreamReader(
                        App.class.getClassLoader().getResourceAsStream("cc/cc-buildspec.yaml"),
                            Charsets.UTF_8));
            if(deploymentRequest.getOverrideBuildSteps() == null ||
                    deploymentRequest.getOverrideBuildSteps().isEmpty()) {
                return buildSpecYaml;
            }
            Map<String, Object> buildSpec = new Yaml().load(buildSpecYaml);
            (((Map<String, Object>) ((Map<String, Object>) buildSpec.get("phases")).get("build")))
                    .put("commands", deploymentRequest.getOverrideBuildSteps());
            YAMLMapper yamlMapper = new YAMLMapper();
            yamlMapper.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true);
            yamlMapper.configure(YAMLGenerator.Feature.SPLIT_LINES, false);
            return yamlMapper.writeValueAsString(buildSpec);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
