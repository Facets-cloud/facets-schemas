package com.capillary.ops.service;

import com.capillary.ops.bo.AbstractInfrastructureResource;

public interface AbstractResourceResponseHandler {

  public void handleResponse(
      AbstractInfrastructureResource infrastructureResource, String releaseName);

  public void handleError(AbstractInfrastructureResource infrastructureResource, Exception ex);
}
