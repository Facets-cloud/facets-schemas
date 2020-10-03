package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.CodeBuildStatusCallback;
import com.capillary.ops.cp.facade.DeploymentFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("cc/v1/callbacks")
public class CallbackController {

    @Autowired
    private DeploymentFacade deploymentFacade;

    @PostMapping("/codebuild")
    public Boolean codeBuildCallback(@RequestBody CodeBuildStatusCallback callback) {
        deploymentFacade.handleCodeBuildCallback(callback);
        return true;
    }
}
