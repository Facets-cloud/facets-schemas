package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.requests.AwsClusterRequest;
import com.capillary.ops.cp.controller.AwsClusterController;
import com.capillary.ops.cp.controller.ClusterController;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * All calls which can be made by the "Cluster Managers"
 */
@RestController
@RequestMapping("cc-ui/v1/aws/clusters")
@Loggable
public class UiAwsClusterController implements ClusterController<AwsCluster, AwsClusterRequest> {

    @Autowired
    AwsClusterController awsClusterController;

    /**
     * Request to create a cluster of a particular Stack
     *
     * @param request Request objects containing essential answers to create a cluster
     * @return Created Cluster Object
     */
    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping()
    public AwsCluster createCluster(@RequestBody AwsClusterRequest request) {
        return awsClusterController.createCluster(request);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("{clusterId}")
    public AwsCluster updateCluster(@RequestBody AwsClusterRequest request, @PathVariable String clusterId) {
        return awsClusterController.updateCluster(request, clusterId);
    }

    /**
     * Get Cluster Details
     *
     * @param clusterId Id of the cluster
     * @return Cluster Object
     */
    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("{clusterId}")
    public AwsCluster getCluster(@PathVariable String clusterId) {
        return awsClusterController.getCluster(clusterId);
    }

}
