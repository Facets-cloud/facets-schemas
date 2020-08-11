package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.SnapshotInfo;
import com.capillary.ops.cp.repository.CpClusterRepository;
import com.capillary.ops.cp.repository.SnapshotInfoRepository;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BaseDRService {

    @Autowired
    private CpClusterRepository cpClusterRepository;

    @Autowired
    private SnapshotInfoRepository snapshotInfoRepository;

    private static final Logger logger = LoggerFactory.getLogger(BaseDRService.class);

    public SnapshotInfo pinSnapshot(String clusterId, String resourceType, String instanceName,
                             SnapshotInfo snapshotInfo) {
        Optional<SnapshotInfo> pinnedSnapshot = snapshotInfoRepository.findByClusterIdAndResourceTypeAndInstanceNameAndPinned(
                clusterId, resourceType, instanceName, true);
        pinnedSnapshot.ifPresent(info -> snapshotInfoRepository.delete(info));

        snapshotInfo.setPinned(true);
        snapshotInfo.setClusterId(clusterId);
        snapshotInfo.setResourceType(resourceType);
        snapshotInfo.setInstanceName(instanceName);

        return snapshotInfoRepository.save(snapshotInfo);
    }

    public SnapshotInfo getPinnedSnapshot(String clusterId, String resourceType, String instanceName) {
        Optional<SnapshotInfo> pinnedSnapshot = snapshotInfoRepository.findByClusterIdAndResourceTypeAndInstanceNameAndPinned(
                clusterId, resourceType, instanceName, true);
        if (!pinnedSnapshot.isPresent()) {
            String error = String.format("No pinned snapshot found for cluster: %s, resourceType: %s, instanceName: %s",
                    clusterId, resourceType, instanceName);
            logger.error(error);
            throw new NotFoundException(error);
        }

        return pinnedSnapshot.get();
    }
}
