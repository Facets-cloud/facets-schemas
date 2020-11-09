package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.CodeBuildStatusCallback;
import com.capillary.ops.cp.bo.DRResult;
import com.capillary.ops.cp.facade.DeploymentFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cc/v1/callbacks")
public class CapillaryCloudCallbackController {

    @Autowired
    private DeploymentFacade deploymentFacade;

    @PostMapping("/codebuild")
    public Boolean codeBuildCallback(@RequestBody CodeBuildStatusCallback callback) {
        deploymentFacade.handleCodeBuildCallback(callback);
        return true;
    }

    @PostMapping("{cluster}/dr/{moduleType}/{instanceName}")
    public Boolean drResultCallback(@PathVariable String cluster, @PathVariable String moduleType,
                                    @PathVariable String instanceName, @RequestBody DRResult callback) {
        deploymentFacade.handleDRCallback(cluster, moduleType, instanceName, callback);
        return true;
    }
}
