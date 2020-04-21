package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.requests.AwsClusterRequest;
import com.capillary.ops.cp.facade.ClusterFacade;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * All calls which can be made by the "Cluster Managers"
 */
@RestController
@RequestMapping("cc/v1/aws/clusters")
public class AwsClusterController implements ClusterController<AwsCluster, AwsClusterRequest> {

    @Autowired
    ClusterFacade clusterFacade;

    /**
     * Request to create a cluster of a particular Stack
     *
     * @param request Request objects containing essential answers to create a cluster
     * @return Created Cluster Object
     */
    @Override
    @PostMapping()
    public AwsCluster createCluster(@RequestBody AwsClusterRequest request) {
        return (AwsCluster) clusterFacade.createCluster(request);
    }

    @PutMapping("{clusterId}")
    public AwsCluster createCluster(@RequestBody AwsClusterRequest request, @PathVariable String clusterId) {
        return (AwsCluster) clusterFacade.updateCluster(request, clusterId);
    }

    /**
     * Get Cluster Details
     *
     * @param clusterId Id of the cluster
     * @return Cluster Object
     */
    @Override
    @GetMapping("{clusterId}")
    public AwsCluster getCluster(@PathVariable String clusterId) {
        AbstractCluster cluster = clusterFacade.getCluster(clusterId);
        if (cluster instanceof AwsCluster) {
            return (AwsCluster) cluster;
        }
        throw new NotFoundException("This Cluster is not defined in AWS");
    }

}
