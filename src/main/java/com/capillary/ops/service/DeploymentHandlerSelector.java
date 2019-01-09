package com.capillary.ops.service;

import com.capillary.ops.service.impl.HelmResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeploymentHandlerSelector {

  @Autowired private HelmResponseHandler helmResponseHandler;

  DeploymentResponseHandler selectHandler(String serviceType) {
    switch (serviceType) {
      case "helm":
        System.out.println("selected response handler for helm");
        return helmResponseHandler;
      default:
        throw new RuntimeException("cannot select unknown handler for: " + serviceType);
    }
  }
}
