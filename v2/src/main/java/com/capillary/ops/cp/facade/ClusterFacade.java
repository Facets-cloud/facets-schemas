package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.*;
import com.capillary.ops.cp.bo.notifications.AlertNotification;
import com.capillary.ops.cp.bo.requests.ClusterRequest;
import com.capillary.ops.cp.bo.requests.OverrideRequest;
import com.capillary.ops.cp.bo.requests.SilenceAlarmRequest;
import com.capillary.ops.cp.exceptions.UnsupportedComponentVersionException;
import com.capillary.ops.cp.repository.CpClusterRepository;
import com.capillary.ops.cp.repository.K8sCredentialsRepository;
import com.capillary.ops.cp.repository.SnapshotInfoRepository;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.cp.service.*;
import com.capillary.ops.cp.service.factory.ClusterServiceFactory;
import com.capillary.ops.cp.service.factory.DRCloudFactorySelector;
import com.capillary.ops.cp.service.notification.NotificationService;
import com.capillary.ops.deployer.bo.KubeApplicationDetails;
import com.capillary.ops.deployer.exceptions.InvalidActionException;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.utils.DeployerUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jcabi.aspects.Loggable;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetList;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.api.model.batch.CronJobList;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Loggable
@Slf4j
public class ClusterFacade {

    /**
     * Cloud based Service Implementation
     */
    @Autowired
    private ClusterServiceFactory factory;

    @Autowired
    private CpClusterRepository cpClusterRepository;

    @Autowired
    private StackRepository stackRepository;

    @Autowired
    private K8sCredentialsRepository k8sCredentialsRepository;

    @Autowired
    private ClusterHelper clusterHelper;

    @Autowired
    private OverrideService overrideService;

    @Autowired
    private ReleaseScheduleService releaseScheduleService;

    @Autowired
    private DRCloudFactorySelector drCloudFactorySelector;

    @Autowired
    private SnapshotInfoRepository snapshotInfoRepository;

    @Autowired
    private AutoSignoffScheduleService autoSignoffScheduleService;

    @Autowired
    PrometheusService prometheusService;

    @Autowired
    private CCKubernetesService ccKubernetesService;

    @Autowired
    private MetaService metaService;

    @Autowired
    private ClusterTaskService clusterTaskService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TFRunConfigurationsService tfRunConfigurationsService;

    private static final Logger logger = LoggerFactory.getLogger(ClusterFacade.class);

    /**
     * Given a cluster find alerts
     *
     * @param clusterId
     * @return
     */
    public HashMap getAllClusterAlerts(String clusterId) {
        Optional<AbstractCluster> existing = cpClusterRepository.findById(clusterId);
        if (!existing.isPresent()) {
            throw new InvalidActionException("No such cluster with id: " + clusterId);
        }
        String url = clusterHelper.getToolsURL(existing.get());
        String pass = clusterHelper.getToolsPws(existing.get());
        JsonObject allAlerts = prometheusService.getAllAlerts(url, pass);
        return (new Gson()).fromJson(allAlerts, HashMap.class);
    }

    /**
     * Given a cluster find open alerts
     *
     * @param clusterId
     * @return
     */
    public HashMap getOpenClusterAlerts(String clusterId) {
        Optional<AbstractCluster> existing = cpClusterRepository.findById(clusterId);
        if (!existing.isPresent()) {
            throw new InvalidActionException("No such cluster with id: " + clusterId);
        }
        String url = clusterHelper.getToolsURL(existing.get());
        String pass = clusterHelper.getToolsPws(existing.get());
        JsonObject allAlerts = prometheusService.getOpenAlerts(url, pass);

        return (new Gson()).fromJson(allAlerts, HashMap.class);
    }

    public boolean receiveAlerts(String clusterId, AlertManagerPayload.Response response) {
        Optional<AbstractCluster> existing = cpClusterRepository.findById(clusterId);
        if (!existing.isPresent()) {
            throw new InvalidActionException("No such cluster with id: " + clusterId);
        }
        AbstractCluster abstractCluster = existing.get();
        List<AlertManagerPayload.Alert> alerts = response.getAlerts();
        alerts.forEach(alert -> {
            if (alert.getResourceName().equals(AlertManagerPayload.NO_NAME)) {
                logger.warn("Ignoring {} as this is old alert", alert.getLabels().get(AlertManagerPayload.ALERTNAME));
            }
            notificationService.publish(new AlertNotification(abstractCluster, alert));
        });
        return true;
    }

    /**
     * @param clusterId
     * @param request
     * @return
     */
    public HashMap silenceAlert(String clusterId, SilenceAlarmRequest request) {
        Optional<AbstractCluster> existing = cpClusterRepository.findById(clusterId);
        if (!existing.isPresent()) {
            throw new InvalidActionException("No such cluster with id: " + clusterId);
        }
        String url = clusterHelper.getToolsURL(existing.get());
        String pass = clusterHelper.getToolsPws(existing.get());
        String authUserName = DeployerUtil.getAuthUserName();

        JsonObject response = prometheusService.silenceAlert(url, pass, request, authUserName);
        return (new Gson()).fromJson(response, HashMap.class);
    }

    /**
     * Cluster agnostic request to create a new cluster
     *
     * @param request Other request Params
     * @return The newly created cluster
     */
    public AbstractCluster createCluster(ClusterRequest request) {
        //Done: Check if stack exists
        Optional<Stack> stack = stackRepository.findById(request.getStackName());
        if (!stack.isPresent()) {
            throw new RuntimeException("Invalid Stack Specified");
        }

        if (request.getCdPipelineParent() != null && !cpClusterRepository.findById(request.getCdPipelineParent())
                .isPresent()) {
            throw new RuntimeException("Invalid CD parent cluster");
        }

        Optional<AbstractCluster> existing =
                cpClusterRepository.findByNameAndStackName(request.getClusterName(), request.getStackName());
        if (existing.isPresent()) {
            throw new InvalidActionException(
                    "Existing cluster with name: " + request.getClusterName() + " present for " + "stack:" + request
                            .getStackName());
        }
        ClusterService service = factory.getService(request.getCloud());
        AbstractCluster cluster = service.createCluster(request);

        return upsertCommonTasks(request, stack, cluster);
    }

    /**
     * Cluster agnostic request to update a new cluster
     *
     * @param request Other request Params
     * @return The updated created cluster
     */
    public AbstractCluster updateCluster(ClusterRequest request, String clusterId)
            throws UnsupportedComponentVersionException {
        //Done: Check if stack exists
        Optional<Stack> stack = stackRepository.findById(request.getStackName());
        if (!stack.isPresent()) {
            throw new RuntimeException("Invalid Stack Specified");
        }
        Stack stackObj = stack.get();
        Optional<AbstractCluster> existing = cpClusterRepository.findById(clusterId);
        if (!existing.isPresent()) {
            throw new InvalidActionException(
                    "No such cluster with name: " + request.getClusterName() + " present for " + "stack:" + request
                            .getStackName());
        }
        AbstractCluster existingCluster = existing.get();

        if (request.getCdPipelineParent() != null && !cpClusterRepository.findById(request.getCdPipelineParent())
                .isPresent()) {
            throw new RuntimeException("Invalid CD parent cluster");
        }

        ClusterService service = factory.getService(request.getCloud());
        AbstractCluster cluster = service.updateCluster(request, existingCluster);
        Map<String, String> mergedClusterVars = new HashMap<>();
        mergedClusterVars.putAll(existingCluster.getUserInputVars());
        mergedClusterVars.putAll(request.getClusterVars());
        request.setClusterVars(mergedClusterVars);
        return upsertCommonTasks(request, stack, cluster);
    }


    private AbstractCluster upsertCommonTasks(ClusterRequest request, Optional<Stack> stack,
                                              AbstractCluster cluster) {
        Map<String, String> secrets = clusterHelper.validateClusterVars(request.getClusterVars(), stack.get());
        cluster.setUserInputVars(secrets);
        cluster.setSchedules(request.getSchedules());
        cluster.setEnableAutoSignOff(request.getEnableAutoSignOff());
        cluster.setAutoSignOffSchedule(request.getAutoSignOffSchedule());
        AbstractCluster save = cpClusterRepository.save(cluster);
        releaseScheduleService.upsertSchedule(save);
        autoSignoffScheduleService.updateSchedule(save);
        return this.getCluster(save.getId());
    }

    /**
     * Get Cluster Definition from the DB
     *
     * @param clusterId which clusterId?
     */
    public AbstractCluster getCluster(String clusterId) {
        Optional<AbstractCluster> cluster = cpClusterRepository.findById(clusterId);
        if (!cluster.isPresent()) {
            throw new NotFoundException("No such Cluster" + clusterId);
        }
        AbstractCluster clusterObj = cluster.get();
        Optional<Stack> stack = stackRepository.findById(clusterObj.getStackName());
        if (!stack.isPresent()) {
            throw new NotFoundException("Invalid Stack value in Specified Cluster Definition");
        }
        Map<String, String> additionalCommonVars = clusterHelper.getCommonVariables(clusterObj, stack.get());
        Map<String, String> secrets = clusterHelper.getSecrets(clusterObj, stack.get());
        clusterObj.setCommonEnvironmentVariables(additionalCommonVars);
        clusterObj.setSecrets(secrets);

        return clusterObj;
    }

    public List<AbstractCluster> getClustersByStackName(String stackName) {
        if (!stackRepository.findById(stackName).isPresent()) {
            throw new NotFoundException("Stack Not Found: " + stackName);
        }
        return cpClusterRepository.findAllByStackName(stackName);
    }

    public Boolean addClusterK8sCredentials(K8sCredentials request) {
        k8sCredentialsRepository.save(request);
        return true;
    }

    public Deployment getApplicationData(String clusterId, String key, String value) {
        Optional<K8sCredentials> credentialsO = k8sCredentialsRepository.findOneByClusterId(clusterId);
        K8sCredentials k8sCredentials = credentialsO.get();
        DefaultKubernetesClient kubernetesClient = new DefaultKubernetesClient(
                new ConfigBuilder().withMasterUrl(k8sCredentials.getKubernetesApiEndpoint())
                        .withOauthToken(k8sCredentials.getKubernetesToken()).withTrustCerts(true).build());
        DeploymentList apps = kubernetesClient.inNamespace("").apps().deployments().withLabel(key, value).list();
        if (apps.getItems().isEmpty()) {
            throw new NotFoundException(
                    "Application not found in cluster. Cluster,value : " + clusterId + ", " + value);
        }
        return apps.getItems().get(0);
    }

    public KubeApplicationDetails getKubeApplicationDetails(String clusterId, String key, String value) {
        Optional<K8sCredentials> credentialsO = k8sCredentialsRepository.findOneByClusterId(clusterId);
        K8sCredentials k8sCredentials = credentialsO.get();
        DefaultKubernetesClient kubernetesClient = new DefaultKubernetesClient(
                new ConfigBuilder().withMasterUrl(k8sCredentials.getKubernetesApiEndpoint())
                        .withOauthToken(k8sCredentials.getKubernetesToken()).withTrustCerts(true).build());
        DeploymentList apps = kubernetesClient.inNamespace("").apps().deployments().withLabel(key, value).list();
        if (!apps.getItems().isEmpty()) {
            Deployment deployment = apps.getItems().get(0);
            return new KubeApplicationDetails(KubeApplicationDetails.K8sResourceType.DEPLOYMENT,
                    deployment.getSpec().getTemplate().getSpec().getContainers(), deployment.getMetadata());
        }

        logger.info("did not find deployment with key: {}, value: {}, in cluster: {}, checking for statefulsets", key,
                value, clusterId);
        StatefulSetList statefulSets = kubernetesClient.inNamespace("").apps().statefulSets().withLabel(key, value)
                .list();
        if (!statefulSets.getItems().isEmpty()) {
            StatefulSet statefulSet = statefulSets.getItems().get(0);
            return new KubeApplicationDetails(KubeApplicationDetails.K8sResourceType.STATEFULSET,
                    statefulSet.getSpec().getTemplate().getSpec().getContainers(), statefulSet.getMetadata());
        }

        logger.info("did not find statefulset with key: {}, value: {}, in cluster: {}, checking for cronjob", key,
                value, clusterId);
        CronJobList cronJobs = kubernetesClient.inNamespace("").batch().cronjobs().withLabel(key, value).list();
        if (!cronJobs.getItems().isEmpty()) {
            CronJob cronJob = cronJobs.getItems().get(0);
            return new KubeApplicationDetails(KubeApplicationDetails.K8sResourceType.CRONJOB,
                    cronJob.getSpec().getJobTemplate().getSpec().getTemplate().getSpec().getContainers(),
                    cronJob.getMetadata());
        }

        logger.error("Application not found in cluster. Cluster,value : " + clusterId + ", " + value);

        return null;
    }

    /**
     * Should not be called by any controller, written for deployer integration
     *
     * @param clusterId
     * @return
     */
    public Optional<K8sCredentials> getClusterK8sCredentials(String clusterId) {
        Optional<K8sCredentials> credentials = k8sCredentialsRepository.findOneByClusterId(clusterId);
        return credentials;
    }

    public List<OverrideObject> override(String clusterId, List<OverrideRequest> request) {

        List<OverrideObject> saved =
                request.stream().map(req -> overrideService.save(clusterId, req)).collect(Collectors.toList());

        return saved;
    }

    public List<OverrideObject> getOverrides(String clusterId) {
        Optional<AbstractCluster> cluster = cpClusterRepository.findById(clusterId);
        if (!cluster.isPresent()) {
            throw new NotFoundException("No such Cluster" + clusterId);
        }
        List<OverrideObject> allByClusterId = overrideService.findAllByClusterId(clusterId);
        List<OverrideObject> collect = allByClusterId.stream()
                .sorted(Comparator.comparing(
                        overrideObject -> overrideObject.getResourceType() + overrideObject.getResourceName()))
                .collect(Collectors.toList());
        return collect;
    }

    public List<SnapshotInfo> listSnapshots(String clusterId, String resourceType, String instanceName) {
        Optional<AbstractCluster> existingCluster = cpClusterRepository.findById(clusterId);
        if (!existingCluster.isPresent()) {
            throw new NotFoundException("No such cluster: " + clusterId);
        }

        AbstractCluster cluster = existingCluster.get();

        DRCloudFactory drCloudFactory = drCloudFactorySelector.getDRCloudFactory(cluster.getCloud());
        DRCloudService drService = drCloudFactory.getDRService(resourceType);
        List<SnapshotInfo> snapshotInfos = drService.listSnapshots(cluster, instanceName);
        snapshotInfos.sort(Comparator.comparing(SnapshotInfo::getStartTime).reversed());
        return snapshotInfos;
    }

    public SnapshotInfo pinSnapshot(String clusterId, String resourceType, String instanceName,
                                    SnapshotInfo snapshotInfo) {
        Optional<AbstractCluster> existingCluster = cpClusterRepository.findById(clusterId);
        if (!existingCluster.isPresent()) {
            throw new NotFoundException("No such cluster: " + clusterId);
        }

        AbstractCluster cluster = existingCluster.get();

        DRCloudFactory drCloudFactory = drCloudFactorySelector.getDRCloudFactory(cluster.getCloud());
        DRCloudService drService = drCloudFactory.getDRService(resourceType);
        return drService.pinSnapshot(cluster.getId(), resourceType, instanceName, snapshotInfo);
    }

    public SnapshotInfo getPinnedSnapshot(String clusterId, String resourceType, String instanceName) {
        Optional<AbstractCluster> existingCluster = cpClusterRepository.findById(clusterId);
        if (!existingCluster.isPresent()) {
            throw new NotFoundException("No such cluster: " + clusterId);
        }

        AbstractCluster cluster = existingCluster.get();

        DRCloudFactory drCloudFactory = drCloudFactorySelector.getDRCloudFactory(cluster.getCloud());
        DRCloudService drService = drCloudFactory.getDRService(resourceType);
        return drService.getPinnedSnapshot(cluster.getId(), resourceType, instanceName);
    }

    public boolean createSnapshot(String clusterId, String resourceType, String instanceName) {
        Optional<AbstractCluster> existingCluster = cpClusterRepository.findById(clusterId);
        if (!existingCluster.isPresent()) {
            throw new NotFoundException("No such cluster: " + clusterId);
        }

        AbstractCluster cluster = existingCluster.get();

        DRCloudFactory drCloudFactory = drCloudFactorySelector.getDRCloudFactory(cluster.getCloud());
        DRCloudService drService = drCloudFactory.getDRService(resourceType);
        return drService.createSnapshot(cluster, resourceType, instanceName);
    }

    public List<OverrideObject> deleteOverride(String clusterId, String resourceType, String resourceName) {
        Optional<AbstractCluster> cluster = cpClusterRepository.findById(clusterId);
        if (!cluster.isPresent()) {
            throw new NotFoundException("No such Cluster" + clusterId);
        }

        return overrideService.delete(clusterId, resourceType, resourceName);
    }

    public String getKubeConfig(String clusterId) {
        return ccKubernetesService.createUserServiceAccount(clusterId);
    }

    public boolean refreshKubernetesSARoles(String clusterId) {
        ccKubernetesService.detachExpiredClusterRoles(clusterId);
        ccKubernetesService.detachExpiredRoles(clusterId);
        ccKubernetesService.attachRoles(clusterId);
        return true;
    }

    public ClusterTask getQueuedClusterTaskForClusterId(String clusterId) {
        return clusterTaskService.getQueuedClusterTaskForClusterId(clusterId);
    }

    public ClusterTask disableClusterTask(String taskId) throws Exception {
        return clusterTaskService.disableClusterTask(taskId);
    }

    public ClusterTask enableClusterTask(String taskId) throws Exception {
        return clusterTaskService.enableClusterTask(taskId);
    }

    public TFRunConfigurations getTFRunConfigurations(String clusterId) {
        return tfRunConfigurationsService.getTFRunConfigurations(clusterId).orElseThrow(() -> new NotFoundException("No TF details found for the cluster id " + clusterId));
    }

    public TFRunConfigurations createTFDetails(TFRunConfigurations tfRunConfigurations, String clusterId) {
        return tfRunConfigurationsService.createTFRunConfigurations(tfRunConfigurations, clusterId);
    }

    public TFRunConfigurations updateTFDetails(TFRunConfigurations tfRunConfigurations, String clusterId) {
        return tfRunConfigurationsService.updateTFRunConfigurations(tfRunConfigurations, clusterId);
    }

    public TFRunConfigurations deleteTFDetails(String clusterId) {
        return tfRunConfigurationsService.deleteTFRunConfigurations(clusterId);
    }
}
