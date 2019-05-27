package com.capillary.ops.deployer.controller;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.service.facade.ApplicationFacade;
import com.capillary.ops.deployer.service.facade.UserFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class ApplicationController {

    @Autowired
    private ApplicationFacade applicationFacade;

    @Autowired
    private UserFacade userFacade;

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @RolesAllowed("ADMIN")
    @PostMapping(value = "/{applicationFamily}/applications", produces = "application/json")
    public Application createApplication(@Valid @RequestBody  Application application,
                                         @PathVariable("applicationFamily") ApplicationFamily applicationFamily) {
        application.setApplicationFamily(applicationFamily);
        return applicationFacade.createApplication(application);
    }

    @GetMapping(value = "/{applicationFamily}/applications", produces = "application/json")
    public List<Application> getApplications(@PathVariable("applicationFamily") ApplicationFamily applicationFamily) {
        return applicationFacade.getApplications(applicationFamily);
    }

    @GetMapping(value = "/{applicationFamily}/applications/{applicationId}", produces = "application/json")
    public Application getApplication(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                            @PathVariable("applicationId") String applicationId) {
        return applicationFacade.getApplication(applicationFamily, applicationId);
    }


    @RolesAllowed({"BUILDERS", "ADMIN"})
    @PostMapping(value = "/{applicationFamily}/applications/{applicationId}/builds", produces = "application/json")
    public Build build(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                       @PathVariable("applicationId") String applicationId, @Valid @RequestBody Build build) {
        build.setApplicationId(applicationId);
        return applicationFacade.createBuild(applicationFamily, build);
    }

    @GetMapping(value = "/{applicationFamily}/applications/{applicationId}/builds/{buildId}", produces = "application/json")
    public Build getBuild(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                          @PathVariable("applicationId") String applicationId, @PathVariable String buildId) {
        return applicationFacade.getBuild(applicationFamily, applicationId, buildId);
    }

    @GetMapping(value = "/{applicationFamily}/applications/{applicationId}/builds/{buildId}/logs", produces = "application/json")
    public TokenPaginatedResponse<LogEvent> getBuildLogs(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                               @PathVariable("applicationId") String applicationId,
                                               @PathVariable String buildId, @RequestParam Optional<String> nextToken) {
        return applicationFacade.getBuildLogs(applicationFamily, applicationId, buildId, nextToken.orElse(""));
    }

    @GetMapping(value = "/{applicationFamily}/applications/{applicationId}/builds", produces = "application/json")
    public List<Build> getBuilds(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                @PathVariable("applicationId") String applicationId) {
        return applicationFacade.getBuilds(applicationFamily, applicationId);
    }

    @GetMapping(value = "/{applicationFamily}/applications/{applicationId}/images", produces = "application/json")
    public List<String> getImages(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                  @PathVariable("applicationId") String applicationId) {
        return applicationFacade.getImages(applicationFamily, applicationId);
    }

    @PreAuthorize("hasAnyRole('DEPLOYERS', #applicationFamily + '_' + #environment + '_' + 'DEPLOYERS')")
    @PostMapping(value = "/{applicationFamily}/{environment}/applications/{applicationId}/deployments", produces = "application/json")
    public Deployment deploy(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                        @PathVariable("environment") String environment,
                                        @RequestBody Deployment deployment,
                                        @PathVariable("applicationId") String applicationId) {
        deployment.setApplicationId(applicationId);
        if(deployment.isRollbackEnabled()){
            applicationFacade.rollbackDeployment(applicationFamily, environment, applicationId);
            return null;
        }
        return applicationFacade.createDeployment(applicationFamily, environment, applicationId, deployment);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/users", produces = "application/json")
    public User createUser(@RequestBody  User user) {
        return userFacade.createUser(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/users/{userId}", produces = "application/json")
    public User updateUser(@RequestBody User user, @PathVariable("userId") String userId) {
        user.setId(userId);
        return userFacade.createUser(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/users", produces = "application/json")
    public List<User> getUsers() {
        return userFacade.getAllUsers();
    }

    @GetMapping(value = "/{applicationFamily}/{environment}/applications/{applicationId}/deploymentStatus", produces = "application/json")
    public DeploymentStatusDetails getDeploymentStatus(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                      @PathVariable("environment") String environment,
                                      @PathVariable("applicationId") String applicationId) {
        return applicationFacade.getDeploymentStatus(applicationFamily, environment, applicationId);
    }

    @GetMapping(value = "/{applicationFamily}/{environment}/applications/{applicationName}/dumps", produces = "application/json")
    public ResponseEntity<List<String>> getDumpFileList(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                   @PathVariable("environment") String environment,
                                                   @PathVariable String applicationName,
                                                   @RequestParam(required = false) String date) {
        if (date != null && !applicationFacade.isDateValid(date)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(applicationFacade.listDumpFilesFromS3(environment, applicationName, date), HttpStatus.OK);
    }

    @GetMapping(value = "/{applicationFamily}/{environment}/applications/{applicationName}/dumps/download")
    public ResponseEntity<InputStreamResource> downloadDumpFile(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                   @PathVariable("environment") String environment,
                                                   @RequestParam("path") String path,
                                                   @PathVariable String applicationName) {
        S3DumpFile dumpFileFromS3 = applicationFacade.downloadDumpFileFromS3(environment, applicationName, path);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(dumpFileFromS3.getContentLength());
        String fileName = String.join("_", path.split("/"));
        headers.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(new InputStreamResource(dumpFileFromS3.getInputStream()), headers, HttpStatus.OK);
    }

    @GetMapping(value = "/applicationFamilies", produces = "application/json")
    public ResponseEntity<List<ApplicationFamily>> getApplicationFamilies() {
        return new ResponseEntity<>(Arrays.asList(ApplicationFamily.values()), HttpStatus.OK);
    }

    @JsonView(UserView.SecretName.class)
    @PostMapping("/{applicationFamily}/{environment}/applications/{applicationId}/secrets")
    public List<ApplicationSecret> initializeApplicationSecrets(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                                @PathVariable("environment") String environment,
                                                                @PathVariable("applicationId") String applicationId,
                                                                @RequestBody List<ApplicationSecret> applicationSecrets) {
        return applicationFacade.initializeApplicaitonSecrets(environment, applicationFamily, applicationId, applicationSecrets);
    }

    @JsonView(UserView.SecretName.class)
    @GetMapping("/{applicationFamily}/{environment}/applications/{applicationId}/secrets")
    public List<ApplicationSecret> getApplicationSecrets(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                            @PathVariable("environment") String environment,
                                                            @PathVariable("applicationId") String applicationId) {
        return applicationFacade.getApplicaitonSecrets(environment, applicationFamily, applicationId);
    }

    @JsonView(UserView.SecretName.class)
    @PutMapping("/{applicationFamily}/{environment}/applications/{applicationId}/secrets")
    public List<ApplicationSecret> updateApplicationSecrets(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                            @PathVariable("environment") String environment,
                                                            @PathVariable("applicationId") String applicationId,
                                                            @RequestBody List<ApplicationSecret> applicationSecrets) {
        return applicationFacade.updateApplicaitonSecrets(environment, applicationFamily, applicationId, applicationSecrets);
    }
}
