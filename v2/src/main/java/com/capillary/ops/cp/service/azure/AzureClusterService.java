package com.capillary.ops.cp.service.azure;

import com.capillary.ops.cp.bo.AzureCluster;
import com.capillary.ops.cp.bo.components.ComponentType;
import com.capillary.ops.cp.bo.requests.AzureClusterRequest;
import com.capillary.ops.cp.service.ClusterService;
import com.capillary.ops.cp.service.ComponentVersionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.SimpleTimeZone;

public class AzureClusterService implements ClusterService<AzureCluster, AzureClusterRequest> {

  @Autowired
  private ComponentVersionService componentVersionService;

  @Override
  public AzureCluster createCluster(AzureClusterRequest request) {
    AzureCluster cluster = new AzureCluster(request.getClusterName());

    cluster.setClientSecret(request.getClientSecret());
    cluster.setTenantId(request.getTenantId());
    cluster.setSubscriptionId(request.getSubscriptionId());
    cluster.setClientId(request.getClientId());

    cluster.setTz(request.getTz());
    //cluster.setRegion(request.getRegion());
    cluster.setReleaseStream(request.getReleaseStream());
    cluster.setK8sRequestsToLimitsRatio(request.getK8sRequestsToLimitsRatio());
    //TODO: Variable Generations
    //1. Generate CIDR.
    cluster.setStackName(request.getStackName());
    cluster.setCdPipelineParent(request.getCdPipelineParent());
    cluster.setRequireSignOff(request.getRequireSignOff());

    Map<ComponentType, String> componentVersions = componentVersionService.getClusterComponentVersions(
            cluster.getStackName(), request);
    cluster.setComponentVersions(componentVersions);

    return cluster;
  }

  @Override
  public AzureCluster updateCluster(AzureClusterRequest request, AzureCluster existing) {
    if (request.getTz() == null || request.getTz().getID() == null){
      request.setTz(new SimpleTimeZone(0, existing.getTz()));
    }

    if (checkChanged(existing.getTz(), request.getTz().getID())) {
      existing.setTz(request.getTz());
    }

    if (checkChanged(existing.getClientId(), request.getClientSecret()) || checkChanged(existing.getSubscriptionId(),
      request.getTenantId())) {
      existing.setClientId(request.getClientId());
      existing.setTenantId(request.getTenantId());
      existing.setSubscriptionId(request.getSubscriptionId());
      existing.setClientSecret(request.getClientSecret());
    }

    if (checkChanged(existing.getK8sRequestsToLimitsRatio(), request.getK8sRequestsToLimitsRatio())) {
      existing.setK8sRequestsToLimitsRatio(request.getK8sRequestsToLimitsRatio());
    }

    if (checkChanged(existing.getCdPipelineParent(), request.getCdPipelineParent())) {
      existing.setCdPipelineParent(request.getCdPipelineParent());
    }

    if (checkChanged(existing.getRequireSignOff(), request.getRequireSignOff())) {
      existing.setRequireSignOff(request.getRequireSignOff());
    }
    componentVersionService.syncComponentsVersion(request, existing);
    return existing;
  }

  private boolean checkChanged(Object old, Object changed) {
    return changed != null && !changed.equals(old);
  }

}
