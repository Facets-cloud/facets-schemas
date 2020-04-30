package com.capillary.ops.deployer.controller;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.bo.actions.ActionExecution;
import com.capillary.ops.deployer.bo.actions.ApplicationAction;
import com.capillary.ops.deployer.bo.webhook.bitbucket.BitbucketPREvent;
import com.capillary.ops.deployer.bo.webhook.github.GithubPREvent;
import com.capillary.ops.deployer.service.OAuth2UserServiceImpl;
import com.capillary.ops.deployer.service.facade.ApplicationFacade;
import com.capillary.ops.deployer.service.facade.UserFacade;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static com.capillary.ops.deployer.bo.Application.*;


@RestController
@RequestMapping("api")
public class ApplicationController {

    @Autowired
    private ApplicationFacade applicationFacade;

    @Autowired
    private UserFacade userFacade;

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', #applicationFamily + '_' + #environment + '_' + 'MODERATOR')")
    @PostMapping(value = "/{applicationFamily}/applications", produces = "application/json")
    public Application createApplication(@Valid @RequestBody  Application application,
                                         @PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                         @ApiParam(value = "Host", hidden = true) @RequestHeader("Host") String host) {
        application.setApplicationFamily(applicationFamily);
        return applicationFacade.createApplication(application, host);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', #applicationFamily + '_' + #environment + '_' + 'MODERATOR')")
    @PutMapping(value = "/{applicationFamily}/applications", produces = "application/json")
    public Application updateApplication(@Valid @RequestBody  Application application,
                                         @PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                         @ApiParam(value = "Host", hidden = true) @RequestHeader("Host") String host) {
        application.setApplicationFamily(applicationFamily);
        return applicationFacade.updateApplication(application, host);
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

    @PostMapping(value = "/{applicationFamily}/applications/{applicationId}/webhooks/pr/github", produces = "application/json")
    public ResponseEntity<Object> processWebhookPRGithub(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                         @PathVariable("applicationId") String applicationId,
                                                         @RequestBody GithubPREvent webhook,
                                                         @RequestHeader("Host") String host) {
        boolean buildTriggered = applicationFacade.processWebhookPRGithub(applicationFamily, applicationId, webhook, host);
        BodyBuilder responseBuilder = buildTriggered ? ResponseEntity.ok() : ResponseEntity.badRequest();
        return responseBuilder.build();
    }

    @PostMapping(value = "/{applicationFamily}/applications/{applicationId}/webhooks/pr/bitbucket", produces = "application/json")
    public ResponseEntity<Object> processWebhookPRBitbucket(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                            @PathVariable("applicationId") String applicationId,
                                                            @RequestBody BitbucketPREvent webhook,
                                                            @RequestHeader("X-Event-Key") String eventKey,
                                                            @RequestHeader("Host") String host) {
        boolean buildTriggered = applicationFacade.processWebhookPRBitbucket(applicationFamily, applicationId, webhook,
                eventKey, host);
        BodyBuilder responseBuilder = buildTriggered ? ResponseEntity.ok() : ResponseEntity.badRequest();
        return responseBuilder.build();
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
        Build build = applicationFacade.getBuild(applicationFamily, applicationId, buildId);
        build.setApplicationFamily(applicationFamily);
        return build;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROMOTERS')")
    @PutMapping(value = "/{applicationFamily}/applications/{applicationId}/builds/{buildId}", produces = "application/json")
    public Build updateBuild(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                          @PathVariable("applicationId") String applicationId, @PathVariable String buildId, @RequestBody Build build) {
        build.setApplicationId(applicationId);
        build.setApplicationFamily(applicationFamily);
        return applicationFacade.updateBuild(applicationFamily, applicationId, buildId, build);
    }

    @GetMapping(value = "/{applicationFamily}/applications/{applicationId}/branches", produces = "application/json")
    public List<String> getApplicationBranches(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                       @PathVariable("applicationId") String applicationId) throws IOException {
        return applicationFacade.getApplicationBranches(applicationFamily, applicationId);
    }

    @GetMapping(value = "/{applicationFamily}/applications/{applicationId}/tags", produces = "application/json")
    public List<String> getApplicationTags(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                               @PathVariable("applicationId") String applicationId) throws IOException {
        return applicationFacade.getApplicationTags(applicationFamily, applicationId);
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

    @PreAuthorize("hasAnyRole('ADMIN', 'DEPLOYERS', #applicationFamily + '_' + #environment + '_' + 'DEPLOYERS')")
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

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER_ADMIN')")
    @PostMapping(value = "/users", produces = "application/json")
    public User createUser(@RequestBody  User user) {
        return userFacade.createUser(user);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER_ADMIN')")
    @PutMapping(value = "/users/{userId}", produces = "application/json")
    public User updateUser(@RequestBody User user, @PathVariable("userId") String userId) {
        user.setId(userId);
        return userFacade.createUser(user);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER_ADMIN')")
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

    @GetMapping(value = "/{applicationFamily}/{environment}/applications/{applicationId}/podDetails", produces = "application/json")
    public List<ApplicationPodDetails> getApplicationPodDetails(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                      @PathVariable("environment") String environment,
                                      @PathVariable("applicationId") String applicationId) {
        return applicationFacade.getApplicationPodDetails(applicationFamily, environment, applicationId);
    }

    @PostMapping(value = "/{applicationFamily}/{environment}/applications/{applicationId}/pods/{podName}/actions/executeAction",
            produces = "application/json")
    public ActionExecution executeActionOnPod(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                              @PathVariable("environment") String environment,
                                              @PathVariable("applicationId") String applicationId,
                                              @PathVariable("podName") String podName,
                                              @RequestBody ApplicationAction applicationAction) {
        return applicationFacade.executeActionOnPod(applicationFamily, environment, applicationId, podName, applicationAction);
    }

    @GetMapping(value = "/{applicationFamily}/applications/{applicationId}/executedActions", produces = "application/json")
    public List<ActionExecution> getExecutedActionsForApplication(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                                  @PathVariable("applicationId") String applicationId) {
        return applicationFacade.getExecutedActionsForApplication(applicationFamily, applicationId);
    }

    @GetMapping(value = "/{applicationFamily}/{environment}/applications/{applicationId}/pods/{podName}/actions",
            produces = "application/json")
    public List<ApplicationAction> getActionsForPod(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                    @PathVariable("environment") String environment,
                                                    @PathVariable("applicationId") String applicationId,
                                                    @PathVariable("podName") String podName) {
        return applicationFacade.getActionsForPod(applicationFamily, environment, applicationId, podName);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/buildType/{buildType}/actions", produces = "application/json")
    public ApplicationAction createGenericAction(@PathVariable("buildType") BuildType buildType,
                                                 @RequestBody ApplicationAction applicationAction) {
        return applicationFacade.createGenericAction(buildType, applicationAction);
    }

    @GetMapping(value = "/{applicationFamily}/{environment}/applications/{applicationId}/dumps", produces = "application/json")
    public ResponseEntity<Map<String, String>> getDumpFileList(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                               @PathVariable("environment") String environment,
                                                               @PathVariable String applicationId,
                                                               @RequestParam(required = false) String date) {
        if (date != null && !applicationFacade.isDateValid(date)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(applicationFacade.listDumpFilesFromS3(applicationFamily, environment, applicationId, date), HttpStatus.OK);
    }

    @GetMapping(value = "/{applicationFamily}/{environment}/applications/{applicationId}/dumps/download")
    public ResponseEntity<InputStreamResource> downloadDumpFile(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                   @PathVariable("environment") String environment,
                                                   @RequestParam("path") String path,
                                                   @PathVariable String applicationId) {
        S3DumpFile dumpFileFromS3 = applicationFacade.downloadDumpFileFromS3(path);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(dumpFileFromS3.getContentLength());
        String fileName = String.join("_", path.split("/"));
        headers.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(new InputStreamResource(dumpFileFromS3.getInputStream()), headers, HttpStatus.OK);
    }

    @GetMapping(value = "/{applicationFamily}/applications/{applicationId}/builds/{buildId}/downloadArtifacts")
    public ResponseEntity<InputStreamResource> downloadTestReport(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                                  @PathVariable String applicationId,
                                                                  @PathVariable String buildId) {
        S3DumpFile dumpFileFromS3 = applicationFacade.downloadTestReport(applicationFamily, applicationId, buildId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(dumpFileFromS3.getContentLength());
        StringJoiner path = new StringJoiner("/")
                .add(dumpFileFromS3.getApplicationName() == null ? applicationId : dumpFileFromS3.getApplicationName())
                .add(buildId)
                .add(".zip");
        headers.setContentDispositionFormData("attachment", path.toString());

        return new ResponseEntity<>(new InputStreamResource(dumpFileFromS3.getInputStream()), headers, HttpStatus.OK);
    }

    @GetMapping(value = "/applicationFamilies", produces = "application/json")
    public ResponseEntity<List<ApplicationFamily>> getApplicationFamilies() {
        return new ResponseEntity<>(Arrays.asList(ApplicationFamily.values()), HttpStatus.OK);
    }

    @GetMapping(value = "/applicationTypes", produces = "application/json")
    public ResponseEntity<List<ApplicationType>> getApplicationTypes() {
        return new ResponseEntity<>(Arrays.asList(ApplicationType.values()), HttpStatus.OK);
    }

    @JsonView(UserView.SecretName.class)
    @PostMapping("/{applicationFamily}/applications/{applicationId}/secretRequests")
    public List<ApplicationSecretRequest> createAppSecretRequest(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                                @PathVariable("applicationId") String applicationId,
                                                                @RequestBody List<ApplicationSecretRequest> applicationSecretRequests) {
        return applicationFacade.createApplicaitonSecretRequest(applicationFamily, applicationId, applicationSecretRequests);
    }

    @GetMapping("/{applicationFamily}/applications/{applicationId}/secretRequests")
    public List<ApplicationSecretRequest> getApplicationSecretRequests(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                            @PathVariable("applicationId") String applicationId) {
        return applicationFacade.getApplicaitonSecretRequests(applicationFamily, applicationId);
    }


    @JsonView(UserView.SecretName.class)
    @GetMapping("/{applicationFamily}/{environment}/applications/{applicationId}/secretRequests")
    public List<ApplicationSecret> getApplicationSecrets(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                                       @PathVariable("environment") String environment,
                                                                       @PathVariable("applicationId") String applicationId) {
        return applicationFacade.getApplicationSecrets(environment, applicationFamily, applicationId);
    }

    @JsonView(UserView.SecretName.class)
    @PutMapping("/{applicationFamily}/{environment}/applications/{applicationId}/secrets")
    public List<ApplicationSecret> updateApplicationSecrets(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                            @PathVariable("environment") String environment,
                                                            @PathVariable("applicationId") String applicationId,
                                                            @RequestBody List<ApplicationSecret> applicationSecrets) {
        return applicationFacade.updateApplicaitonSecrets(environment, applicationFamily, applicationId, applicationSecrets);
    }

    @JsonView(UserView.SecretName.class)
    @DeleteMapping("/{applicationFamily}/{environment}/applications/{applicationId}/secrets/{secretName}")
    public boolean deleteApplicationSecret(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                                            @PathVariable("environment") String environment,
                                                            @PathVariable("applicationId") String applicationId,
                                                            @PathVariable("secretName") String secretName) {
        return applicationFacade.deleteApplicaitonSecret(environment, applicationFamily, applicationId, secretName);
    }

    @GetMapping("/{applicationFamily}/environmentMetaData")
    public ResponseEntity<List<EnvironmentMetaData>> getEnvironmentMetaData(
            @PathVariable("applicationFamily") ApplicationFamily applicationFamily) throws FileNotFoundException {
        return new ResponseEntity<>(applicationFacade.getEnvironmentMetaData(applicationFamily), HttpStatus.OK);
    }

    @GetMapping("cc/{applicationFamily}/environmentMetaData")
    public ResponseEntity<List<EnvironmentMetaData>> getCCEnvironmentMetaData(
        @PathVariable("applicationFamily") ApplicationFamily applicationFamily) throws FileNotFoundException {
        return new ResponseEntity<>(applicationFacade.getEnvironmentMetaDataCC(applicationFamily), HttpStatus.OK);
    }

    @GetMapping(value = "/{applicationFamily}/{environment}/applications/{applicationId}/deployment/current", produces = "application/json")
    public Deployment getCurrentDeployment(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                 @PathVariable("applicationId") String applicationId, @PathVariable("environment") String environment) {
        return applicationFacade.getCurrentDeployment(applicationFamily, applicationId, environment);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/{applicationFamily}/environments")
    public ResponseEntity<List<Environment>> getEnvironments(@PathVariable ApplicationFamily applicationFamily) {
        List<Environment> result = applicationFacade.getEnvironments(applicationFamily);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/{applicationFamily}/environments/{id}")
    public ResponseEntity<Environment> getEnvironment(@PathVariable ApplicationFamily applicationFamily,
                                                       @PathVariable String id) {
        Environment result = applicationFacade.getEnvironment(applicationFamily, id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{applicationFamily}/environments")
    public ResponseEntity<Environment> upsertEnvironment(@RequestBody Environment environment,
                                                         @PathVariable ApplicationFamily applicationFamily) {
        environment.getEnvironmentMetaData().setApplicationFamily(applicationFamily);
        Environment result = applicationFacade.upsertEnvironment(applicationFamily, environment);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/me")
    public OAuth2UserServiceImpl.SimpleOauth2User me() {
        return (OAuth2UserServiceImpl.SimpleOauth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping(value = "/stats")
    public GlobalStats globalStats() {
        return applicationFacade.getGlobalStats();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', #applicationFamily + '_' + #environment + '_' + 'MODERATOR')")
    @DeleteMapping(value = "/{applicationFamily}/applications/{applicationId}", produces = "application/json")
    public boolean deleteApplication(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                     @PathVariable("applicationId") String applicationId) {
        return applicationFacade.deleteApplication(applicationFamily, applicationId);
    }

    @RolesAllowed("ADMIN")
    @PostMapping(value = "/{applicationFamily}/{environment}/applications/{applicationId}/monitoring", produces = "application/json")
    public boolean enableMonitoring(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                         @PathVariable("applicationId") String applicationId,
                                         @PathVariable("environment") String environment) {
        return applicationFacade.enableNewrelicMonitoring(applicationFamily, applicationId, environment);
    }

    @RolesAllowed("ADMIN")
    @DeleteMapping(value = "/{applicationFamily}/{environment}/applications/{applicationId}/monitoring", produces = "application/json")
    public boolean disableMonitoring(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                         @PathVariable("applicationId") String applicationId,
                                         @PathVariable("environment") String environment) {
        return applicationFacade.disableNewrelicMonitoring(applicationFamily, applicationId, environment);
    }

    @RolesAllowed("ADMIN")
    @PostMapping(value = "/{applicationFamily}/{environment}/applications/{applicationId}/alerting", produces = "application/json")
    public boolean enableAlerting(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                    @PathVariable("applicationId") String applicationId,
                                    @PathVariable("environment") String environment) {
        return applicationFacade.enableNewrelicAlerting(applicationFamily, applicationId, environment);
    }

    @RolesAllowed("ADMIN")
    @DeleteMapping(value = "/{applicationFamily}/{environment}/applications/{applicationId}/alerting", produces = "application/json")
    public boolean disableAlerting(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                     @PathVariable("applicationId") String applicationId,
                                     @PathVariable("environment") String environment) {
        return applicationFacade.disableNewrelicAlerting(applicationFamily, applicationId, environment);
    }

    @RolesAllowed("ADMIN")
    @GetMapping(value = "/{applicationFamily}/{environment}/applications/{applicationId}/monitoring", produces = "application/json")
    public Monitoring getMonitoringDetails(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                     @PathVariable("applicationId") String applicationId,
                                     @PathVariable("environment") String environment) {
        return applicationFacade.getMonitoringDetails(applicationFamily, applicationId, environment);
    }

    @RolesAllowed("ADMIN")
    @GetMapping(value = "/{applicationFamily}/{environment}/applications/{applicationId}/alerting", produces = "application/json")
    public Alerting getAlertingDetails(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                            @PathVariable("applicationId") String applicationId,
                                            @PathVariable("environment") String environment) {
        return applicationFacade.getAlertingDetails(applicationFamily, applicationId, environment);
    }



    @PreAuthorize("hasAnyRole('ADMIN', 'DEPLOYERS', #applicationFamily + '_' + #environment + '_' + 'DEPLOYERS')")
    @PostMapping(value = "/{applicationFamily}/{environment}/applications/{applicationId}/halt", produces = "application/json")
    public boolean haltApplication(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                    @PathVariable("applicationId") String applicationId,
                                    @PathVariable("environment") String environment) {
        return applicationFacade.shutdownApplication(applicationFamily, applicationId, environment);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DEPLOYERS', #applicationFamily + '_' + #environment + '_' + 'DEPLOYERS')")
    @PostMapping(value = "/{applicationFamily}/{environment}/applications/{applicationId}/resume", produces = "application/json")
    public boolean resumeApplication(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                       @PathVariable("applicationId") String applicationId,
                                       @PathVariable("environment") String environment) {
        return applicationFacade.resumeApplication(applicationFamily, applicationId, environment);
    }

    @RolesAllowed("ADMIN")
    @PostMapping(value = "/{applicationFamily}/{environment}/redeployment", produces = "application/json")
    public Map<String, Boolean> redeploy(@PathVariable("applicationFamily") ApplicationFamily applicationFamily,
                                       @PathVariable("environment") String environment) {
        return applicationFacade.redeploy(applicationFamily, environment);
    }


}
