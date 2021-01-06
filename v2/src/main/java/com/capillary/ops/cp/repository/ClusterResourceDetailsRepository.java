package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.ClusterResourceDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import software.amazon.awssdk.services.codebuild.model.StatusType;
import java.util.Optional;

public interface ClusterResourceDetailsRepository extends MongoRepository<ClusterResourceDetails, String> {

    Optional<ClusterResourceDetails> findByCodeBuildId(String codeBuildId);

    Optional<ClusterResourceDetails> findFirstByClusterIdAndStatusOrderByIdDesc(String clusterId, StatusType status);
}
