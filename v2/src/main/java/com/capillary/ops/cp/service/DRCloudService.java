package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.SnapshotInfo;

import java.util.List;

public interface DRCloudService {
    String getResourceType();

    List<SnapshotInfo> listSnapshots(AbstractCluster cluster);

    List<SnapshotInfo> listSnapshots(AbstractCluster cluster, String instanceName);

    SnapshotInfo pinSnapshot(String clusterId, String resourceType, String instanceName, SnapshotInfo snapshotInfo);

    SnapshotInfo getPinnedSnapshot(String clusterId, String resourceType, String instanceName);

    boolean createSnapshot(AbstractCluster cluster, String resourceType, String instanceName);
}
