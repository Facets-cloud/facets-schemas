package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AzureCluster;
import com.capillary.ops.cp.bo.requests.AzureClusterRequest;
import com.capillary.ops.cp.facade.ClusterFacade;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * All calls which can be made by the "Cluster Managers"
 */
@RestController
@RequestMapping("cc/v1/azure/clusters")
@Loggable
public class AzureClusterController implements ClusterController<AzureCluster, AzureClusterRequest> {

    @Autowired
    ClusterFacade clusterFacade;

    /**
     * Request to create a cluster of a particular Stack
     *
     * @param request Request objects containing essential answers to create a cluster
     * @return Created Cluster Object
     */
    @Override
    @PreAuthorize("hasRole('CC-ADMIN')")
    @PostMapping()
    public AzureCluster createCluster(@RequestBody AzureClusterRequest request) {
        return (AzureCluster) clusterFacade.createCluster(request);
    }


    @Override
    @PreAuthorize("hasRole('CC-ADMIN') or @aclService.hasClusterWriteAccess(authentication, #clusterId)")
    @PutMapping("{clusterId}")
    public AzureCluster updateCluster(@RequestBody AzureClusterRequest request, @PathVariable String clusterId) {
        return (AzureCluster) clusterFacade.updateCluster(request, clusterId);
    }

    /**
     * Get Cluster Details
     *
     * @param clusterId Id of the cluster
     * @return Cluster Object
     */
    @Override
    @GetMapping("{clusterId}")
    public AzureCluster getCluster(@PathVariable String clusterId) {
        AbstractCluster cluster = clusterFacade.getCluster(clusterId);
        if (cluster instanceof AzureCluster) {
            return (AzureCluster) cluster;
        }
        throw new NotFoundException("This Cluster is not defined in azure");
    }
}
