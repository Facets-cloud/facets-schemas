package com.capillary.ops.service.impl;

import com.capillary.ops.bo.AbstractInfrastructureResource;
import com.capillary.ops.bo.InfrastructureResourceStatus;
import com.capillary.ops.bo.mongodb.MongoResource;
import com.capillary.ops.repository.mongodb.MongoInfraRepository;
import com.capillary.ops.service.AbstractResourceResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoResourceResponseHandler implements AbstractResourceResponseHandler {

  @Autowired private MongoInfraRepository mongoInfraRepository;

  @Override
  public void handleResponse(
      AbstractInfrastructureResource infrastructureResource, String releaseName) {
    MongoResource mongoResource = (MongoResource) infrastructureResource;
    mongoResource.setDeploymentName(releaseName);
    mongoResource.setDeploymentStatus(InfrastructureResourceStatus.SUCCESS);

    mongoInfraRepository.save(mongoResource);
  }

  @Override
  public void handleError(AbstractInfrastructureResource infrastructureResource, Exception ex) {
    MongoResource mongoResource = (MongoResource) infrastructureResource;

    System.out.println("error happened while deploying resource:" + mongoResource);
    ex.printStackTrace();

    infrastructureResource.setDeploymentStatus(InfrastructureResourceStatus.FAILURE);
    mongoInfraRepository.save(mongoResource);
  }
}
