package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.StackFile;
import com.capillary.ops.cp.bo.requests.AwsClusterRequest;
import com.capillary.ops.cp.controller.AwsClusterController;
import com.capillary.ops.cp.controller.ClusterController;
import com.capillary.ops.cp.facade.StackFacade;
import com.capillary.ops.cp.service.AclService;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    private StackFacade stackFacade;

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
        return hideSecrets(awsClusterController.createCluster(request));
    }

    @Override
    @PreAuthorize("hasRole('CC-ADMIN') or @aclService.hasClusterWriteAccess(authentication, #clusterId)")
    @PutMapping("{clusterId}")
    public AwsCluster updateCluster(@RequestBody AwsClusterRequest request, @PathVariable String clusterId) {
        return hideSecrets(awsClusterController.updateCluster(request, clusterId));
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
        hideSecrets(cluster);
        return cluster;
    }

    public AwsCluster hideSecrets(AwsCluster cluster) {
        String stackName = cluster.getStackName();
        Stack stackByName = stackFacade.getStackByName(stackName);
        Map<String, StackFile.VariableDetails> clusterVariablesMeta = stackByName.getClusterVariablesMeta();
        Map<String, String> secrets = cluster.getSecrets();
        clusterVariablesMeta.forEach(
                (key, cvm) -> {
                    if (cvm.isSecret()) {
                        if (!secrets.containsKey(key) || secrets.get(key)==null || secrets.get(key).isEmpty()) {
                            secrets.put(key, "Not Set");
                        } else if (cvm.getValue().equals(secrets.get(key))) {
                            secrets.put(key, "Stack Default");
                        } else{
                            secrets.put(key, "Set");
                        }
                    }
                }
        );
        cluster.setSecrets(secrets);
        return cluster;
    }
}
