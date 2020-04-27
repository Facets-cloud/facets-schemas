package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.K8sCredentials;
import com.capillary.ops.cp.facade.ClusterFacade;
import com.jcabi.aspects.Loggable;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cc/v1/clusters")
@Loggable
public class CommonClusterController {

    @Autowired
    ClusterFacade clusterFacade;

    @PostMapping("{clusterId}/credentials")
    public Boolean addClusterK8sCredentials(@RequestBody K8sCredentials request, @PathVariable String clusterId) {
        request.setClusterId(clusterId);
        return clusterFacade.addClusterK8sCredentials(request);
    }

    @GetMapping("{clusterId}/deployments/{value}")
    public Deployment getDeploymentInCluster(@PathVariable String clusterId, @PathVariable String value,
        @RequestParam(value = "lookup", defaultValue = "deployerid") String lookupKey) {
        return clusterFacade.getApplicationData(clusterId, lookupKey, value);
    }
}
