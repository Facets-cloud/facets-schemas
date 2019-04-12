package com.capillary.ops.deployer.controller;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.service.facade.ApplicationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApplicationController {

    @Autowired
    private ApplicationFacade applicationFacade;

    @PostMapping("/{applicationFamily}/applications")
    public Application createApplication(@RequestBody  Application application,
                                         @PathVariable("applicationFamily") ApplicationFamily applicationFamily) {
        application.setApplicationFamily(applicationFamily);
        return applicationFacade.createApplication(application);
    }

    @GetMapping("/{applicationFamily}/applications")
    public List<Application> getApplications(@PathVariable("applicationFamily") ApplicationFamily applicationFamily) {
        return applicationFacade.getApplications(applicationFamily);
    }

    @PostMapping("/{applicationFamily}/applications/{applicationId}/builds")
    public Build build(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                       @PathVariable("applicationId") String applicationId, @RequestBody Build build) {
        build.setApplicationId(applicationId);
        return applicationFacade.createBuild(applicationFamily, build);
    }

    @GetMapping("/{applicationFamily}/applications/{applicationId}/builds/{buildId}")
    public Build getBuild(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                          @PathVariable("applicationId") String applicationId, @PathVariable String buildId) {
        return applicationFacade.getBuild(applicationFamily, applicationId, buildId);
    }

    @GetMapping("/{applicationFamily}/applications/{applicationId}/builds/{buildId}/logs")
    public List<LogEvent> getBuildLogs(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                       @PathVariable("applicationId") String applicationId,
                                       @PathVariable String buildId) {
        return applicationFacade.getBuildLogs(applicationFamily, applicationId, buildId);
    }

    @GetMapping("/{applicationFamily}/applications/{applicationId}/builds")
    public List<Build> getBuilds(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                @PathVariable("applicationId") String applicationId) {
        return applicationFacade.getBuilds(applicationFamily, applicationId);
    }

    @GetMapping("/{applicationFamily}/applications/{applicationId}/images")
    public List<String> getImages(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                  @PathVariable("applicationId") String applicationId) {
        return applicationFacade.getImages(applicationFamily, applicationId);
    }

    @PostMapping("/{applicationFamily}/{environment}/applications/{applicationId}/deployments")
    public Deployment deploy(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                        @PathVariable("environment") String environment,
                                        @RequestBody Deployment deployment,
                                        @PathVariable("applicationId") String applicationId) {
        deployment.setApplicationId(applicationId);
        return applicationFacade.createDeployment(applicationFamily, environment, applicationId, deployment);
    }
}
