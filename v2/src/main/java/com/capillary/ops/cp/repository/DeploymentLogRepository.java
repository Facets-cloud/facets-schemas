package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.DeploymentLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeploymentLogRepository extends MongoRepository<DeploymentLog, String> {

    List<DeploymentLog> findFirst50ByClusterIdOrderByCreatedOnDesc(String clusterId);
}
