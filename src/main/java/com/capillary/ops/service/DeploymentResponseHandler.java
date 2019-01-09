package com.capillary.ops.service;

import com.capillary.ops.bo.AbstractDeploymentResource;
import com.capillary.ops.bo.AbstractInfrastructureResource;

public interface DeploymentResponseHandler {

    public void handleResponse(
        AbstractInfrastructureResource infrastructureResource,
        AbstractDeploymentResource deploymentResource, Object responseObject);

    public void handleError(
        AbstractInfrastructureResource infrastructureResource,
        AbstractDeploymentResource deploymentResource, Exception ex);
}
