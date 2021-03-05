package com.capillary.ops.cp.service.aws;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.SnapshotInfo;
import com.capillary.ops.cp.service.DRCloudService;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.exceptions.NotImplementedException;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rds.model.DBClusterSnapshot;
import software.amazon.awssdk.services.rds.model.Filter;
import software.amazon.awssdk.services.rds.model.Tag;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AwsAuroraDRService extends BaseAwsDRService implements DRCloudService {

    @Autowired
    private RDSService rdsService;

    private static final Logger logger = LoggerFactory.getLogger(AwsAuroraDRService.class);

    @Override
    public String getResourceType() {
        return "aurora";
    }

    private SnapshotInfo toSnapshotInfo(String clusterId, DBClusterSnapshot dbClusterSnapshot, String resourceType,
                                        String instanceName) {
        return new SnapshotInfo(clusterId, dbClusterSnapshot.dbClusterSnapshotIdentifier(),
                dbClusterSnapshot.dbClusterSnapshotArn(), resourceType, instanceName,
                dbClusterSnapshot.dbClusterIdentifier(), dbClusterSnapshot.snapshotCreateTime(), null,
                dbClusterSnapshot.allocatedStorage());
    }

    private List<SnapshotInfo> toSnapshotInfo(String clusterId, List<DBClusterSnapshot> dbClusterSnapshot, String resourceType,
                                              String instanceName) {
        return dbClusterSnapshot.parallelStream()
                .map(snapshot -> toSnapshotInfo(clusterId, snapshot, resourceType, instanceName))
                .collect(Collectors.toList());
    }

    @Override
    public List<SnapshotInfo> listSnapshots(AbstractCluster cluster) {
        throw new NotImplementedException("Get all snapshots is not implemented for aurora");
    }

    private String removeHyphens(String name) {
        return name.replaceAll("-", "");
    }

    @Override
    public List<SnapshotInfo> listSnapshots(AbstractCluster cluster, String instanceName) {
        AwsCluster awsCluster = (AwsCluster) cluster;

        List<DBClusterSnapshot> snapshots = rdsService.getSnapshots(awsCluster, RDSService.SnapshotType.automated);
        String sanitizedName = "rds:" + removeHyphens(cluster.getName() + instanceName) + "-";
        List<DBClusterSnapshot> dbClusterSnapshots = snapshots.stream()
                .filter(snap -> snap.dbClusterSnapshotIdentifier().startsWith(sanitizedName))
                .collect(Collectors.toList());
        logger.info("found {} rds snapshots for db: {} and cluster: {}", dbClusterSnapshots.size(), sanitizedName,
                awsCluster.getId());

        return toSnapshotInfo(cluster.getId(), dbClusterSnapshots, getResourceType(), instanceName);
    }

    @Override
    public boolean createSnapshot(AbstractCluster cluster, String resourceType, String instanceName) {
        AwsCluster awsCluster = (AwsCluster) cluster;

        String clusterName = cluster.getName();
        String rdsClusterId = (clusterName + instanceName).replaceAll("-", "");
        String snapshotId = String.format("%s-snapshot-%s", rdsClusterId, Instant.now().getEpochSecond());

        SnapshotInfo pinnedSnapshot = null;
        try {
            pinnedSnapshot = getPinnedSnapshot(cluster.getId(), resourceType, instanceName);
        } catch (NotFoundException ex) {
            logger.info("did not find pinned aurora snapshot for cluster: {}, instance: {}", cluster.getId(), instanceName);
        }

        if (pinnedSnapshot != null) {
            rdsClusterId = pinnedSnapshot.getName().replaceAll("-", "");
        }

        Tag clusterTag = Tag.builder()
                .key("cluster")
                .value(clusterName)
                .build();
        Tag rdsClusterTag = Tag.builder()
                .key("rdsCluster")
                .value(rdsClusterId)
                .build();

        rdsService.createDBClusterSnapshot(awsCluster, rdsClusterId, snapshotId,
                Lists.newArrayList(clusterTag, rdsClusterTag));
        return true;
    }
}
