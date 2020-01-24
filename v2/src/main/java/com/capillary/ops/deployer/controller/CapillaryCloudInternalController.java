package com.capillary.ops.deployer.controller;

import com.capillary.ops.deployer.bo.capillaryCloud.K8sCluster;
import com.capillary.ops.deployer.bo.capillaryCloud.VPC;
import com.capillary.ops.deployer.service.capillaryCloud.CapillaryCloudFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/capillarycloud/api")
public class CapillaryCloudInternalController {

    @Autowired
    private CapillaryCloudFacade capillaryCloudFacade;

    @PostMapping("/infraResources/vpcs/{infrastructureResourceName}/instances/{clusterName}")
    public VPC registerVPC(@PathVariable String infrastructureResourceName, @PathVariable String clusterName,
                           @RequestBody  VPC vpc) {
        return capillaryCloudFacade.registerInstance(infrastructureResourceName, clusterName, vpc);
    }

    @GetMapping("/infraResources/vpcs/{infrastructureResourceName}/instances/{clusterName}")
    public VPC getVPC(@PathVariable String infrastructureResourceName, @PathVariable String clusterName) {
        return capillaryCloudFacade.getInstance(infrastructureResourceName, clusterName);
    }

    @DeleteMapping("/infraResources/vpcs/{infrastructureResourceName}/instances/{clusterName}")
    public VPC deleteVPC(@PathVariable String infrastructureResourceName, @PathVariable String clusterName) {
        return capillaryCloudFacade.deleteInstance(infrastructureResourceName, clusterName);
    }

    @PostMapping("/infraResources/k8sClusters/{infrastructureResourceName}/instances/{clusterName}")
    public K8sCluster registerK8sCluster(@PathVariable String infrastructureResourceName, @PathVariable String clusterName,
                                         @RequestBody K8sCluster k8sCluster) {
        return capillaryCloudFacade.registerInstance(infrastructureResourceName, clusterName, k8sCluster);
    }

    @GetMapping("/infraResources/k8sClusters/{infrastructureResourceName}/instances/{clusterName}")
    public K8sCluster getK8sCluster(@PathVariable String infrastructureResourceName, @PathVariable String clusterName) {
        return capillaryCloudFacade.getInstance(infrastructureResourceName, clusterName);
    }

    @DeleteMapping("/infraResources/k8sClusters/{infrastructureResourceName}/instances/{clusterName}")
    public K8sCluster deleteK8sCluster(@PathVariable String infrastructureResourceName, @PathVariable String clusterName) {
        return capillaryCloudFacade.deleteInstance(infrastructureResourceName, clusterName);
    }


}
