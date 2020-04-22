package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.requests.ClusterRequest;
import com.capillary.ops.cp.repository.CpClusterRepository;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.cp.service.ClusterHelper;
import com.capillary.ops.cp.service.ClusterService;
import com.capillary.ops.cp.service.factory.ClusterServiceFactory;
import com.capillary.ops.deployer.exceptions.InvalidActionException;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
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
        Map<String, String> secrets =
            clusterHelper.validateClusterVars(existing.get().getUserInputVars(), stack.get());
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
}
