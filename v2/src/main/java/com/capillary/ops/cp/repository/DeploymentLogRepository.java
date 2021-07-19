package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.DeploymentLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeploymentLogRepository extends MongoRepository<DeploymentLog, String> {

    List<DeploymentLog> findFirst50ByClusterIdOrderByCreatedOnDesc(String clusterId);

    List<DeploymentLog> findFirst50ByClusterIdAndStatusNotInOrderByCreatedOnDesc(String clusterId,
                                                                                 Collection<StatusType> status);

    Integer countByClusterIdAndDeploymentTypeAndStatusAndCreatedOnGreaterThan(String clusterId,
                                                                              DeploymentLog.DeploymentType deploymentType,
                                                                              StatusType statusType,
                                                                              Date date);


    Optional<DeploymentLog> findOneByCodebuildId(String runId);

    Optional<DeploymentLog> findFirstByClusterIdAndStatusAndDeploymentTypeOrderByCreatedOnDesc(String clusterId,
                                                                                               StatusType statusType,
                                                                                               DeploymentLog.DeploymentType deploymentType);

    Optional<DeploymentLog> findFirstByClusterIdAndStatusAndDeploymentTypeAndSignedOffOrderByCreatedOnDesc(
            String clusterId,
            StatusType statusType,
            DeploymentLog.DeploymentType deploymentType,
            boolean b);

    Optional<DeploymentLog> findFirstByClusterIdOrderByCreatedOnDesc(String clusterId);
}
