package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.*;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.repository.DeploymentLogRepository;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.repository.K8sCredentialsRepository;
import com.capillary.ops.cp.repository.QASuiteRepository;
import com.capillary.ops.cp.service.BuildService;
import com.capillary.ops.cp.service.TFBuildService;
import com.capillary.ops.deployer.exceptions.NotFoundException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.codebuild.model.EnvironmentVariable;
import software.amazon.awssdk.services.codebuild.model.EnvironmentVariableType;

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
    private BuildService buildService;

    private static final Logger logger = LoggerFactory.getLogger(DeploymentFacade.class);

    /**
     * Create a new Deployment
     *
     * @param clusterId         Id of the Cluster
     * @param deploymentRequest Any Additional Deployment Params
     * @return The Deployment Log Object
     */
    public DeploymentLog createDeployment(String clusterId, DeploymentRequest deploymentRequest) {
        AbstractCluster cluster = clusterFacade.getCluster(clusterId);
        //TODO: Save Deployment requests for audit purpose
        String buildId = tfBuildService.deployLatest(cluster, deploymentRequest);
        DeploymentLog log = new DeploymentLog();
        log.setCodebuildId(buildId);
        log.setClusterId(clusterId);
        log.setDescription(deploymentRequest.getTag());
        log.setReleaseType(deploymentRequest.getReleaseType());
        log.setCreatedOn(new Date());
        deploymentLogRepository.save(log);
        return log;
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

        String uniqueID = UUID.randomUUID().toString();
        qaSuite.setJobReferenceId(uniqueID);
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
            qaSuite.getEnvironmentVariables().forEach((key, value) -> {
                envVarMap.put(key, new EnvVarBuilder().withName(key).withValue(value).build());
            });

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

/*      K8sJobStatus jobStatus = getK8sJobStatus(existingJob);
        if (!K8sJobStatus.RUNNING.equals(jobStatus)) {
            String errorMessage = String.format("could not abort job %s in %s state, only running jobs can be stopped", jobName, jobStatus.name());
            logger.error(errorMessage);
            throw new Exception(errorMessage);
        }*/

        KubernetesClient kubernetesClient = getKubernetesClientForCluster(clusterId);
        kubernetesClient.batch()
                .jobs()
                .inNamespace("default")
                .withName(jobName)
                .delete();
        logger.info(String.format("aborted qa job with name: %s, executionId: %s", module, executionId));
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

    /**
     *
     * @param clusterId Id of the kubernetes cluster
     * @param qaSuiteResult final result of QASuite
     */
    public void validateSanityResult(String clusterId, QASuiteResult qaSuiteResult) throws Exception {
        try {
            if (K8sJobStatus.FAILURE.equals(qaSuiteResult.getStatus())) {
                List<String> automationFailures = qaSuiteResult.getModuleStatusMap().entrySet().stream()
                        .filter(m -> (K8sJobStatus.FAILURE.equals(m.getValue())))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                logger.info("builds to unpromote: {}", automationFailures);

                automationFailures.parallelStream().forEach(moduleName -> {
                    logger.info("automation failure, unpormoting build for module: {}", moduleName);
                    Deployment application = clusterFacade.getApplicationData(clusterId, "app", moduleName);
                    String deployerId = application.getMetadata().getLabels().get("deployerid");
                    String deployerBuildId = application.getMetadata().getLabels().get("deployerBuildId");
                    buildService.unPromoteBuild(deployerId, deployerBuildId);
                });

                DeploymentRequest deploymentRequest = new DeploymentRequest();
                deploymentRequest.setTag("test1");
                deploymentRequest.setReleaseType(ReleaseType.RELEASE);
                deploymentRequest.setExtraEnv(Collections.singletonList(EnvironmentVariable.builder()
                        .name("REDEPLOYMENT_BUILD_ID")
                        .value(qaSuiteResult.getDeploymentId())
                        .type(EnvironmentVariableType.PLAINTEXT)
                        .build()));

                createDeployment(clusterId, deploymentRequest);
            }
        } catch (Exception e) {
            logger.error("Error validating sanity results", e);
            throw new Exception("unknown error happened while validating qa suite result");
        }
    }

    public List<DeploymentLog> getAllDeployments(String clusterId) {
        return deploymentLogRepository.findFirst50ByOrderByCreatedOnDesc();
    }
}
