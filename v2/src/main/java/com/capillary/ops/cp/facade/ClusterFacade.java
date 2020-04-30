package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.K8sCredentials;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.requests.ClusterRequest;
import com.capillary.ops.cp.repository.CpClusterRepository;
import com.capillary.ops.cp.repository.K8sCredentialsRepository;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.cp.service.ClusterHelper;
import com.capillary.ops.cp.service.ClusterService;
import com.capillary.ops.cp.service.factory.ClusterServiceFactory;
import com.capillary.ops.deployer.exceptions.InvalidActionException;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.jcabi.aspects.Loggable;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Optional<AbstractCluster> existing =
            cpClusterRepository.findByNameAndStackName(request.getClusterName(), request.getStackName());
        if (existing.isPresent()) {
            throw new InvalidActionException(
                "Existing cluster with name: " + request.getClusterName() + " present for " + "stack:" + request
                    .getStackName());
        }
        ClusterService service = factory.getService(request.getCloud());
        AbstractCluster cluster = service.createCluster(request);
        Map<String, String> secrets = clusterHelper.validateClusterVars(request.getClusterVars(), stack.get());
        cluster.setUserInputVars(secrets);
        //Done: Persist Cluster Object
        //Persist to DB
        return cpClusterRepository.save(cluster);
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
        ClusterService service = factory.getService(request.getCloud());
        AbstractCluster cluster = service.updateCluster(request, existing.get());
        existing.get().getUserInputVars().putAll(request.getClusterVars());
        Map<String, String> secrets = clusterHelper.validateClusterVars(existing.get().getUserInputVars(), stack.get());
        cluster.setUserInputVars(secrets);
        //Done: Persist Cluster Object
        //Persist to DB
        return cpClusterRepository.save(cluster);
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
}
