package com.capillary.ops.deployer.controller;

import com.capillary.ops.deployer.bo.capillaryCloud.Cluster;
import com.capillary.ops.deployer.bo.capillaryCloud.InfrastructureResource;
import com.capillary.ops.deployer.bo.capillaryCloud.ProcessExecutionResult;
import com.capillary.ops.deployer.service.capillaryCloud.CapillaryCloudFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/capillarycloud/api")
public class CapillaryCloudController {

    @Autowired
    private CapillaryCloudFacade capillaryCloudFacade;

    @PostMapping("/clusters")
    public Cluster defineCluster(@RequestBody Cluster cluster) {
        capillaryCloudFacade.defineCluster(cluster);
        return cluster;
    }

    @PutMapping("/clusters/{clusterId}")
    public Cluster updateCluster(@PathVariable String clusterId, @RequestBody Cluster cluster) {
        cluster.setId(clusterId);
        capillaryCloudFacade.defineCluster(cluster);
        return cluster;
    }

    @GetMapping("/clusters/{clusterId}/infra/plan")
    public ProcessExecutionResult planCluster(@PathVariable String clusterId) {
        return capillaryCloudFacade.planCluster(clusterId);
    }

    @PostMapping("/clusters/{clusterId}/infra/sync")
    public ProcessExecutionResult executePlan(@PathVariable String clusterId) {
        return capillaryCloudFacade.syncCluster(clusterId);
    }

    @PostMapping("/clusters/{clusterId}/infra/destroy")
    public ProcessExecutionResult destroyCluster(@PathVariable String clusterId) {
        return capillaryCloudFacade.destroyCluster(clusterId);
    }

    @PostMapping("/infraResources")
    public InfrastructureResource
    createInfrastructureResource(@RequestBody InfrastructureResource infrastructureResource) {
        return capillaryCloudFacade.createInfrastructureResource(infrastructureResource);
    }

}
