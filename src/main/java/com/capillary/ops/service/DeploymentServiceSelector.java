package com.capillary.ops.service;

import com.capillary.ops.service.helm.HelmDeploymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeploymentServiceSelector {

  @Autowired private HelmDeploymentService helmDeploymentService;

  InfrastructureDeploymentService selectService(String serviceType) {
    switch (serviceType) {
      case "helm":
        return helmDeploymentService;
      default:
        throw new RuntimeException("cannot select unknown service: " + serviceType);
    }
  }
}
