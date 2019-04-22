package com.capillary.ops.deployer.controller;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.service.facade.ApplicationFacade;
import com.capillary.ops.deployer.service.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Arrays;
import java.util.List;

@RestController
public class ApplicationController {

    @Autowired
    private ApplicationFacade applicationFacade;

    @Autowired
    private UserFacade userFacade;

    @RolesAllowed("ADMIN")
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

    @RolesAllowed("BUILDERS")
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

    @PreAuthorize("hasAnyRole('DEPLOYERS', #applicationFamily + '_' + #environment + '_' + 'DEPLOYERS')")
    @PostMapping("/{applicationFamily}/{environment}/applications/{applicationId}/deployments")
    public Deployment deploy(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                        @PathVariable("environment") String environment,
                                        @RequestBody Deployment deployment,
                                        @PathVariable("applicationId") String applicationId) {
        deployment.setApplicationId(applicationId);
        return applicationFacade.createDeployment(applicationFamily, environment, applicationId, deployment);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public User createUser(@RequestBody  User user) {
        return userFacade.createUser(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{userId}")
    public User updateUser(@RequestBody User user, @PathVariable("userId") String userId) {
        user.setId(userId);
        return userFacade.createUser(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<User> getUsers() {
        return userFacade.getAllUsers();
    }

    @GetMapping("/{applicationFamily}/{environment}/applications/{applicationId}/deployments/status")
    public DeploymentStatusDetails getDeploymentStatus(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                      @PathVariable("environment") String environment,
                                      @PathVariable("applicationId") String applicationId) {
        return applicationFacade.getDeploymentStatus(applicationFamily, environment, applicationId);
    }
}
