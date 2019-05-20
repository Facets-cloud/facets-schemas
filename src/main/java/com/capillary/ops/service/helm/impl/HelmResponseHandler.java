package com.capillary.ops.service.helm.impl;

import com.capillary.ops.bo.AbstractDeploymentResource;
import com.capillary.ops.bo.AbstractInfrastructureResource;
import com.capillary.ops.bo.HelmInfrastructureResource;
import com.capillary.ops.repository.helm.HelmInfrastructureRepository;
import com.capillary.ops.service.DeploymentResponseHandler;
import com.capillary.ops.service.ResourceResponseHandlerSelector;
import hapi.release.ReleaseOuterClass.Release;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelmResponseHandler implements DeploymentResponseHandler {

  @Autowired private HelmInfrastructureRepository helmInfrastructureRepository;

  @Autowired private ResourceResponseHandlerSelector responseHandlerSelector;

  @Override
  public void handleResponse(
      AbstractInfrastructureResource infrastructureResource,
      AbstractDeploymentResource deploymentResource,
      Object responseObject) {

    HelmInfrastructureResource helmInfrastructureResource =
        (HelmInfrastructureResource) deploymentResource;

    Release release = (Release) responseObject;
    if (release == null) {
      throw new RuntimeException(
          "error occured while deploying resource, release was null: " + deploymentResource);
    }

    String releaseName = release.getName();

    helmInfrastructureResource.setDeploymentName(releaseName);
    helmInfrastructureRepository.save((HelmInfrastructureResource) deploymentResource);

    responseHandlerSelector
        .selectHandler(deploymentResource.getType())
        .handleResponse(infrastructureResource, releaseName);
  }

  @Override
  public void handleError(
      AbstractInfrastructureResource infrastructureResource,
      AbstractDeploymentResource deploymentResource,
      Exception ex) {
    System.out.println("exception occured while deploying helm resource = " + ex);
    responseHandlerSelector
        .selectHandler(deploymentResource.getType())
        .handleError(infrastructureResource, ex);
  }
}
