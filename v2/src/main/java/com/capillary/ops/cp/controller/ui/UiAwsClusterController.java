package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.App;
import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.requests.AwsClusterRequest;
import com.capillary.ops.cp.controller.AwsClusterController;
import com.capillary.ops.cp.controller.ClusterController;
import com.capillary.ops.cp.service.AclService;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * All calls which can be made by the "Cluster Managers"
 */
@RestController
@RequestMapping("cc-ui/v1/aws/clusters")
@Loggable
public class UiAwsClusterController implements ClusterController<AwsCluster, AwsClusterRequest> {

    @Autowired
    AwsClusterController awsClusterController;

    @Autowired
    private AclService aclService;

    /**
     * Request to create a cluster of a particular Stack
     *
     * @param request Request objects containing essential answers to create a cluster
     * @return Created Cluster Object
     */
    @Override
    @PreAuthorize("hasRole('CC-ADMIN')")
    @PostMapping()
    public AwsCluster createCluster(@RequestBody AwsClusterRequest request) {
        return awsClusterController.createCluster(request);
    }

    @Override
    @PreAuthorize("hasRole('CC-ADMIN') or @aclService.hasClusterWriteAccess(authentication, #clusterId)")
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
    @GetMapping("{clusterId}")
    public AwsCluster getCluster(@PathVariable String clusterId) {
        AwsCluster cluster = awsClusterController.getCluster(clusterId);
        Map<String, String> secrets =
            cluster.getSecrets().keySet().stream().collect(Collectors.toMap(Function.identity(), x -> "****"));
        cluster.setSecrets(secrets);
        return cluster;
    }
}
