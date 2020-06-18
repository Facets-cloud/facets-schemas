package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.QASuiteResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QASuiteResultRepository extends MongoRepository<QASuiteResult, String> {
    Optional<QASuiteResult> findOneByDeploymentId(String deploymentId);
}
