package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.bo.AzureCluster;
import com.capillary.ops.cp.bo.AzureCluster;
import com.capillary.ops.cp.bo.requests.AzureClusterRequest;
import com.capillary.ops.cp.bo.requests.AzureClusterRequest;
import com.capillary.ops.cp.controller.AzureClusterController;
import com.capillary.ops.cp.controller.AzureClusterController;
import com.capillary.ops.cp.controller.ClusterController;
import com.capillary.ops.cp.service.AclService;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * All calls which can be made by the "Cluster Managers"
 */
@RestController
@RequestMapping("cc-ui/v1/azure/clusters")
@Loggable
public class UiAzureClusterController {

    @Autowired
    AzureClusterController azureClusterController;

    @Autowired
    private AclService aclService;

    /**
     * Request to create a cluster of a particular Stack
     *
     * @param request Request objects containing essential answers to create a cluster
     * @return Created Cluster Object
     */
    @PreAuthorize("hasRole('CC-ADMIN')")
    @PostMapping()
    public AzureCluster createAzureCluster(@RequestBody AzureClusterRequest request) {
        return azureClusterController.createCluster(request);
    }

    @PreAuthorize("hasRole('CC-ADMIN') or @aclService.hasClusterWriteAccess(authentication, #clusterId)")
    @PutMapping("{clusterId}")
    public AzureCluster updateAzureCluster(@RequestBody AzureClusterRequest request, @PathVariable String clusterId) {
        return azureClusterController.updateCluster(request, clusterId);
    }

    /**
     * Get Cluster Details
     *
     * @param clusterId Id of the cluster
     * @return Cluster Object
     */
    @GetMapping("{clusterId}")
    public AzureCluster getAzureCluster(@PathVariable String clusterId) {
        AzureCluster cluster = azureClusterController.getCluster(clusterId);
        Map<String, String> secrets =
            cluster.getSecrets().keySet().stream().collect(Collectors.toMap(Function.identity(), x -> "****"));
        cluster.setSecrets(secrets);
        return cluster;
    }
}
