package com.capillary.ops.controller;

import com.capillary.ops.bo.codebuild.CodeBuildApplication;
import com.capillary.ops.bo.codebuild.CodeBuildDetails;
import com.capillary.ops.repository.CodeBuildApplicationRepository;
import com.capillary.ops.service.CodeBuildServiceFactory;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CodeBuildController {

  @Autowired CodeBuildServiceFactory codeBuildServiceFactory;

  @Autowired CodeBuildApplicationRepository repository;

  @PostMapping("codebuild/applications")
  public ResponseEntity<CodeBuildApplication> createApplication(
      @RequestBody CodeBuildApplication application) {
    application.setId(repository.save(application).getId());
    CodeBuildApplication resApplication =
        codeBuildServiceFactory
            .getCodeBuildService(application.getApplicationType())
            .createApplication(application);
    return new ResponseEntity<>(
        resApplication, resApplication == null ? HttpStatus.CONFLICT : HttpStatus.CREATED);
  }

  @GetMapping("codebuild/applications/{applicationId}")
  public ResponseEntity<CodeBuildApplication> getApplication(
      @PathVariable(name = "applicationId") String applicationId) {
    CodeBuildApplication application =
        codeBuildServiceFactory
            .getCodeBuildService(repository.findById(applicationId).get().getApplicationType())
            .getApplication(applicationId);
    return new ResponseEntity<>(
        application, application == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
  }

  @PostMapping("codebuild/applications/{applicationId}/build")
  public ResponseEntity<CodeBuildDetails> createBuild(
      @PathVariable(name = "applicationId") String applicationId,
      @RequestBody CodeBuildDetails details) {
    CodeBuildDetails resDetails =
        codeBuildServiceFactory
            .getCodeBuildService(repository.findById(applicationId).get().getApplicationType())
            .createBuild(applicationId, details);
    return new ResponseEntity<>(
        resDetails, resDetails == null ? HttpStatus.NOT_FOUND : HttpStatus.CREATED);
  }

  @GetMapping("codebuild/builds/{buildId}")
  public ResponseEntity<CodeBuildDetails> getBuildDetails(
      @PathVariable(name = "buildId") String buildId) {
    CodeBuildDetails resDetails =
        codeBuildServiceFactory.getDefaultBuildService().getBuildDetails(buildId);
    HttpStatus status = (resDetails == null) ? HttpStatus.NOT_FOUND : HttpStatus.OK;
    return new ResponseEntity<>(resDetails, status);
  }

  @DeleteMapping("codebuild/builds/{buildId}")
  public ResponseEntity<CodeBuildDetails> stopBuild(
      @PathVariable(name = "buildId") String buildId) {
    CodeBuildDetails resDetails =
        codeBuildServiceFactory.getDefaultBuildService().stopBuild(buildId);
    HttpStatus status = (resDetails == null) ? HttpStatus.NOT_FOUND : HttpStatus.OK;
    return new ResponseEntity<>(resDetails, status);
  }

  @PostMapping("codebuild/applications/{applicationId}/deploy/{tag}")
  public ResponseEntity<Object> deployApplication(
      @PathVariable(name = "applicationId") String applicationId,
      @PathVariable(name = "tag") String tag) {

    CodeBuildApplication application =
        codeBuildServiceFactory.getDefaultBuildService().getApplication(applicationId);
    KubernetesClient kubernetesClient = new DefaultKubernetesClient();
    Deployment deployment =
        kubernetesClient
            .extensions()
            .deployments()
            .inNamespace(application.getNamespace())
            .withName(applicationId)
            .get();

    String imagePath =
        deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getImage();
    String newPath = imagePath.split(":")[0] + ":" + tag;
    deployment.getSpec().getTemplate().getSpec().getContainers().get(0).setImage(newPath);
    kubernetesClient
        .extensions()
        .deployments()
        .inNamespace(application.getNamespace())
        .withName(applicationId)
        .createOrReplace(deployment);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
