package com.capillary.ops.cp.service.local;

import com.capillary.ops.cp.bo.AzureCluster;
import com.capillary.ops.cp.bo.LocalCluster;
import com.capillary.ops.cp.bo.components.ComponentType;
import com.capillary.ops.cp.bo.requests.AzureClusterRequest;
import com.capillary.ops.cp.bo.requests.LocalClusterRequest;
import com.capillary.ops.cp.service.ClusterService;
import com.capillary.ops.cp.service.ComponentVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.SimpleTimeZone;
@Service
public class LocalClusterService implements ClusterService<LocalCluster, LocalClusterRequest> {

  @Autowired
  private ComponentVersionService componentVersionService;

  @Override
  public LocalCluster createCluster(LocalClusterRequest request) {
    LocalCluster cluster = new LocalCluster(request.getClusterName());

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
  public LocalCluster updateCluster(LocalClusterRequest request, LocalCluster existing) {
    if (request.getTz() == null || request.getTz().getID() == null){
      request.setTz(new SimpleTimeZone(0, existing.getTz()));
    }

    if (checkChanged(existing.getTz(), request.getTz().getID())) {
      existing.setTz(request.getTz());
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
