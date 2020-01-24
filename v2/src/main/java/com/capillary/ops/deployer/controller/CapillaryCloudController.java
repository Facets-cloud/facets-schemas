package com.capillary.ops.deployer.controller;

import com.capillary.ops.deployer.bo.capillaryCloud.Cluster;
import com.capillary.ops.deployer.bo.capillaryCloud.InfrastructureResource;
import com.capillary.ops.deployer.bo.capillaryCloud.InfrastructureResourceInstances;
import com.capillary.ops.deployer.bo.capillaryCloud.ProcessExecutionResult;
import com.capillary.ops.deployer.service.capillaryCloud.CapillaryCloudFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

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

    @PutMapping("/clusters/{clusterName}")
    public Cluster updateCluster(@PathVariable String clusterName, @RequestBody Cluster cluster) {
        cluster.setId(clusterName);
        capillaryCloudFacade.defineCluster(cluster);
        return cluster;
    }

    @GetMapping("/clusters/{clusterName}/infra/plan")
    public ProcessExecutionResult planCluster(@PathVariable String clusterName) {
        return capillaryCloudFacade.planCluster(clusterName);
    }

    @PostMapping("/clusters/{clusterName}/infra/sync")
    public ProcessExecutionResult executePlan(@PathVariable String clusterName) {
        return capillaryCloudFacade.syncCluster(clusterName);
    }

    @PostMapping("/clusters/{clusterName}/infra/destroy")
    public ProcessExecutionResult destroyCluster(@PathVariable String clusterName) {
        return capillaryCloudFacade.destroyCluster(clusterName);
    }

    @GetMapping("/infraResources")
    public Collection<InfrastructureResource> getAllInfrastructureResources() {
        return capillaryCloudFacade.getInfrastructureResources();
    }

    @GetMapping("/infraResourceInstances/{clusterName}")
    public InfrastructureResourceInstances
    getInfrastructureResourceInstances(@PathVariable String clusterName) {
        return capillaryCloudFacade.getInfrastructureResourceInstances(clusterName);
    }
}
