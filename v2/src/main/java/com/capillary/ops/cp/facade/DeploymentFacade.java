package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.K8sCredentials;
import com.capillary.ops.cp.bo.QASuite;
import com.capillary.ops.cp.bo.QASuiteResult;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.repository.K8sCredentialsRepository;
import com.capillary.ops.cp.repository.QASuiteRepository;
import com.capillary.ops.cp.service.BuildService;
import com.capillary.ops.cp.service.CCHelmService;
import com.capillary.ops.cp.service.TFBuildService;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.service.HelmService;
import com.jcabi.aspects.Loggable;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.batch.*;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private CCHelmService ccHelmService;

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
        String buildId = tfBuildService.deployLatest(cluster, deploymentRequest.getReleaseType());
        DeploymentLog log = new DeploymentLog(buildId);
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

        qaSuite = qaSuiteRepository.save(qaSuite);
        try {
            JobTemplateSpec jobTemplate = existingCronjob.getSpec().getJobTemplate();
            Container container = jobTemplate.getSpec().getTemplate().getSpec().getContainers().get(0);

            Map<String, EnvVar> envVarMap =
                    container.getEnv().stream().collect(Collectors.toMap(EnvVar::getName, Function.identity()));
            qaSuite.getEnvironmentVariables().forEach((key, value) -> {
                envVarMap.put(key, new EnvVarBuilder().withName(key).withValue(value).build());
            });

            ObjectMeta metadata = getJobMetadataForQASuite(qaSuite, existingCronjob, jobTemplate);
            container.setEnv(new ArrayList<>(envVarMap.values()));
            Job job = new JobBuilder().withSpec(jobTemplate.getSpec()).withMetadata(metadata).build();

            KubernetesClient kubernetesClient = getKubernetesClientForCluster(clusterId);
            kubernetesClient.batch().jobs().inNamespace("default").create(job);
        } catch (Exception e) {
            logger.error("error happened while triggering qa automation suite", e);
            qaSuiteRepository.delete(qaSuite);
            throw new Exception("Could not trigger automation suite because of unknown error");
        }

        return qaSuite.getId();
    }

    private ObjectMeta getJobMetadataForQASuite(QASuite qaSuite, CronJob existingCronjob, JobTemplateSpec jobTemplate) {
        ObjectMeta metadata = jobTemplate.getMetadata();
        String jobName = existingCronjob.getMetadata().getName() + "-" + qaSuite.getId();
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
    public void abortAutomationSuite(String clusterId, String executionId) {
        AbstractCluster cluster = clusterFacade.getCluster(clusterId);
        QASuite existingQASuite = qaSuiteRepository.findById(executionId).get();
        String module = existingQASuite.getModule();
        String jobName = module + "-" + executionId;
        Job existingJob = getJobInClusterWithName(clusterId, jobName);
        if (existingJob == null) {
            logger.error("could not find active job for module: " + module);
            throw new NotFoundException("Could not find active job for module " + module);
        }

        KubernetesClient kubernetesClient = getKubernetesClientForCluster(clusterId);
        kubernetesClient.batch().jobs().inNamespace("default").withName(jobName).delete();
        logger.info(String.format("aborted qa job with name: %s, executionId: %s", module, executionId));
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
        List<CronJob> cronJobList = kubernetesClient.batch().cronjobs().inNamespace("default").list().getItems();

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
        List<Job> cronJobList = kubernetesClient.batch().jobs().inNamespace("default").list().getItems();

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
     * @param clusterId
     * @param executionId
     * @param qaSuiteResult
     */
    public void validateSanityResult(String clusterId, String executionId, QASuiteResult qaSuiteResult) {
        try {
            if (qaSuiteResult.getStatus().equals("FAIL")) {
                //Unpromote apps
                //Helm rollback
                //Schema/Data of DB rollback?
                //redeployment = true
                List<String> failureList = qaSuiteResult.getModules().entrySet().stream()
                        .filter(m -> (m.equals("FAIL")))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
                for(String module: failureList){
                    ccHelmService.rollback(clusterId,module);
                    buildService.unPromoteBuild(clusterId,"","");
                }
            } else if (qaSuiteResult.getStatus().equals("PASS")) {
                //Notify - all good
            }
        } catch (Exception e) {
            logger.error("Error validating sanity results", e);
        }
    }
}
