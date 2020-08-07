package com.capillary.ops.deployer.service.facade;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.capillary.ops.App;
import com.capillary.ops.cp.service.CCAdapterService;
import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.bo.actions.ActionExecution;
import com.capillary.ops.deployer.bo.actions.ApplicationAction;
import com.capillary.ops.deployer.bo.actions.CreationStatus;
import com.capillary.ops.deployer.bo.webhook.bitbucket.BitbucketPREvent;
import com.capillary.ops.deployer.bo.webhook.github.GithubPREvent;
import com.capillary.ops.deployer.bo.webhook.sonar.CallbackBody;
import com.capillary.ops.deployer.bo.webhook.sonar.Condition;
import com.capillary.ops.deployer.component.DeployerHttpClient;
import com.capillary.ops.deployer.exceptions.*;
import com.capillary.ops.deployer.repository.*;
import com.capillary.ops.deployer.service.IVcsServiceSelector;
import com.capillary.ops.deployer.service.S3DumpService;
import com.capillary.ops.deployer.service.SecretService;
import com.capillary.ops.deployer.service.VcsService;
import com.capillary.ops.deployer.service.interfaces.*;
import com.capillary.ops.deployer.service.newrelic.INewRelicService;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import com.github.alturkovic.lock.Interval;
import com.github.alturkovic.lock.redis.alias.RedisLocked;
import com.jcabi.aspects.Loggable;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Loggable
public class ApplicationFacade {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private IECRService ecrService;

    @Autowired
    private ICodeBuildService codeBuildService;

    @Autowired
    private BuildRepository buildRepository;

    @Autowired
    private IHelmService helmService;

    @Autowired
    private DeploymentRepository deploymentRepository;

    @Autowired
    private TestBuildDetailsRepository testBuildDetailsRepository;

    @Autowired
    private IKubernetesService kubernetesService;

    @Autowired
    private S3DumpService s3DumpService;

    @Autowired
    private SecretService secretService;

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private IVcsServiceSelector vcsServiceSelector;

    @Autowired
    private ApplicationActionRepository applicationActionRepository;

    @Autowired
    private IActionsService actionsService;

    private static final Logger logger = LoggerFactory.getLogger(ApplicationFacade.class);

    @Value("${ecr.registry}")
    private String ecrRepoUrl;

    @Value("${aws.s3bucket.testOutputBucket.name}")
    private String testOutputS3Bucket;

    @Value("${aws.s3bucket.testOutputBucket.region}")
    private String testOutputS3BucketRegion;

    @Autowired
    private INewRelicService newRelicService;

    @Autowired
    private DeployerHttpClient httpClient;

    @Autowired
    private ActionExecutionRepository actionExecutionRepository;

    @Autowired
    private PullRequestRepository pullRequestRepository;

    @Autowired
    private CCAdapterService ccAdapterService;

    public Application createApplication(Application application, String host) {
        if (application.getHealthCheck().getLivenessProbe().getPort() == 0) {
            application.getHealthCheck().setLivenessProbe(null);
        }
        if (application.getHealthCheck().getReadinessProbe().getPort() == 0) {
            application.getHealthCheck().setReadinessProbe(null);
        }

        String statusCallbackUrl = application.getStatusCallbackUrl();
        if (!StringUtils.isEmpty(statusCallbackUrl) && !isValidCallbackUrl(statusCallbackUrl)) {
            throw new RuntimeException("Invalid Status Callback Url");
        }

        applicationRepository.save(application);
        ecrService.createRepository(application);
        codeBuildService.createProject(application);
        if (application.isCiEnabled()) {
            createPullRequestWebhook(application, host);
        }

        return application;
    }

    private boolean isValidCallbackUrl(String statusCallbackUrl) {
        try {
            new URL(statusCallbackUrl).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }

        return statusCallbackUrl.startsWith("https://api.flock.com/hooks/sendMessage/");
    }

    private void createPullRequestWebhook(Application application, String host) {
        try {
            String repositoryOwner = getRepositoryOwner(application);
            String repositoryName = getRepositoryName(application);

            String webhookId = vcsServiceSelector
                    .selectVcsService(application.getVcsProvider())
                    .createPullRequestWebhook(application, repositoryOwner, repositoryName, host);

            if (!StringUtils.isEmpty(webhookId)) {
                application.setWebhookId(webhookId);
                applicationRepository.save(application);
            }
        } catch (IOException e) {
            logger.error("could not access the project repository");
        } catch (Exception e) {
            logger.error("unknown error happened while creating webhook", e);
        }
    }

    public Application getApplication(ApplicationFamily applicationFamily, String applicationId) {
        return applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
    }

    public boolean processWebhookPRGithub(ApplicationFamily applicationFamily, String applicationId,
                                          GithubPREvent webhook, String host) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        VcsService vcsService = vcsServiceSelector.selectVcsService(application.getVcsProvider());
        try {
            PullRequest pullRequest = webhook.toPullRequest();
            pullRequest.setHost(host);
            if (!application.isCiEnabled() || !vcsService.shouldTriggerBuild(application, pullRequest)) {
                return true;
            }

            return processPullRequest(application, pullRequest);
        } catch (ParseException e) {
            logger.error("error happened while parsing pull request date", e);
        }

        logger.error("could not build application");
        return false;
    }

    public boolean processWebhookPRBitbucket(ApplicationFamily applicationFamily, String applicationId,
                                             BitbucketPREvent webhook, String eventKey, String host) {
        String webhookType = eventKey.split(":")[0];
        String webhookAction = eventKey.split(":")[1];

        if (!webhookType.equals("pullrequest")) {
            return false;
        }

        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        VcsService vcsService = vcsServiceSelector.selectVcsService(application.getVcsProvider());
        try {
            PullRequest pullRequest = webhook.toPullRequest();
            pullRequest.setHost(host);
            pullRequest.setAction(webhookAction);
            pullRequest.setApplicationId(application.getId());
            if (!application.isCiEnabled() || !vcsService.shouldTriggerBuild(application, pullRequest)) {
                return true;
            }

            return processPullRequest(application, pullRequest);
        } catch (ParseException e) {
            logger.error("error happened while parsing pull request date", e);
        }

        logger.error("could not build application");
        return false;
    }

    private boolean processPullRequest(Application application, PullRequest pullRequest) {
        Build build = triggerTestBuild(application, pullRequest.getSourceBranch(), pullRequest.getNumber());

        VcsService vcsService = vcsServiceSelector.selectVcsService(application.getVcsProvider());
        vcsService.processPullRequest(pullRequest, build);
        vcsService.commentOnPullRequest(pullRequest, "Build started");

        return true;
    }

    private Build triggerTestBuild(Application application, String branch, Integer requestNumber) {
        Build build = new Build();
        build.setApplicationId(application.getId());
        build.setApplicationId(application.getId());
        build.setTag(branch);
        build.setApplicationFamily(application.getApplicationFamily());
        build.setDescription("Built via pull request " + requestNumber);
        build.setTriggeredBy("capbuilder");
        build.setTestBuild(true);

        if (build.getEnvironmentVariables() == null)
            build.setEnvironmentVariables(new HashMap<>());

        build.getEnvironmentVariables().putIfAbsent("pullRequestNumber", requestNumber + "");
        build.getEnvironmentVariables().putIfAbsent("appId", application.getId());
        build.getEnvironmentVariables().putIfAbsent("appFamily", build.getApplicationFamily().name());
        buildRepository.save(build);

        build.getEnvironmentVariables().putIfAbsent("deployerBuildId", build.getId());

        String testBuildId = codeBuildService.triggerBuild(application, build, true);
        build.setCodeBuildId(testBuildId);
        buildRepository.save(build);
        return build;
    }

    @RedisLocked(
            expression = "#applicationFamily + '_' + #build.getApplicationId() + '_' + #build.getTag()",
            expiration = @Interval(value = "1", unit = TimeUnit.MINUTES)
    )
    public Build createBuild(ApplicationFamily applicationFamily, Build build) {
        String applicationId = build.getApplicationId();
        if (canTriggerBuild(build, applicationId)) {
            String username = getUserName();
            build.setTriggeredBy(username);
            Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();

            String codeBuildId;
            try {
                codeBuildId = codeBuildService.triggerBuild(application, build, build.isTestBuild());
                buildRepository.save(build);
            } catch (Exception ex) {
                logger.error("error happened while triggering application build");
                postMessageToFlock(application, getBuildTriggerFailureMessage(build, applicationFamily));
                throw ex;
            }
            build.setCodeBuildId(codeBuildId);
            buildRepository.save(build);
            return build;
        }

        throw new RuntimeException("unknown exception while triggering build");
    }

    private String getBuildTriggerFailureMessage(Build build, ApplicationFamily applicationFamily) {
        JSONObject message = new JSONObject();
        message.put("text", "build trigger for applicationFamily: " + applicationFamily + ", applicationId: " + build.getApplicationId() + " failed");
        return message.toString();
    }

    private boolean canTriggerBuild(Build build, String applicationId) {
        List<Build> existingBuilds = buildRepository.findByApplicationIdOrderByTimestampDesc(applicationId);
        if (existingBuilds.size() > 0) {
            Build latestBuild = existingBuilds.get(0);
            if (StatusType.IN_PROGRESS.equals(latestBuild.getStatus()) && latestBuild.getTag().equals(build.getTag())) {
                throw new ConcurrentExecutionException("concurrent builds of same application and branch cannot run at the same time");
            }
        }

        return true;
    }

    public List<Application> getApplications(ApplicationFamily applicationFamily) {
        return applicationRepository.findByApplicationFamily(applicationFamily);
    }

    public Build getBuild(ApplicationFamily applicationFamily, String applicationId, String buildId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Build build = buildRepository.findOneByApplicationIdAndId(application.getId(), buildId).get();
        return getBuildDetails(application, build, true);
    }

    public Build updateBuild(ApplicationFamily applicationFamily, String applicationId, String buildId, Build build) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Build existingBuild = buildRepository.findOneByApplicationIdAndId(application.getId(), buildId).get();
        existingBuild = getBuildDetails(application, existingBuild, true);
        if (applicationFamily.equals(ApplicationFamily.CRM) || applicationFamily.equals(ApplicationFamily.OPS)) {
            if (!existingBuild.isPromoted() && build.isPromoted()) {
                ecrService.syncToChinaECR(existingBuild.getImage());
            }
        }
        existingBuild.setPromoted(build.isPromoted());
        if (build.isPromoted()) {
            if (build.isPromoted() && build.getPromotionIntent().equals(PromotionIntent.NA)) {
                throw new IllegalArgumentException("PromotionIntent is Mandatory while promoting the build");
            }
            existingBuild.setPromotionIntent(build.getPromotionIntent());
        }
        buildRepository.save(existingBuild);
        return getBuildDetails(application, existingBuild);
    }

    public List<String> getApplicationBranches(ApplicationFamily applicationFamily, String applicationId) throws IOException {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        VcsService vcsService = vcsServiceSelector.selectVcsService(application.getVcsProvider());

        String repositoryName = getRepositoryName(application);
        String repositoryOwner = getRepositoryOwner(application);

        List<String> branches = vcsService.getBranches(repositoryOwner, repositoryName);
        return branches;
    }

    public List<String> getApplicationTags(ApplicationFamily applicationFamily, String applicationId) throws IOException {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        VcsService vcsService = vcsServiceSelector.selectVcsService(application.getVcsProvider());

        String repositoryName = getRepositoryName(application);
        String repositoryOwner = getRepositoryOwner(application);

        List<String> tags = vcsService.getTags(repositoryOwner, repositoryName);

        return tags;
    }

    public TestBuildDetails getTestBuildDetails(ApplicationFamily applicationFamily, String applicationId,
                                                String buildId) {
        TestBuildDetails build = testBuildDetailsRepository.findFirstByBuildId(buildId).get();
        return build;

    }

    private String getRepositoryName(Application application) {
        String[] repositoryUrlParts = application.getRepositoryUrl().split("/");
        return repositoryUrlParts[repositoryUrlParts.length - 1];
    }

    private String getRepositoryOwner(Application application) {
        String[] repositoryUrlParts = application.getRepositoryUrl().split("/");
        return repositoryUrlParts[repositoryUrlParts.length - 2];
    }

    @Cacheable(
            value = "codebuild",
            key = "#application.id + '_' + #build.codeBuildId + '_' + #includeImage",
            unless = "#result == null || (#result.getStatus().name() != 'SUCCEEDED') || #result.codeBuildId == null || #result.codeBuildId == ''"
    )
    public Build getBuildDetails(Application application, Build build, boolean includeImage) {
        software.amazon.awssdk.services.codebuild.model.Build codeBuildServiceBuild =
                codeBuildService.getBuild(application, build.getCodeBuildId());
        StatusType status = codeBuildServiceBuild.buildStatus();
        build.setStatus(status);
        if (StatusType.SUCCEEDED.equals(codeBuildServiceBuild.buildStatus()) && includeImage) {
            build.setImage(ecrService.findImageBetweenTimes(application,
                    codeBuildServiceBuild.startTime(), codeBuildServiceBuild.endTime()));
            String artifactLocation = codeBuildServiceBuild.artifacts().location();
            if (!StringUtils.isEmpty(artifactLocation)) {
                URL url = AmazonS3ClientBuilder.standard().build().generatePresignedUrl(
                        "deployer-test-build-output", codeBuildServiceBuild.id().split(":")[1] + "/" + application.getName(),
                        DateUtils.addHours(new Date(), 2)
                );
                build.setArtifactUrl(url.toString());
            }
        }
        return build;
    }

    private List<Build> getBuildDetails(Application application, List<Build> builds) {
        Map<String, Build> codeBuildToBuildMap = builds.parallelStream()
                .filter(build -> build.getCodeBuildId() != null)
                .collect(Collectors.toMap(Build::getCodeBuildId, Function.identity()));
        List<software.amazon.awssdk.services.codebuild.model.Build> codeBuildServiceBuilds =
                codeBuildService.getBuilds(application, new ArrayList<>(codeBuildToBuildMap.keySet()));

        if (codeBuildServiceBuilds != null) {
            codeBuildServiceBuilds.parallelStream()
                    .forEach(x -> codeBuildToBuildMap.get(x.id()).setStatus(x.buildStatus()));
        }

        return builds;
    }

    public List<Build> getBuilds(ApplicationFamily applicationFamily, String applicationId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        List<Build> builds = buildRepository.findByApplicationIdOrderByTimestampDesc(application.getId());
        builds.parallelStream().forEach(x -> x.setApplicationFamily(applicationFamily));
        return getBuildDetails(application, builds);
    }

    private Build getBuildDetails(Application application, Build build) {
        return getBuildDetails(application, build, false);
    }

    public Deployment getCurrentDeployment(ApplicationFamily applicationFamily, String applicationId, String environment) {
        Optional<Deployment> deployment = deploymentRepository.findTopOneByApplicationFamilyAndApplicationIdAndEnvironmentOrderByTimestampDesc(applicationFamily, applicationId, environment);

        if (deployment.isPresent()) {
            return deployment.get();
        }
        Deployment ccDeployment = ccAdapterService.getCCDeployment(applicationFamily, applicationId, environment);
        return ccDeployment;
    }

    public TokenPaginatedResponse<LogEvent> getBuildLogs(ApplicationFamily applicationFamily,
                                                         String applicationId, String buildId,
                                                         String nextToken) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Build build = getBuild(applicationFamily, applicationId, buildId);
        return codeBuildService.getBuildLogs(application, build.getCodeBuildId(), nextToken);
    }

    public Deployment createDeployment(ApplicationFamily applicationFamily, String environment, String applicationId, Deployment deployment) {
        deployment.setTimestamp(new Date());
        Environment env = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environment).get();
        if (env.getEnvironmentMetaData().getEnvironmentType().equals(EnvironmentType.PRODUCTION)) {
            if (!getBuildPromotionStatus(applicationId, deployment.getBuildId())) {
                logger.info("Build {} is not promoted", deployment.getBuildId());
                throw new NotPromotedException("Build " + deployment.getBuildId() + " is not promoted");
            }
        }

        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        deployment.setApplicationId(application.getId());
        deployment.setApplicationFamily(applicationFamily);
        deployment.setEnvironment(environment);
        deployment.setDeployedBy(getUserName());
        Build build = getBuild(applicationFamily, applicationId, deployment.getBuildId());
        if (build.isTestBuild()) {
            logger.error("invalid action, cannot deploy a test build");
            throw new InvalidActionException("Invalid Action: cannot deploy a test build");
        }

        if (StringUtils.isEmpty(build.getImage())) {
            throw new NotFoundException("No image");
        }

        if (Application.ApplicationType.SCHEDULED_JOB.equals(application.getApplicationType())) {
            validateCronExpression(deployment.getSchedule());
        }

        deployment.setImage(build.getImage());
        String mirror = env.getEnvironmentConfiguration() != null ?
                env.getEnvironmentConfiguration().getEcrMirrorRepo() : null;
        if (mirror != null && !mirror.isEmpty()) {
            deployment.setImage(deployment.getImage().replaceAll(ecrRepoUrl, mirror));
        }
        if (env.getEnvironmentConfiguration() != null && env.getEnvironmentConfiguration().isPreDeployTaskEnabled()) {
            preDeployTasks(application, deployment);
        }
        try {
            deploymentRepository.save(deployment);
            helmService.deploy(application, deployment);
        } catch (Exception e) {
            deploymentRepository.delete(deployment);
            logger.error("error happened while deploying application");
//            postMessageToFlock(
//                    application,
//                    getDeploymentFailureMessage(deployment, application));
            throw e;
        }
        return deployment;
    }

    private void preDeployTasks(Application application, Deployment deployment) {
        try {
            switch (application.getApplicationFamily()) {
                case CRM:
                    logger.info("Evaluating pre deploy task");
                    if (deployment.getConfigurationsMap().containsKey("zkPublish") && deployment.getConfigurationsMap().containsKey("zkName")) {

                        Deployment currentDeployment = getCurrentDeployment(application.getApplicationFamily(), application.getId(), deployment.getEnvironment());
                        if (currentDeployment != null && currentDeployment.getConfigurationsMap().get("zkPublish").equals(deployment.getConfigurationsMap().get("zkPublish"))) {
                            logger.info("Skipping pre-deploy task for {} on {}", application.getName(), deployment.getEnvironment());
                            break;
                        }
                        if (deployment.getConfigurationsMap().get("zkPublish").equals("true")) {
                            logger.info("Attempting to delete zk mirror service");
                            Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(application.getApplicationFamily(), deployment.getEnvironment()).get();
                            kubernetesService.deleteServiceCreatedByKubeCompassIfExists(application.getName(), environment);
                        }
                    }
            }
        } catch (Exception e) {
            logger.error("Error executing pre-deploy task", e);
        }
    }

    private String getDeploymentFailureMessage(Deployment deployment, Application application) {
        JSONObject message = new JSONObject();
        message.put("text", "Deployment for application: " + application.getName() +
                ", with buildId: " + deployment.getBuildId() +
                ", failed for environment: " + deployment.getEnvironment());
        return message.toString();
    }

    private void postMessageToFlock(Application application, String message) {
        List<String> statusCallbackUrls = application.getStatusCallbackUrls();
        statusCallbackUrls.forEach(webhookUrl -> {
            if (StringUtils.isEmpty(webhookUrl)) {
                logger.info("status callback url not defined, not failure to flock");
                return;
            }

            try {
                httpClient.makePOSTRequest(webhookUrl, message, "", "");
            } catch (Exception e) {
                logger.error("error happened while posting message to flock: {}", message, e);
            }
        });
    }

    private void validateCronExpression(String deploymentSchedule) {
        if (deploymentSchedule.isEmpty()) {
            logger.error("cron schedule cannot be empty");
        }

        try {
            CronDefinition definition = CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX);
            CronParser parser = new CronParser(definition);
            parser.parse(deploymentSchedule);
        } catch (IllegalArgumentException ex) {
            logger.error("invalid cron expression for deployment schedule", ex);
            throw new InvalidScheduleException("Invalid cron expression for schedule");
        }
    }

    private boolean getBuildPromotionStatus(String applicationId, String buildId) {
        Optional<Build> build = buildRepository.findOneByApplicationIdAndId(applicationId, buildId);
        return build.get().isPromoted();
    }

    public List<ApplicationPodDetails> getApplicationPodDetails(ApplicationFamily applicationFamily, String environmentName, String applicationId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        String releaseName = helmService.getReleaseName(application, environment);
        return kubernetesService.getApplicationPodDetails(application, environment, releaseName);
    }

    public DeploymentStatusDetails getDeploymentStatus(ApplicationFamily applicationFamily, String environmentName,
                                                       String applicationId) {
        Application application =
                applicationRepository.findById(applicationId).get();
        logger.info("Application from repo: {}", application.getName());
        Optional<Environment> environmentO = environmentRepository
                .findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily,
                        environmentName);
        DeploymentStatusDetails deploymentStatus = null;
        if (!environmentO.isPresent()) {
            // This can be a CC request
            logger.info("This may be a cc request for {}", application.getName());
            Optional<EnvironmentMetaData> ccMeta =
                    ccAdapterService.getCCEnvironmentMeta(applicationFamily, environmentName);
            if (!ccMeta.isPresent()) {
                logger.info("No such Environment present in CC {}", environmentName);
                return null;
            }
            Environment environment = new Environment();
            EnvironmentConfiguration ec = ccAdapterService.getCCEnvironmentConfiguration(environmentName);
            environment.setEnvironmentConfiguration(ec);
            environment.setEnvironmentMetaData(ccMeta.get());
            deploymentStatus = kubernetesService.getDeploymentStatus(application, environment, applicationId, true);
        } else {
            deploymentStatus = kubernetesService
                    .getDeploymentStatus(application, environmentO.get(), helmService.getReleaseName(application,
                            environmentO.get()),
                            false);
        }
        return deploymentStatus;
    }

    public List<String> getImages(ApplicationFamily applicationFamily, String applicationId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        return ecrService.listImages(application);
    }

    public S3DumpFile downloadDumpFileFromS3(String path) {
        return s3DumpService.downloadObject(path);
    }

    public String getS3URL(String path) {
        return s3DumpService.getS3URL(path);
    }

    public S3DumpFile downloadTestReport(ApplicationFamily applicationFamily, String applicationId, String buildId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Build build = buildRepository.findOneByApplicationIdAndId(applicationId, buildId).get();
        Build buildDetails = getBuildDetails(application, build);
        logger.info("downloading test report for build: {}", build);

        if (StatusType.SUCCEEDED.equals(buildDetails.getStatus()) || StatusType.FAILED.equals(buildDetails.getStatus())) {
            logger.info("got build status as {}, downloading artifact file", buildDetails.getStatus());
            String path = buildDetails.getCodeBuildId().split(":")[1] + "/" + application.getName();
            S3DumpFile s3DumpFile = s3DumpService.downloadObject(testOutputS3Bucket, path, Regions.valueOf(testOutputS3BucketRegion));
            s3DumpFile.setApplicationName(application.getName());

            return s3DumpFile;
        }

        throw new NotFoundException("Artifacts can only be downloaded for successful or failed builds");
    }

    public Map<String, String> listDumpFilesFromS3(ApplicationFamily applicationFamily, String environment, String applicationId, String date) {
        Environment env = getEnvironmentWithCCFallback(applicationFamily, environment);
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        String releaseName = getReleaseName(application, env);
        List<String> paths = s3DumpService.listObjects(applicationFamily, environment, releaseName, getDateForDump(date));
        if (paths == null) {
            return new HashMap<>();
        }
        return paths.stream().collect(Collectors.toMap(x -> x, x -> getS3URL(x)));
    }

    private String getDateForDump(String date) {
        if (date != null) {
            return date;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    public boolean isDateValid(@RequestParam(required = false) String date) {
        if (date == null) {
            return false;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
        } catch (ParseException e) {
            logger.error("error parsing the date in yyyy-MM-dd format", e);
            return false;
        }

        return true;
    }

    public List<ApplicationSecretRequest> createApplicaitonSecretRequest(ApplicationFamily applicationFamily, String applicationId, List<ApplicationSecretRequest> applicationSecretRequests) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        applicationSecretRequests.parallelStream().forEach(x -> {
            x.setApplicationFamily(applicationFamily);
            x.setApplicationId(applicationId);
        });

        List<ApplicationSecretRequest> savedSecrets = secretService.getApplicationSecretRequests(applicationFamily, applicationId);
        if (!Collections.disjoint(savedSecrets, applicationSecretRequests)) {
            throw new AlreadyExistsException("some secrets have already been created");
        }
        return secretService.initializeApplicationSecrets(applicationSecretRequests);
    }

    public List<ApplicationSecretRequest> getApplicaitonSecretRequests(ApplicationFamily applicationFamily, String applicationId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        List<ApplicationSecretRequest> applicationSecrets = secretService.getApplicationSecretRequests(applicationFamily, applicationId);
        return applicationSecrets;
    }

    public List<ApplicationSecret> updateApplicaitonSecrets(String environmentName, ApplicationFamily applicationFamily, String applicationId, List<ApplicationSecret> applicationSecrets) {
        applicationSecrets.parallelStream().forEach(x -> {
            if (StringUtils.isEmpty(x.getSecretName()) || StringUtils.isEmpty(x.getSecretValue())) {
                throw new InvalidSecretException("secret name and value cannot be empty");
            }
        });
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();

        if (!doSecretsExist(applicationFamily, applicationId, applicationSecrets)) {
            throw new NotFoundException("some secrets have not been created");
        }

//        String releaseName = helmService.getReleaseName(application, environment);
//        Map<String, String> secretMap = applicationSecrets.parallelStream()
//                .collect(Collectors.toMap(ApplicationSecret::getSecretName, ApplicationSecret::getSecretValue));
//        kubernetesService.createOrUpdateSecret(environment, releaseName + "-credentials", secretMap);

        return secretService.updateApplicationSecrets(environmentName, applicationFamily, applicationId, applicationSecrets);
    }

    private boolean doSecretsExist(ApplicationFamily applicationFamily, String applicationId, List<ApplicationSecret> applicationSecrets) {
        List<ApplicationSecretRequest> savedSecrets = secretService.getApplicationSecretRequests(applicationFamily, applicationId);
        return savedSecrets.parallelStream().map(ApplicationSecretRequest::getSecretName).collect(Collectors.toList())
                .containsAll(applicationSecrets.parallelStream().map(ApplicationSecret::getSecretName).collect(Collectors.toList()));
    }

    public void rollbackDeployment(ApplicationFamily applicationFamily, String environmentName, String applicationId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        helmService.rollback(application, environment);
    }

    private String getUserName() {
        String username = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof OAuth2User) {
            username = ((OAuth2User) principal).getName();
        } else {
            username = principal.toString();
        }
        return username;
    }

    public List<EnvironmentMetaData> getEnvironmentMetaData(ApplicationFamily applicationFamily) {
        return environmentRepository.findByEnvironmentMetaDataApplicationFamily(applicationFamily)
                .stream().map(Environment::getEnvironmentMetaData).collect(Collectors.toList());
    }

    public List<EnvironmentMetaData> getEnvironmentMetaDataCC(ApplicationFamily applicationFamily) {
        List<EnvironmentMetaData> ccEnvs = ccAdapterService.getCCEnvironmentMetaList(applicationFamily);
        return ccEnvs;
    }

    public List<Environment> getEnvironments(ApplicationFamily applicationFamily) {
        return new ArrayList<>(environmentRepository.findByEnvironmentMetaDataApplicationFamily(applicationFamily));
    }

    public Application updateApplication(Application application, String host) {
        Application existingApplication =
                applicationRepository
                        .findOneByApplicationFamilyAndId(application.getApplicationFamily(),
                                application.getId()).get();
        if (!existingApplication.isCiEnabled() && application.isCiEnabled()) {
            createPullRequestWebhook(application, host);
        }

        application.setName(existingApplication.getName());
        application.setBuildType(existingApplication.getBuildType());
        codeBuildService.updateProject(application);
        return applicationRepository.save(application);
    }

    public Environment upsertEnvironment(ApplicationFamily applicationFamily, Environment environment) {
        return environmentRepository.save(environment);
    }

    public Environment getEnvironment(ApplicationFamily applicationFamily, String id) {
        return environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndId(applicationFamily, id).get();
    }

    public List<ApplicationSecret> getApplicationSecrets(String environmentName, ApplicationFamily applicationFamily,
                                                         String applicationId) {
        Application existingApplication =
                applicationRepository
                        .findOneByApplicationFamilyAndId(applicationFamily,
                                applicationId).get();
        return secretService.getApplicationSecrets(environmentName, applicationFamily, applicationId);
    }

    public GlobalStats getGlobalStats() {
        long applicationCount = this.applicationRepository.count();
        long buildCount = this.buildRepository.count();
        long deploymentCount = this.deploymentRepository.count();
        return new GlobalStats(applicationCount, buildCount, deploymentCount);
    }

    public boolean deleteApplication(ApplicationFamily applicationFamily, String applicationId) {
        Application application =
                applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        applicationRepository.delete(application);
        ecrService.deleteRepository(application);
        codeBuildService.deleteProject(application);
        return true;
    }

    public String getReleaseName(Application application, Environment environment) {
        EnvironmentConfiguration envConfig = environment.getEnvironmentConfiguration();
        if (envConfig == null || StringUtils.isEmpty(envConfig.getNodeGroup())) {
            return application.getName();
        }

        return environment.getEnvironmentMetaData().getName() + "-" + application.getName();
    }

    private boolean isValidActoinArgument(ApplicationAction applicationAction) {
        return true;
    }

    private Environment getEnvironmentWithCCFallback(ApplicationFamily applicationFamily, String environment) {
        Optional<Environment> env = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environment);
        if (env.isPresent()) {
            return env.get();
        }

        return ccAdapterService.getEnvironment(applicationFamily, environment);
    }

    public ActionExecution executeActionOnPod(ApplicationFamily applicationFamily, String environment,
                                              String applicationId, String podName, ApplicationAction applicationAction) {
        ApplicationAction existingAction = applicationActionRepository.findById(applicationAction.getId()).get();
        if (CreationStatus.FULFILLED.equals(existingAction.getCreationStatus()) && isValidActoinArgument(applicationAction)) {
            Environment env = getEnvironmentWithCCFallback(applicationFamily, environment);
            logger.info("executing action: {}, in environment: {}, on pod: {}", existingAction, env.getEnvironmentMetaData().getName(), podName);
            ActionExecution actionExecution = kubernetesService.executeAction(existingAction, env, podName);
            actionExecution.setApplicationId(applicationId);

            return actionExecutionRepository.save(actionExecution);
        }

        logger.info("action {} has not been fulfilled yet, cannot be run on application: {}", applicationAction, applicationId);
        throw new NotFulfilledException("The action has not been fulfilled. Please contact an admin");
    }

    public List<ApplicationAction> getActionsForPod(ApplicationFamily applicationFamily, String environment, String applicationId, String podName) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        List<ApplicationAction> genericActions = actionsService.getGenericActions(application.getBuildType());
        logger.info("fetched {} generic actions for buildType {}", genericActions.size(), application.getBuildType());

        List<ApplicationAction> applicationActions = actionsService.getApplicationActions(application.getId());
        logger.info("fetched {} application actions for application {}", applicationActions.size(), application.getId());

        return Stream.concat(genericActions.stream(), applicationActions.stream())
                .collect(Collectors.toList());
    }

    private boolean isValidGenericActionPath(ApplicationAction applicationAction) {
        String actionPath = applicationAction.getPath();
        if (StringUtils.isEmpty(actionPath)) {
            logger.error("error, empty path for action: {}", applicationAction);
            throw new InvalidActionException("action path cannot be empty");
        }

        if (!actionPath.startsWith("/commands/generic")) {
            logger.error("error, path for generic action: {} not starting with /commands/generic", applicationAction);
            throw new InvalidActionException("generic action path can only start with /commands/generic");
        }

        return true;
    }

    public ApplicationAction createGenericAction(BuildType buildType, ApplicationAction applicationAction) {
        if (isValidGenericActionPath(applicationAction)) {
            return actionsService.saveGenericAction(buildType, applicationAction);
        }

        logger.error("unknown error while saving action: {}", applicationAction);
        throw new RuntimeException("unknown error occured while saving generic action");
    }

    public boolean enableNewrelicMonitoring(ApplicationFamily applicationFamily,
                                            String applicationId, String environmentName) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        newRelicService.upsertDashboard(application, environment);
        return true;
    }

    public boolean disableNewrelicMonitoring(ApplicationFamily applicationFamily,
                                             String applicationId, String environmentName) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        newRelicService.deleteDashboard(application, environment);
        return true;
    }

    public boolean enableNewrelicAlerting(ApplicationFamily applicationFamily,
                                          String applicationId, String environmentName) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        newRelicService.createAlerts(application, environment);
        return true;
    }

    public boolean disableNewrelicAlerting(ApplicationFamily applicationFamily,
                                           String applicationId, String environmentName) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        newRelicService.disableAlerts(application, environment);
        return true;
    }

    public boolean shutdownApplication(ApplicationFamily applicationFamily, String applicationId, String environmentName) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        String releaseName = helmService.getReleaseName(application, environment);
        kubernetesService.haltApplication(releaseName, environment);
        return true;
    }

    public boolean resumeApplication(ApplicationFamily applicationFamily, String applicationId, String environmentName) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        String releaseName = helmService.getReleaseName(application, environment);
        kubernetesService.resumeApplication(releaseName, environment);
        return true;
    }

    public Monitoring getMonitoringDetails(ApplicationFamily applicationFamily, String applicationId,
                                           String environmentName) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        String newRelicServiceDashboardURL = newRelicService.getDashboardURL(application, environment);
        return new Monitoring(applicationFamily, applicationId, environmentName, newRelicServiceDashboardURL);
    }

    public List<ActionExecution> getExecutedActionsForApplication(
            ApplicationFamily applicationFamily, String applicationId) {
        applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        logger.info("getting executed actions for applicationFamily: {}, applicationId: {}",
                applicationFamily, applicationId);
        return actionsService.getLastNExecutions(applicationId, 10, "triggerTime");
    }

    public Alerting getAlertingDetails(ApplicationFamily applicationFamily, String applicationId,
                                       String environmentName) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        String newRelicServiceAlertsURL = newRelicService.getAlertsURL(application, environment);
        return new Alerting(applicationFamily, applicationId, environmentName, newRelicServiceAlertsURL);
    }

    public boolean deleteApplicaitonSecret(String environment, ApplicationFamily applicationFamily, String applicationId, String secretName) {
        return secretService.deleteApplicationSecret(environment, applicationFamily, applicationId, secretName);
    }

    public Map<String, Boolean> redeploy(ApplicationFamily applicationFamily, String environmentName) {
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        DeploymentList deployments = kubernetesService.getDeployments(environment);
        List<String> deploymentIds = deployments.getItems().stream()
                .filter(x -> x.getSpec().getTemplate() != null)
                .filter(x -> x.getSpec().getTemplate().getMetadata() != null)
                .filter(x -> x.getSpec().getTemplate().getMetadata().getAnnotations() != null)
                .filter(x -> x.getSpec().getTemplate().getMetadata().getAnnotations().containsKey("deploymentId"))
                .map(x -> x.getSpec().getTemplate().getMetadata().getAnnotations().get("deploymentId"))
                .collect(Collectors.toList());
        Map<String, Boolean> ret = new HashMap<>();
        for (String deploymentId : deploymentIds) {
            Deployment deployment = deploymentRepository.findById(deploymentId).get();
            Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, deployment.getApplicationId()).get();
            try {
                helmService.deploy(application, deployment);
                ret.put(application.getId(), true);
            } catch (Throwable t) {
                ret.put(application.getId(), false);
            }
        }
        return ret;
    }

    // to add the pr based on the status of sonar callback
    public boolean processSonarCallback(CallbackBody body) {
        Integer prNumber = Integer.parseInt(body.getPrNumber());
        String appId = body.getAppId();

        TestBuildDetails testBuildDetails = getTestBuildDetails(body);
        testBuildDetailsRepository.save(testBuildDetails);

        // it has a pr id
        if (prNumber != null && prNumber > 0) {

            PullRequest pullRequest = pullRequestRepository.findAllByApplicationIdAndNumber(appId, prNumber).get(0);
            Application application = applicationRepository.findById(pullRequest.getApplicationId()).get();
            VcsService vcsService = vcsServiceSelector.selectVcsService(application.getVcsProvider());

            final String newline = "-----         ";
            String message = "Status :" + body.getQualityGate().getStatus();
            message = message + newline + "Sonar url: " + testBuildDetails.getSonarUrl();
            for (Condition cond : body.getQualityGate().getConditions()) {
                // if any condition is not ok
                if (!cond.getStatus().equalsIgnoreCase("OK") && cond.getValue() != null) {
                    String value = cond.getValue();
                    message += newline + cond.getMetric() + " " + cond.getOperator() + " " + cond.getErrorThreshold()
                            + "(" + value + ")";
                }
            }

            // report all the failed
            vcsService.commentOnPullRequest(pullRequest, message);
        }

        return true;
    }

    private TestBuildDetails getTestBuildDetails(CallbackBody callbackBody) {

        TestBuildDetails ret = new TestBuildDetails();
        ret.setBuildId(callbackBody.getDeployerBuildId());
        ret.setPrId(callbackBody.getPrNumber());
        ret.setTestStatusRules(callbackBody.getQualityGate().getConditions());
        ret.setApplicationId(callbackBody.getAppId());
        ret.setApplicationFamily(callbackBody.getApplicationFamily());
        ret.setBranch(callbackBody.getBranch().getName());
        ret.setBranchType(callbackBody.getBranch().getType());
        ret.setSonarUrl(callbackBody.getBranch().getUrl());

        switch (callbackBody.getStatus()) {
            case "OK":
                ret.setTestStatus(TestBuildDetails.Status.PASS);
                break;

            default:
                ret.setTestStatus(TestBuildDetails.Status.FAIL);
                break;
        }
        return ret;

    }

    @Scheduled(cron = "0 0 * * * *")
    public void testBuildMasterBranch() {

        logger.info("Triggering all test builds on master branch");

        List<Application> applications = applicationRepository.findAll();

        // build for all ci enabled projects
        applications.parallelStream().forEach(application -> {
            if(application.isCiEnabled())
                triggerTestBuild(application, "master", 0);
        });

//        Calendar c = Calendar.getInstance();
//        Date y = new Date();
//        c.setTime(y);
//        c.add(Calendar.DATE, -7);
//        Date date = c.getTime();
//
//        List<String> applicationIds = buildRepository.
//                findApplicationIdDistinctByTestBuildAndTimestampGreaterThan(true, date);

//        applicationIds.parallelStream().forEach(id -> {
//            Application application = applicationRepository.findById(id).get();
//            triggerTestBuild(application, "master", 0);
//        });
    }

    public Map<String,ApplicationMetrics> getApplicationMetricSummary(ApplicationFamily applicationFamily,
                                                                String applicationId) {

        Map<String, ApplicationMetrics> ret = new HashMap<>();

        Date today = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -7);
        Date lastWeek = c.getTime();

        c.add(Calendar.DATE, -7);
        Date lastTwoWeek = c.getTime();

        ApplicationMetrics oldMetrics = getMetricsFromDeployer(applicationId, lastTwoWeek, lastWeek);
        ApplicationMetrics recentMetrics = getMetricsFromDeployer(applicationId, lastWeek, today);

        ret.putIfAbsent("old",oldMetrics);
        ret.putIfAbsent("new",recentMetrics);

        return ret;
    }

    private ApplicationMetrics getMetricsFromDeployer(String applicationId, Date periodStartDate,
                                                      Date periodEndDate){

        ApplicationMetrics metrics = new ApplicationMetrics(applicationId, periodEndDate);

        // sonar data
        TestBuildDetails buildDetails = testBuildDetailsRepository
                .findFirstByApplicationIdAndTimestampLessThanOrderByTimestampDesc(
                        applicationId, periodEndDate.getTime());

        if(buildDetails!= null)
            updateMetricObject( buildDetails.getTestStatusRules(), metrics);

        // test build data
        Integer failedBuilds= buildRepository.countBuildByApplicationIdAndTimestampBetween(
                applicationId, periodStartDate.getTime(), periodEndDate.getTime());
        metrics.setBuildFailures(failedBuilds);

        // new relic data
        Application application = applicationRepository.findById(applicationId).get();
        Map<String, Double> newrelicMetrics = newRelicService.getMetrics(
                application.getName(), periodStartDate, periodEndDate);
        if(newrelicMetrics!=null)
            updateMetricObject(newrelicMetrics, metrics);

        return metrics;

    }
    private void updateMetricObject(List<Condition> map, ApplicationMetrics metrics) {

        if(map == null)
            return;

        /*
        blocker_violations
        code_smells
        critical_violations
        security_hotspots_reviewed
        tests
         */
        map.stream().forEach(cond -> {
            if(cond.getMetric().equalsIgnoreCase("line_coverage"))
                metrics.setUnitTestCoverage(Integer.parseInt(cond.getMetric()));
        });

        map.stream().forEach(cond -> {
            if(cond.getMetric().equalsIgnoreCase("tests"))
                metrics.setUnitTests(Integer.parseInt(cond.getMetric()));
        });

        map.stream().forEach(cond -> {
            if(cond.getMetric().equalsIgnoreCase("code_smells"))
                metrics.setUnitTestCoverage(Integer.parseInt(cond.getMetric()));
        });

    }

    private void updateMetricObject(Map<String, Double> map, ApplicationMetrics metrics) {

        metrics.setResponseTime(map.getOrDefault("percentile_95", 0.0));
        metrics.setErrors((int) Math.round(map.getOrDefault("failures", 0.0)));
    }

    public List<ApplicationMetricsWrapper> getAllApplicationMetrics(ApplicationFamily applicationFamily){

        List<ApplicationMetricsWrapper> ret = new ArrayList<>();

        List<Application> applications = applicationRepository.findByApplicationFamilyAndApplicationType(applicationFamily,
                Application.ApplicationType.SERVICE);

        applications.parallelStream().forEach(application -> {
            Map<String, ApplicationMetrics> metrics = getApplicationMetricSummary(applicationFamily, application.getId());
            ret.add(new ApplicationMetricsWrapper(application, metrics.get("new"), metrics.get("old")));
        });

        return  ret;
    }
}
