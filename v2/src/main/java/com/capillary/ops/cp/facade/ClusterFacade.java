package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.*;
import com.capillary.ops.cp.bo.requests.ClusterRequest;
import com.capillary.ops.cp.bo.requests.OverrideRequest;
import com.capillary.ops.cp.repository.CpClusterRepository;
import com.capillary.ops.cp.repository.K8sCredentialsRepository;
import com.capillary.ops.cp.repository.SnapshotInfoRepository;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.cp.service.*;
import com.capillary.ops.cp.service.factory.ClusterServiceFactory;
import com.capillary.ops.cp.service.factory.DRCloudFactorySelector;
import com.capillary.ops.deployer.exceptions.InvalidActionException;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.jcabi.aspects.Loggable;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Loggable
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

    private static final Logger logger = LoggerFactory.getLogger(ClusterFacade.class);

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

        if (request.getCdPipelineParent() != null && ! cpClusterRepository.findById(request.getCdPipelineParent()).isPresent()) {
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
    public AbstractCluster updateCluster(ClusterRequest request, String clusterId) {
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

        if (request.getCdPipelineParent() != null && ! cpClusterRepository.findById(request.getCdPipelineParent()).isPresent()) {
            throw new RuntimeException("Invalid CD parent cluster");
        }

        ClusterService service = factory.getService(request.getCloud());
        AbstractCluster cluster = service.updateCluster(request, existing.get());
        Map<String, String> mergedClusterVars = new HashMap<>();
        mergedClusterVars.putAll(existing.get().getUserInputVars());
        mergedClusterVars.putAll(request.getClusterVars());
        request.setClusterVars(mergedClusterVars);
        return upsertCommonTasks(request, stack, cluster);
    }

    private AbstractCluster upsertCommonTasks(ClusterRequest request, Optional<Stack> stack,
        AbstractCluster cluster) {
        Map<String, String> secrets = clusterHelper.validateClusterVars(request.getClusterVars(), stack.get());
        cluster.setUserInputVars(secrets);
        cluster.setSchedules(request.getSchedules());
        AbstractCluster save = cpClusterRepository.save(cluster);
        releaseScheduleService.upsertSchedule(save);
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
        DeploymentList apps = kubernetesClient.inNamespace("").extensions().deployments().withLabel(key, value).list();
        if (apps.getItems().isEmpty()) {
            throw new NotFoundException(
                "Application not found in cluster. Cluster,value : " + clusterId + ", " + value);
        }
        Deployment deployment = apps.getItems().get(0);
        return deployment;
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
        return overrideService.findAllByClusterId(clusterId);
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
}
