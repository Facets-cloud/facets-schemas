package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.DeploymentLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeploymentLogRepository extends MongoRepository<DeploymentLog, String> {

    List<DeploymentLog> findFirst50ByClusterIdOrderByCreatedOnDesc(String clusterId);

    Optional<DeploymentLog> findOneByCodebuildId(String runId);

    Optional<DeploymentLog> findOneByClusterIdAndStatusAndDeploymentType(String clusterId,
                                                                         StatusType statusType,
                                                                         DeploymentLog.DeploymentType deploymentType);

    Optional<DeploymentLog> findOneByClusterIdAndStatusAndDeploymentTypeAndSignedOff(String clusterId,
                                                                                     StatusType statusType,
                                                                                     DeploymentLog.DeploymentType deploymentType,
                                                                                     boolean b);
}
