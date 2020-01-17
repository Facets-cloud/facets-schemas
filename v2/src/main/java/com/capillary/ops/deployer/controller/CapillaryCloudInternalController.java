package com.capillary.ops.deployer.controller;

import com.capillary.ops.deployer.bo.capillaryCloud.K8sCluster;
import com.capillary.ops.deployer.bo.capillaryCloud.VPC;
import com.capillary.ops.deployer.service.capillaryCloud.CapillaryCloudFacade;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/capillarycloud/api")
public class CapillaryCloudInternalController {

    @Autowired
    private CapillaryCloudFacade capillaryCloudFacade;

    @PostMapping("/infraResources/vpcs/{infrastructureResourceId}/instances/{clusterId}")
    public VPC hello(@RequestBody  VPC vpc) {
        return capillaryCloudFacade.registerVPC(vpc);
    }

    @PostMapping("/infraResources/k8sClusters/{infrastructureResourceId}/instances/{clusterId}")
    public K8sCluster hello(@RequestBody K8sCluster k8sCluster) {
        return capillaryCloudFacade.registerK8sCluster(k8sCluster);
    }

}
