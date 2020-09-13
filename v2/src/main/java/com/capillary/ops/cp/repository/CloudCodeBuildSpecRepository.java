package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.CloudCodeBuildSpec;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CloudCodeBuildSpecRepository extends MongoRepository<CloudCodeBuildSpec, String> {
    Optional<CloudCodeBuildSpec> findByClusterId(String clusterId);
}
