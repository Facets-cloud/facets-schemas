package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.LocalCluster;
import com.capillary.ops.cp.bo.requests.LocalClusterRequest;
import com.capillary.ops.cp.controller.ClusterController;
import com.capillary.ops.cp.facade.ClusterFacade;
import com.capillary.ops.cp.facade.DeploymentFacade;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * All calls which can be made by the "Cluster Managers"
 */
@RestController
@RequestMapping("cc-ui/v1/local/clusters")
@Loggable
public class UiLocalClusterController implements ClusterController<LocalCluster, LocalClusterRequest> {

    @Autowired
    ClusterFacade clusterFacade;

    @Autowired
    DeploymentFacade deploymentFacade;

    /**
     * Request to create a cluster of a particular Stack
     *
     * @param request Request objects containing essential answers to create a cluster
     * @return Created Cluster Object
     */
    @Override
    @PreAuthorize("hasRole('CC-ADMIN')")
    @PostMapping()
    public LocalCluster createCluster(@RequestBody LocalClusterRequest request) {
        return (LocalCluster) clusterFacade.createCluster(request);
    }


    @Override
    @PreAuthorize("hasRole('CC-ADMIN') or @aclService.hasClusterWriteAccess(authentication, #clusterId)")
    @PutMapping("{clusterId}")
    public LocalCluster updateCluster(@RequestBody LocalClusterRequest request, @PathVariable String clusterId) {
        return (LocalCluster) clusterFacade.updateCluster(request, clusterId);
    }

    /**
     * Get Cluster Details
     *
     * @param clusterId Id of the cluster
     * @return Cluster Object
     */
    @Override
    @GetMapping("{clusterId}")
    public LocalCluster getCluster(@PathVariable String clusterId) {
        AbstractCluster cluster = clusterFacade.getCluster(clusterId);
        if (cluster instanceof LocalCluster) {
            return (LocalCluster) cluster;
        }
        throw new NotFoundException("This Cluster is not defined in Local");
    }

    @GetMapping("{clusterId}/vagrant")
    public String getVagrant(@PathVariable String clusterId) {
        DeploymentLog lastSuccessFullDeployment = deploymentFacade.getLastSuccessfulDeployment(clusterId);
        throw new NullPointerException("Fat gaya");
//        return deploymentFacade.getSignedUrlVagrantArtifact(clusterId, lastSuccessFullDeployment);
    }
}
