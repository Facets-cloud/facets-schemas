package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.facade.DeploymentFacade;
import com.capillary.ops.deployer.exceptions.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cc/v1/clusters/{clusterId}/deployments/")
public class DeploymentController {

    @Autowired
    DeploymentFacade deploymentFacade;

    /**
     * Create a new Deployment for latest definition of this cluster
     *
     * @param clusterId         Cluster Id
     * @param deploymentRequest Request Params for deployments
     * @return The Deployment Log Object
     */
    @PostMapping
    DeploymentLog createDeployment(@PathVariable String clusterId, @RequestBody DeploymentRequest deploymentRequest) {
        return deploymentFacade.createDeployment(clusterId, deploymentRequest);
    }

    /**
     * Get log for the deployment
     *
     * @param id Deployment Id
     * @return List of Logs
     */
    @GetMapping("{id}")
    List<String> getLogs(@PathVariable String clusterId, @PathVariable String id) {
        throw new NotImplementedException("Getting Logs is Not implemented yet");
    }

}
