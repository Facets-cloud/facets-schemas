package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.SnapshotInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SnapshotInfoRepository extends MongoRepository<SnapshotInfo, String> {
    public Optional<SnapshotInfo> findByClusterIdAndResourceTypeAndInstanceNameAndPinned(String clusterId, String resourceType,
                                                                                         String instanceName, boolean pinned);

    List<SnapshotInfo> findByClusterIdAndPinned(String clusterId, boolean b);
}
