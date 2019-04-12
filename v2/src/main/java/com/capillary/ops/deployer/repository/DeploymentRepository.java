package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.Deployment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeploymentRepository extends MongoRepository<Deployment, String> {
    List<Deployment> findByApplicationIdAndEnvironmentOrderByTimestampDesc(String applicationId, String environment);
}
