package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.*;
import com.capillary.ops.cp.bo.notifications.ApplicationDeploymentNotification;
import com.capillary.ops.cp.bo.notifications.QASanityNotification;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.bo.wrappers.ListDeploymentsWrapper;
import com.capillary.ops.cp.exceptions.QACallbackAbsentException;
import com.capillary.ops.cp.repository.*;
import com.capillary.ops.cp.service.BaseDRService;
import com.capillary.ops.cp.service.BuildService;
import com.capillary.ops.cp.service.GitService;
import com.capillary.ops.cp.service.TFBuildService;
import com.capillary.ops.cp.service.notification.NotificationService;
import com.capillary.ops.deployer.component.DeployerHttpClient;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.repository.DeploymentRepository;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.jcabi.aspects.Loggable;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.batch.*;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.codebuild.model.EnvironmentVariable;
import software.amazon.awssdk.services.codebuild.model.EnvironmentVariableType;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Loggable
public class DeploymentFacade {

    @Autowired
    private ClusterFacade clusterFacade;

    @Autowired
    private TFBuildService tfBuildService;

    @Autowired
    private K8sCredentialsRepository k8sCredentialsRepository;

    @Autowired
    private QASuiteRepository qaSuiteRepository;

    @Autowired
    private DeploymentLogRepository deploymentLogRepository;

    @Autowired
    private QASuiteResultRepository qaSuiteResultRepository;

    @Autowired
    private DeployerHttpClient httpClient;

    @Autowired
    private OverrideObjectRepository overrideObjectRepository;

    @Autowired
    private GitService gitService;

    @Autowired
    private StackFacade stackFacade;

    @Autowired
    private ArtifactFacade artifactFacade;

    @Autowired
    private BaseDRService baseDRService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    private static final Logger logger = LoggerFactory.getLogger(DeploymentFacade.class);

    @Value("${flock.notification.cc.endpoint}")
    private String flockCCNotificationEndpoint;

    /**
     * Create a new Deployment
     *
     * @param clusterId         Id of the Cluster
     * @param deploymentRequest Any Additional Deployment Params
     * @return The Deployment Log Object
     */
    public DeploymentLog createDeployment(String clusterId, DeploymentRequest deploymentRequest) {
        try {
            AbstractCluster cluster = clusterFacade.getCluster(clusterId);
            DeploymentContext deploymentContext = getDeploymentContext(clusterId, deploymentRequest);
            //TODO: Save Deployment requests for audit purpose
            return tfBuildService.deployLatest(cluster, deploymentRequest, deploymentContext);
        } catch (QACallbackAbsentException e) {
            sendCCNotification(e.getMessage());
            throw e;
        }
    }

    private void sendCCNotification(String message) {
        JSONObject messageJson = new JSONObject();
        messageJson.put("text", message);

        DeployerHttpClient httpClient = new DeployerHttpClient();
        try {
            httpClient.makePOSTRequest(flockCCNotificationEndpoint, messageJson.toString(), "", "");
        } catch (IOException e) {
            logger.info("could not send capillary cloud notification: {}", messageJson);
            logger.error("error happened while sending capillary cloud notification", e);
        }
    }

    /**
     * Trigger job for automation suite
     * <p>
     * Strategy:
     * --------
     * Get the existing cronjob in the kubernetes cluster with the same name as the module
     * Get the jobTemplate spec from the cronjob
     * Set the new environment variables in the container of the spec that was fetched above
     * Create a new Job object using the jobSpec
     * Save the qaSuite in the repository and
     *
     * @param clusterId Id of the Cluster
     * @param qaSuite   qaSuite definition
     * @return executionId for the automation job
     */
    public String triggerAutomationSuite(String clusterId, QASuite qaSuite) throws Exception {
        AbstractCluster cluster = clusterFacade.getCluster(clusterId);
        String module = qaSuite.getModule();

        CronJob existingCronjob = getCronjobsInClusterWithName(clusterId, module);
        if (existingCronjob == null) {
            throw new NotFoundException("could not find any automation suite with module name: " + module);
        }

        String jobReferenceId = generateJobReferenceId(qaSuite);
        qaSuite.setJobReferenceId(jobReferenceId);
        qaSuite = qaSuiteRepository.save(qaSuite);

        try {
            JobTemplateSpec jobTemplate = existingCronjob.getSpec().getJobTemplate();
            Container container = jobTemplate.getSpec()
                    .getTemplate()
                    .getSpec()
                    .getContainers()
                    .get(0);
            Map<String, EnvVar> envVarMap =
                    container.getEnv().stream().collect(Collectors.toMap(EnvVar::getName, Function.identity()));
            qaSuite.getEnvironmentVariables().forEach((key, value) -> envVarMap.put(key, new EnvVarBuilder()
                    .withName(key)
                    .withValue(value)
                    .build()));
            ObjectMeta metadata = getJobMetadataForQASuite(qaSuite, existingCronjob, jobTemplate);
            container.setEnv(new ArrayList<>(envVarMap.values()));
            Job job = new JobBuilder().withSpec(jobTemplate.getSpec())
                    .withMetadata(metadata)
                    .build();

            KubernetesClient kubernetesClient = getKubernetesClientForCluster(clusterId);
            kubernetesClient.batch()
                    .jobs()
                    .inNamespace("default")
                    .create(job);
        } catch (Exception e) {
            logger.error("error happened while triggering qa automation suite", e);
            qaSuiteRepository.delete(qaSuite);
            throw new Exception("Could not trigger automation suite because of unknown error");
        }

        return qaSuite.getId();
    }

    private String generateJobReferenceId(QASuite qaSuite) {
        if (qaSuite.getRunId() != null) {
            String generatedString = RandomStringUtils.random(5, true, true);
            String referenceId = qaSuite.getRunId() + "-" + generatedString;
            return referenceId.toLowerCase();
        }

        return UUID.randomUUID().toString();
    }

    private ObjectMeta getJobMetadataForQASuite(QASuite qaSuite, CronJob existingCronjob, JobTemplateSpec jobTemplate) {
        ObjectMeta metadata = jobTemplate.getMetadata();
        String jobName = existingCronjob.getMetadata().getName() + "-" + qaSuite.getJobReferenceId();
        metadata.setName(jobName);

        Map<String, String> annotations = metadata.getAnnotations();
        if (annotations == null) {
            annotations = new HashMap<>();
        }
        annotations.put("source", "capillary-cloud");
        annotations.put("cc-kind", "qaSuite");
        annotations.put("cc-qaSuite-deploymentId", qaSuite.getDeploymentId());
        annotations.put("cc-qaSuite-executionId", qaSuite.getId());
        metadata.setAnnotations(annotations);

        return metadata;
    }

    /**
     * Trigger job for automation suite
     * <p>
     * Strategy:
     * --------
     * Get the existing cronjob in the kubernetes cluster with the given executionId
     * Delete the existing job for the automation suite
     *
     * @param clusterId   ID of the Cluster
     * @param executionId automation suite execution ID
     * @return void
     */
    public void abortAutomationSuite(String clusterId, String executionId) throws Exception {
        AbstractCluster cluster = clusterFacade.getCluster(clusterId);
        QASuite existingQASuite = qaSuiteRepository.findById(executionId).get();
        String module = existingQASuite.getModule();
        String jobName = module + "-" + existingQASuite.getJobReferenceId();

        Job existingJob = getJobInClusterWithName(clusterId, jobName);
        if (existingJob == null) {
            logger.error("could not find active job for module: " + module);
            throw new NotFoundException("Could not find active job for module " + module);
        }

        KubernetesClient kubernetesClient = getKubernetesClientForCluster(clusterId);
        kubernetesClient.batch()
                .jobs()
                .inNamespace("default")
                .withName(jobName)
                .delete();

        logger.info("aborted qa job with name: {}, executionId: {}", module, executionId);
    }

    /**
     * Get job status for automation suite
     *
     * @param clusterId   ID of the Cluster
     * @param executionId automation suite execution ID
     * @return String
     */
    public String getAutomationSuiteStatus(String clusterId, String executionId) throws Exception{
        try {
            QASuite existingQASuite = qaSuiteRepository.findById(executionId).get();
            String module = existingQASuite.getModule();
            String jobName = module + "-" + existingQASuite.getJobReferenceId();

            KubernetesClient kubernetesClient = getKubernetesClientForCluster(clusterId);
            Job qaSuite = kubernetesClient.batch()
                    .jobs()
                    .inNamespace("default")
                    .withName(jobName)
                    .get();

            if (qaSuite == null) {
                logger.info("Could not find active job for module: " + module);
                return K8sJobStatus.NA.name();
            }

            K8sJobStatus jobStatus = getK8sJobStatus(qaSuite);
            return jobStatus.name();
        }
        catch (Exception e){
            logger.error("Error while fetching QA job status", e);
            throw new Exception("Error while fetching QA job status");
        }
    }

    private K8sJobStatus getK8sJobStatus(Job qaSuite) {
        K8sJobStatus jobStatus = K8sJobStatus.NA;
        if (qaSuite.getStatus().getSucceeded() != null && qaSuite.getStatus().getSucceeded() > 0) {
            jobStatus = K8sJobStatus.SUCCESS;
        } else if (qaSuite.getStatus().getFailed() != null && qaSuite.getStatus().getFailed() > 0) {
            jobStatus = K8sJobStatus.FAILURE;
        } else if (qaSuite.getStatus().getActive() != null && qaSuite.getStatus().getActive() > 0) {
            jobStatus = K8sJobStatus.RUNNING;
        }

        return jobStatus;
    }

    private List<CronJob> getCronjobsInCluster(String clusterId) {
        AbstractCluster cluster = clusterFacade.getCluster(clusterId);
        Optional<K8sCredentials> credentialsO = k8sCredentialsRepository.findOneByClusterId(clusterId);
        K8sCredentials k8sCredentials = credentialsO.get();
        KubernetesClient kubernetesClient = getKubernetesClient(k8sCredentials);
        CronJobList cronJobList = kubernetesClient.batch().cronjobs().inNamespace("default").list();

        return cronJobList.getItems();
    }

    private CronJob getCronjobsInClusterWithName(String clusterId, String cronjobName) {
        AbstractCluster cluster = clusterFacade.getCluster(clusterId);
        KubernetesClient kubernetesClient = getKubernetesClientForCluster(clusterId);
        List<CronJob> cronJobList = kubernetesClient.batch()
                .cronjobs()
                .inNamespace("default")
                .list()
                .getItems();

        for (CronJob cronjob : cronJobList) {
            if (cronjob.getMetadata().getName().equals(cronjobName)) {
                return cronjob;
            }
        }

        return null;
    }

    private Job getJobInClusterWithName(String clusterId, String jobName) {
        AbstractCluster cluster = clusterFacade.getCluster(clusterId);
        KubernetesClient kubernetesClient = getKubernetesClientForCluster(clusterId);
        List<Job> cronJobList = kubernetesClient.batch()
                .jobs()
                .inNamespace("default")
                .list()
                .getItems();

        for (Job job : cronJobList) {
            if (job.getMetadata().getName().equals(jobName)) {
                return job;
            }
        }

        return null;
    }

    private KubernetesClient getKubernetesClient(K8sCredentials k8sCredentials) {
        return new DefaultKubernetesClient(new ConfigBuilder().withMasterUrl(k8sCredentials.getKubernetesApiEndpoint())
                .withOauthToken(k8sCredentials.getKubernetesToken()).withTrustCerts(true).build());
    }

    private KubernetesClient getKubernetesClientForCluster(String clusterId) {
        Optional<K8sCredentials> credentialsO = k8sCredentialsRepository.findOneByClusterId(clusterId);
        K8sCredentials k8sCredentials = credentialsO.get();
        return new DefaultKubernetesClient(new ConfigBuilder().withMasterUrl(k8sCredentials.getKubernetesApiEndpoint())
                .withOauthToken(k8sCredentials.getKubernetesToken()).withTrustCerts(true).build());
    }

    private Map<String, K8sJobStatus> filterNotFailedQASuites(Map<String, K8sJobStatus> jobStatusMap) {
        return jobStatusMap.entrySet().parallelStream()
                .filter(entry -> !K8sJobStatus.FAILURE.equals(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     *
     * @param clusterId Id of the kubernetes cluster
     * @param qaSuiteResult final result of QASuite
     */
    synchronized public void validateSanityResult(String clusterId, QASuiteResult qaSuiteResult) throws Exception {
        AbstractCluster cluster = clusterFacade.getCluster(clusterId);
        Optional<QASuiteResult> existingResult = qaSuiteResultRepository.findOneByDeploymentId(qaSuiteResult.getDeploymentId());
        existingResult.ifPresent(suiteResult -> qaSuiteResult.setId(suiteResult.getId()));
        if (shouldValidateSanityResult(qaSuiteResult)) {
            logger.info("validating sanity failure for: {}, for modules: {}", qaSuiteResult.getDeploymentId(),
                    qaSuiteResult.getModuleStatusMap());
            validateSanityFailure(cluster, qaSuiteResult);
        } else {
            qaSuiteResultRepository.save(qaSuiteResult);
        }
    }

    private boolean shouldValidateSanityResult(QASuiteResult qaSuiteResult) {
        List<K8sJobStatus> errorStatuses = Lists.newArrayList(K8sJobStatus.FAILURE, K8sJobStatus.ERROR);
        return errorStatuses.contains(qaSuiteResult.getStatus()) && qaSuiteResult.getDeploymentId() != null;
    }

    private void validateSanityFailure(AbstractCluster cluster, QASuiteResult qaSuiteResult) throws Exception {
        Map<String, K8sJobStatus> notFailedJobs = filterNotFailedQASuites(qaSuiteResult.getModuleStatusMap());
        QASuiteResult copy = qaSuiteResult.withModuleStatusMap(notFailedJobs);
        List<String> automationFailures = getFailedModules(qaSuiteResult);

        logger.info("builds to unpromote: {}", automationFailures);

        try {
            if (qaSuiteResult.isRedeployment()) {
                handleRedeploymentFailure(cluster, copy, automationFailures);
            } else {
                handleNewDeploymentFailure(cluster, copy, automationFailures);
            }
        } catch (Exception ex) {
            logger.error("Error validating sanity results", ex);
            throw new Exception("unknown error happened while validating qa suite result");
        }
    }

    private void deployModulesWithRevertedBuilds(String clusterId, String deploymentId) {
        DeploymentRequest deploymentRequest = new DeploymentRequest();
        deploymentRequest.setTag("test1");
        deploymentRequest.setReleaseType(ReleaseType.RELEASE);
        deploymentRequest.setExtraEnv(ImmutableMap.of("REDEPLOYMENT_BUILD_ID", deploymentId));
        createDeployment(clusterId, deploymentRequest);
    }

    public void handleNewDeploymentFailure(AbstractCluster cluster, QASuiteResult qaSuiteResult, List<String> automationFailures) {
        logger.info("handling new deployment failure for cluster: {}, qa suite: {}", cluster.getId(), qaSuiteResult);

        String clusterId = cluster.getId();
        Map<String, String> failedBuilds = new HashMap<>();
        automationFailures.parallelStream().forEach(moduleName -> {
            logger.info("automation failure, unpormoting build for module: {}", moduleName);

            Deployment application = clusterFacade.getApplicationData(clusterId, "app", moduleName);
            String applicationId = application.getMetadata().getLabels().get("deployerid");
            String deployerBuildId = application.getSpec().getTemplate().getMetadata().getLabels().get("deployerBuildId");
//            buildService.unPromoteBuild(applicationId, deployerBuildId);

            Map<String, Object> logParameters = ImmutableMap.of("applicationId", applicationId,
                    "deployerBuildId", deployerBuildId);
            AuditLog auditLog = new AuditLog("unpromoted build", Instant.now(), "deployer",
                    "sanity failure", logParameters);
            auditLogRepository.save(auditLog);

            qaSuiteResult.getModuleStatusMap().put(moduleName, K8sJobStatus.FAILURE);
            qaSuiteResultRepository.save(qaSuiteResult);
            failedBuilds.put(moduleName, deployerBuildId);
        });


        if (BuildStrategy.PROD.equals(cluster.getReleaseStream())) {
//            deployModulesWithRevertedBuilds(clusterId, qaSuiteResult.getDeploymentId());
        }

        publishSanityFailures(cluster, qaSuiteResult, failedBuilds);
    }

    public void handleRedeploymentFailure(AbstractCluster cluster, QASuiteResult qaSuiteResult, List<String> automationFailures) {
        logger.info("handling redeployment failure for cluster: {}, qa suite: {}", cluster.getId(), qaSuiteResult);

        String clusterId = cluster.getId();
        Map<String, String> failedBuilds = new HashMap<>();
        automationFailures.parallelStream().forEach(moduleName -> {
            logger.info("automation failure, unpormoting build for module: {}", moduleName);

            Deployment application = clusterFacade.getApplicationData(clusterId, "app", moduleName);
            String deployerBuildId = application.getSpec().getTemplate().getMetadata().getLabels().get("deployerBuildId");

            qaSuiteResult.getModuleStatusMap().put(moduleName, K8sJobStatus.FAILURE);
            qaSuiteResultRepository.save(qaSuiteResult);
            failedBuilds.put(moduleName, deployerBuildId);
        });

        publishSanityFailures(cluster, qaSuiteResult, failedBuilds);
    }

    private void publishSanityFailures(AbstractCluster cluster, QASuiteResult qaSuiteResult, Map<String, String> failedBuilds) {
        logger.info("publishing sanity suite results for cluster: {}, deployment: {}, modules: {}", cluster.getName(),
                qaSuiteResult.getDeploymentId(), failedBuilds );
        failedBuilds.forEach((k, v) -> {
            QASuiteModuleResult moduleResult = new QASuiteModuleResult(qaSuiteResult.getId(), k,
                    qaSuiteResult.getDeploymentId(), K8sJobStatus.FAILURE);
            QASanityNotification qaSanityNotification = new QASanityNotification(moduleResult, cluster);
            notificationService.publish(qaSanityNotification);
        });
    }

    private String constructSanityFailureNotificationMessage(AbstractCluster cluster, Map<String, String> failedBuilds) {
        if (failedBuilds.size() == 0) {
            return null;
        }

        StringJoiner moduleJoiner = new StringJoiner("\n");
        moduleJoiner.add(String.format("%s (%s) - Sanity FAILED for the following modules", cluster.getName(), cluster.getId()));
        failedBuilds.forEach((moduleName, buildId) -> {
            moduleJoiner.add(String.format("%s: %s", moduleName, buildId));
        });

        return moduleJoiner.toString();
    }

    private String unpromoteBuild(String clusterId, String moduleName) {
        Deployment application = clusterFacade.getApplicationData(clusterId, "app", moduleName);
        String deployerId = application.getMetadata().getLabels().get("deployerid");
        String deployerBuildId = application.getSpec().getTemplate().getMetadata().getLabels().get("deployerBuildId");
//        buildService.unPromoteBuild(deployerId, deployerBuildId);
        return deployerBuildId;
    }

    private List<String> getFailedModules(QASuiteResult qaSuiteResult) {
        Map<String, K8sJobStatus> failedAutomationSuites = getPreviouslyFailedModulesForDeployment(qaSuiteResult.getDeploymentId());
        return qaSuiteResult.getModuleStatusMap().entrySet().stream()
                .filter(m -> (K8sJobStatus.FAILURE.equals(m.getValue())))
                .filter(m -> !failedAutomationSuites.containsKey(m.getKey()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private Map<String, K8sJobStatus> getPreviouslyFailedModulesForDeployment(String deploymentId) {
        Map<String, K8sJobStatus> failedAutomationSuites = new HashMap<>();
        Optional<QASuiteResult> existingResult = qaSuiteResultRepository.findOneByDeploymentId(deploymentId);
        if (existingResult.isPresent()) {
            failedAutomationSuites = existingResult.get().getModuleStatusMap().entrySet().stream()
                    .filter(e -> K8sJobStatus.FAILURE.equals(e.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        return failedAutomationSuites;
    }

    public ListDeploymentsWrapper getAllDeployments(String clusterId) {
        AbstractCluster cluster = clusterFacade.getCluster(clusterId);
        Stack stack = stackFacade.getStackByName(cluster.getStackName());
        stack.setAppPassword(null);
        List<AbstractCluster> allClusters = clusterFacade.getClustersByStackName(cluster.getStackName());
        List<String> downStreamClusters = allClusters.stream()
                .filter(x -> cluster.getId().equalsIgnoreCase(x.getCdPipelineParent()) && x.getRequireSignOff() == true)
                .map(x -> x.getName())
                .collect(Collectors.toList());
        List<DeploymentLog> deployments = deploymentLogRepository.findFirst50ByClusterIdOrderByCreatedOnDesc(clusterId);
        deployments = deployments.stream()
                .map(x -> tfBuildService.loadDeploymentStatus(x, false)).collect(Collectors.toList());
        Optional<DeploymentLog> signedOffDeploymentOptional =
                deploymentLogRepository.findFirstByClusterIdAndStatusAndDeploymentTypeAndSignedOffOrderByCreatedOnDesc(
                        cluster.getId(), StatusType.SUCCEEDED, DeploymentLog.DeploymentType.REGULAR, true);
        return new ListDeploymentsWrapper(stack, clusterId, deployments, downStreamClusters,
                signedOffDeploymentOptional.orElse(null));
    }

    public DeploymentLog getDeployment(String deploymentId) {
        DeploymentLog deployment = deploymentLogRepository.findById(deploymentId).get();
        return tfBuildService.loadDeploymentStatus(deployment, true);
    }

    public DeploymentContext getDeploymentContext(String clusterId, DeploymentRequest deploymentRequest) {
        AbstractCluster cluster = clusterFacade.getCluster(clusterId);
        Map<String, Map<String, Artifact>> allArtifacts = artifactFacade.getAllArtifacts(cluster.getReleaseStream(), deploymentRequest.getReleaseType());
        Map<String, Map<String, SnapshotInfo>> pinnedSnapshots = baseDRService.getAllPinnedSnapshots(cluster.getId())
                .stream().collect(Collectors.groupingBy(x -> x.getResourceType())).entrySet().stream()
                .collect(Collectors.toMap(x -> x.getKey(),
                        x -> x.getValue().stream().collect(
                                Collectors.toMap((SnapshotInfo y) -> y.getInstanceName(), (SnapshotInfo y) -> y))));
        List<OverrideObject> overrides = overrideObjectRepository.findAllByClusterId(cluster.getId());
        return new DeploymentContext(cluster, allArtifacts, overrides, pinnedSnapshots,
                deploymentRequest.getExtraEnv());
    }

    public void handleCodeBuildCallback(CodeBuildStatusCallback callback) {
        Optional<DeploymentLog> deploymentLogOptional =
                deploymentLogRepository.findOneByCodebuildId(callback.getCodebuidId());
        if(!deploymentLogOptional.isPresent()) {
            return;
        }
        DeploymentLog deploymentLog = deploymentLogOptional.get();
        AbstractCluster cluster = clusterFacade.getCluster(deploymentLog.getClusterId());
        deploymentLog = tfBuildService.loadDeploymentStatus(deploymentLog, true);

        if(deploymentLog.getAppDeployments() != null && !deploymentLog.getAppDeployments().isEmpty()) {
            deploymentLog.getAppDeployments().stream().forEach(
                    x -> notificationService.publish(new ApplicationDeploymentNotification(x, cluster)));
        }
    }

    public DeploymentLog signOff(String clusterId, String deploymentId) {
        DeploymentLog deploymentLog = deploymentLogRepository.findById(deploymentId).get();
        if(deploymentLog.getStatus().equals(StatusType.SUCCEEDED)) {
            deploymentLog.setSignedOff(true);
            return deploymentLogRepository.save(deploymentLog);
        } else {
            throw new RuntimeException("Cannot signoff failed deployment");
        }
    }
}
