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

    cluster.setStackName(request.getStackName());

    return cluster;
  }

  @Override
  public LocalCluster updateCluster(LocalClusterRequest request, LocalCluster existing) {
    return existing;
  }

  private boolean checkChanged(Object old, Object changed) {
    return changed != null && !changed.equals(old);
  }

}
