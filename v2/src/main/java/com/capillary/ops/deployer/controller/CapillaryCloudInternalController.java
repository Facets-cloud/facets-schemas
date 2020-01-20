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

    @PostMapping("/infraResources/vpcs/{infrastructureResourceName}/instances/{clusterId}")
    public VPC hello(@PathVariable String infrastructureResourceName, @RequestBody  VPC vpc) {
        return capillaryCloudFacade.registerVPC(infrastructureResourceName, vpc);
    }

    @PostMapping("/infraResources/k8sClusters/{infrastructureResourceName}/instances/{clusterId}")
    public K8sCluster hello(@PathVariable String infrastructureResourceName, @RequestBody K8sCluster k8sCluster) {
        return capillaryCloudFacade.registerK8sCluster(infrastructureResourceName, k8sCluster);
    }

}
