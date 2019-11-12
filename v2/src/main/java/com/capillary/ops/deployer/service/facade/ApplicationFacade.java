package com.capillary.ops.deployer.service.facade;

import com.amazonaws.regions.Regions;
import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.bo.webhook.bitbucket.BitbucketPREvent;
import com.capillary.ops.deployer.bo.webhook.github.GithubPREvent;
import com.capillary.ops.deployer.exceptions.*;
import com.capillary.ops.deployer.repository.ApplicationRepository;
import com.capillary.ops.deployer.repository.BuildRepository;
import com.capillary.ops.deployer.repository.DeploymentRepository;
import com.capillary.ops.deployer.repository.EnvironmentRepository;
import com.capillary.ops.deployer.service.S3DumpService;
import com.capillary.ops.deployer.service.SecretService;
import com.capillary.ops.deployer.service.VcsService;
import com.capillary.ops.deployer.service.VcsServiceSelector;
import com.capillary.ops.deployer.service.interfaces.ICodeBuildService;
import com.capillary.ops.deployer.service.interfaces.IECRService;
import com.capillary.ops.deployer.service.interfaces.IHelmService;
import com.capillary.ops.deployer.service.interfaces.IKubernetesService;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
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
    private IKubernetesService kubernetesService;

    @Autowired
    private S3DumpService s3DumpService;

    @Autowired
    private SecretService secretService;

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private VcsServiceSelector vcsServiceSelector;

    private static final Logger logger = LoggerFactory.getLogger(ApplicationFacade.class);

    @Value("${ecr.registry}")
    private String ecrRepoUrl;

    @Value("${aws.s3bucket.testOutputBucket.name}")
    private String testOutputS3Bucket;

    @Value("${aws.s3bucket.testOutputBucket.region}")
    private String testOutputS3BucketRegion;

    public Application createApplication(Application application) {
        if(application.getHealthCheck().getLivenessProbe().getPort() == 0) {
            application.getHealthCheck().setLivenessProbe(null);
        }
        if(application.getHealthCheck().getReadinessProbe().getPort() == 0) {
            application.getHealthCheck().setReadinessProbe(null);
        }
        applicationRepository.save(application);
        ecrService.createRepository(application);
        codeBuildService.createProject(application);
        if (application.isCiEnabled()) {
            createPullRequestWebhook(application);
        }

        return application;
    }

    private void createPullRequestWebhook(Application application) {
        try {
            vcsServiceSelector.selectVcsService(application.getVcsProvider())
                    .createPullRequestWebhook(application, getRepositoryOwner(application), getRepositoryName(application));
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
        Build build = new Build();
        int pullRequestNumber = pullRequest.getNumber();
        build.setApplicationId(application.getId());
        build.setTag(pullRequest.getSourceBranch());
        build.setDescription("Built via pull request " + pullRequestNumber);
        build.setTriggeredBy("capbuilder");
        build.setTestBuild(true);
        buildRepository.save(build);

        String testBuildId = codeBuildService.triggerBuild(application, build, true);
        build.setCodeBuildId(testBuildId);
        buildRepository.save(build);

        VcsService vcsService = vcsServiceSelector.selectVcsService(application.getVcsProvider());
        vcsService.processPullRequest(pullRequest, build);
        vcsService.commentOnPullRequest(pullRequest, "Build started");

        return true;
    }

    public Build createBuild(ApplicationFamily applicationFamily, Build build, boolean testBuild) {
        String username = getUserName();
        build.setTriggeredBy(username);
        String applicationId = build.getApplicationId();
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        buildRepository.save(build);
        String codeBuildId = codeBuildService.triggerBuild(application, build, testBuild);
        build.setCodeBuildId(codeBuildId);
        buildRepository.save(build);
        return build;
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
        if(applicationFamily.equals(ApplicationFamily.CRM) && !existingBuild.isPromoted() && build.isPromoted()){
            ecrService.syncToChinaECR(existingBuild.getImage());
        }
        existingBuild.setPromoted(build.isPromoted());
        buildRepository.save(existingBuild);
        return getBuildDetails(application, existingBuild);
    }

    public List<String> getApplicationBranches(ApplicationFamily applicationFamily, String applicationId) throws IOException {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        VcsService vcsService = vcsServiceSelector.selectVcsService(application.getVcsProvider());

        String repositoryName = getRepositoryName(application);
        String repositoryOwner = getRepositoryOwner(application);

        List<String> branches = vcsService.getBranches(repositoryOwner, repositoryName);
        List<String> tags = vcsService.getTags(repositoryOwner, repositoryName);

        return Stream.concat(branches.stream(), tags.stream()).collect(Collectors.toList());
    }

    private String getRepositoryName(Application application) {
        String[] repositoryUrlParts = application.getRepositoryUrl().split("/");
        return repositoryUrlParts[repositoryUrlParts.length - 1];
    }

    private String getRepositoryOwner(Application application) {
        String[] repositoryUrlParts = application.getRepositoryUrl().split("/");
        return repositoryUrlParts[repositoryUrlParts.length - 2];
    }

    private Build getBuildDetails(Application application, Build build, boolean includeImage) {
        software.amazon.awssdk.services.codebuild.model.Build codeBuildServiceBuild =
                codeBuildService.getBuild(application, build.getCodeBuildId());
        StatusType status = codeBuildServiceBuild.buildStatus();
        build.setStatus(status);
        if(codeBuildServiceBuild.buildStatus().equals(StatusType.SUCCEEDED) && includeImage) {
            build.setImage(ecrService.findImageBetweenTimes(application,
                    codeBuildServiceBuild.startTime(), codeBuildServiceBuild.endTime()));
        }
        return build;
    }

    private List<Build> getBuildDetails(Application application, List<Build> builds) {
        Map<String, Build> codeBuildToBuildMap = builds.parallelStream()
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
        return deployment.isPresent() ? deployment.get() : null;
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
        if(env.getEnvironmentMetaData().getEnvironmentType().equals(EnvironmentType.PRODUCTION)){
            if(!getBuildPromotionStatus(applicationId, deployment.getBuildId())) {
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
        if(StringUtils.isEmpty(build.getImage())) {
            throw new NotFoundException("No image");
        }

        if (Application.ApplicationType.SCHEDULED_JOB.equals(application.getApplicationType())) {
            validateCronExpression(deployment.getSchedule());
        }

        deployment.setImage(build.getImage());
        String mirror = env.getEnvironmentConfiguration() != null ?
                env.getEnvironmentConfiguration().getEcrMirrorRepo() : null;
        if(mirror != null && ! mirror.isEmpty()) {
            deployment.setImage(deployment.getImage().replaceAll(ecrRepoUrl, mirror));
        }
        deploymentRepository.save(deployment);
        helmService.deploy(application, deployment);
        return deployment;
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
        Optional<Build> build = buildRepository.findOneByApplicationIdAndId(applicationId,buildId);
        return build.get().isPromoted();
    }

    public List<ApplicationPodDetails> getApplicationPodDetails(ApplicationFamily applicationFamily, String environmentName, String applicationId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        String releaseName = helmService.getReleaseName(application, environment);
        return kubernetesService.getApplicationPodDetails(application, environment, releaseName);
    }

    public DeploymentStatusDetails getDeploymentStatus(ApplicationFamily applicationFamily, String environmentName, String applicationId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        DeploymentStatusDetails deploymentStatus = kubernetesService.getDeploymentStatus(application, environment, helmService.getReleaseName(application, environment));
        return deploymentStatus;
    }

    public List<String> getImages(ApplicationFamily applicationFamily, String applicationId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        return ecrService.listImages(application);
    }

    public S3DumpFile downloadDumpFileFromS3(String path) {
        return s3DumpService.downloadObject(path);
    }

    public S3DumpFile downloadTestReport(String applicationName, String buildId) {
        String path = buildId + "/" + applicationName;
        return s3DumpService.downloadObject(testOutputS3Bucket, path, Regions.valueOf(testOutputS3BucketRegion));
    }

    public List<String> listDumpFilesFromS3(ApplicationFamily applicationFamily, String environment, String applicationId, String date) {
        Environment env = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environment).get();
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        String releaseName = getReleaseName(application, env);
        return s3DumpService.listObjects(applicationFamily, environment, releaseName, getDateForDump(date));
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

    public void rollbackDeployment(ApplicationFamily applicationFamily, String environmentName, String applicationId){
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        helmService.rollback(application,environment);
    }

    private String getUserName() {
        String username = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof OAuth2User) {
            username = ((OAuth2User)principal).getName();
        } else {
            username = principal.toString();
        }
        return username;
    }

    public List<EnvironmentMetaData> getEnvironmentMetaData(ApplicationFamily applicationFamily) {
        return environmentRepository.findByEnvironmentMetaDataApplicationFamily(applicationFamily)
                .stream().map(Environment::getEnvironmentMetaData).collect(Collectors.toList());
    }

    public List<Environment> getEnvironments(ApplicationFamily applicationFamily) {
        return new ArrayList<>(environmentRepository.findByEnvironmentMetaDataApplicationFamily(applicationFamily));
    }

    public Application updateApplication(Application application) {
        Application existingApplication =
                applicationRepository
                        .findOneByApplicationFamilyAndId(application.getApplicationFamily(),
                                application.getId()).get();
        if (!existingApplication.isCiEnabled() && application.isCiEnabled()) {
            createPullRequestWebhook(application);
        }

        application.setName(existingApplication.getName());
        application.setBuildType(existingApplication.getBuildType());
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
        return false;
    }

    public String getReleaseName(Application application, Environment environment) {
        return org.apache.commons.lang3.StringUtils.isEmpty(environment.getEnvironmentConfiguration().getNodeGroup()) ?
                application.getName() :
                environment.getEnvironmentMetaData().getName() + "-" + application.getName();
    }

    public DeploymentStatusDetails getCronjobHistory(ApplicationFamily applicationFamily, String environmentName, String applicationId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        DeploymentStatusDetails deploymentStatus = kubernetesService.getDeploymentStatus(application, environment, helmService.getReleaseName(application, environment));
        return deploymentStatus;
    }
}
