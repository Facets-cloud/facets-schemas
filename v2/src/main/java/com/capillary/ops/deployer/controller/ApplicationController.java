package com.capillary.ops.deployer.controller;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.service.facade.ApplicationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @Autowired
    private ApplicationFacade applicationFacade;

    @PostMapping("/v2/applications")
    public Application createApplication(@RequestBody  Application application) {
        return applicationFacade.createApplication(application);
    }

    @PostMapping("/v2/applications/{applicationId}/builds")
    public Build createApplication(@RequestBody Build build) {
        return null;
    }

    @PostMapping("/v2/applications/{applicationId}/deployments")
    public Deployment createApplication(@RequestBody Deployment deployment) {
        return null;
    }

}
