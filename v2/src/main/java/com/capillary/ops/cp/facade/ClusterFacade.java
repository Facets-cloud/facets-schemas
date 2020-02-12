package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.requests.Cloud;
import com.capillary.ops.cp.bo.requests.ClusterRequest;
import com.capillary.ops.cp.repository.ClusterRepository;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.cp.service.ClusterService;
import com.capillary.ops.cp.service.factory.ClusterServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClusterFacade {

    /**
     * Cloud based Service Implementation
     */
    @Autowired
    private ClusterServiceFactory factory;

    @Autowired
    private ClusterRepository clusterRepository;

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
        ClusterService service = factory.getService(request.getCloud());
        AbstractCluster cluster = service.createCluster(request, stack.get().getName());
        //Done: Persist Cluster Object
        //Persist to DB
        AbstractCluster saved = clusterRepository.save(cluster);
        return saved;
    }

    /**
     * Get Cluster Definition from the DB
     *
     * @param clusterId which clusterId?
     */
    public AbstractCluster getCluster(String clusterId) {
        Optional<AbstractCluster> cluster = clusterRepository.findById(clusterId);
        return cluster.get();
    }
}
