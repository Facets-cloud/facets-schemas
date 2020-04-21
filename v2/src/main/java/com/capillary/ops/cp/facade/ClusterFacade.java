package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.requests.ClusterRequest;
import com.capillary.ops.cp.repository.CpClusterRepository;
import com.capillary.ops.cp.repository.StackRepository;
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
        Stack stackObj = stack.get();
        Optional<AbstractCluster> existing =
            cpClusterRepository.findByNameAndStackName(request.getClusterName(), request.getStackName());
        if (existing.isPresent()) {
            throw new InvalidActionException(
                "Existing cluster with name: " + request.getClusterName() + " present for " + "stack:" + request
                    .getStackName());
        }
        ClusterService service = factory.getService(request.getCloud());
        AbstractCluster cluster = service.createCluster(request);
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
        Stack stackObj = stack.get();
        Map<String, String> stackVars = stackObj.getStackVars();
        stackVars.put("CLUSTER", clusterObj.getName());
        stackVars.put("TZ", clusterObj.getTz());
        if (clusterObj instanceof AwsCluster) {
            stackVars.put("AWS_REGION", ((AwsCluster) clusterObj).getAwsRegion());
        }
        clusterObj.addCommonEnvironmentVariables(stackVars);
        return clusterObj;
    }
}
