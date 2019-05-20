package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.Deployment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BuildRepository extends MongoRepository<Build, String> {
    List<Build> findByApplicationId(String applicationId);
    Optional<Build> findOneByApplicationIdAndId(String applicationId, String buildId);
}
