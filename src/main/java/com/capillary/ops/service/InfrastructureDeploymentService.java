package com.capillary.ops.service;

import com.capillary.ops.bo.AbstractDeploymentResource;

import java.io.IOException;

public interface InfrastructureDeploymentService {

    Object deploy(AbstractDeploymentResource deploymentResource)
        throws IOException;

    Object update(AbstractDeploymentResource deploymentResource)
        throws IOException;
}
