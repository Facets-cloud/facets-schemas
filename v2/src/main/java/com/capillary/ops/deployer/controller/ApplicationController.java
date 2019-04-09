package com.capillary.ops.deployer.controller;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.bo.LogEvent;
import com.capillary.ops.deployer.service.facade.ApplicationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.cloudwatchlogs.model.OutputLogEvent;

import java.util.List;

@RestController
public class ApplicationController {

    @Autowired
    private ApplicationFacade applicationFacade;

    @PostMapping("/applications")
    public Application createApplication(@RequestBody  Application application) {
        return applicationFacade.createApplication(application);
    }

    @GetMapping("/applications")
    public List<Application> getApplications() {
        return applicationFacade.getApplications();
    }

    @PostMapping("/applications/{applicationId}/builds")
    public Build createApplication(@PathVariable("applicationId") String applicationId, @RequestBody Build build) {
        build.setApplicationId(applicationId);
        return applicationFacade.createBuild(build);
    }

    @GetMapping("/applications/{applicationId}/builds/{buildId}")
    public Build getBuild(@PathVariable("applicationId") String applicationId, @PathVariable String buildId) {
        return applicationFacade.getBuild(buildId);
    }

    @GetMapping("/applications/{applicationId}/builds/{buildId}/logs")
    public List<LogEvent> getBuildLogs(@PathVariable("applicationId") String applicationId, @PathVariable String buildId) {
        return applicationFacade.getBuildLogs(buildId);
    }

    @GetMapping("/applications/{applicationId}/builds")
    public List<Build> getBuild(@PathVariable("applicationId") String applicationId) {
        return applicationFacade.getBuilds();
    }

    @PostMapping("/applications/{applicationId}/deployments")
    public Deployment createApplication(@RequestBody Deployment deployment,
                                        @PathVariable("applicationId") String applicationId) {
        deployment.setApplicationId(applicationId);
        return applicationFacade.createDeployment(deployment);
    }

}
