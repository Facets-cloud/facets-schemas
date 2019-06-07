package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Deployment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DeploymentRepository extends MongoRepository<Deployment, String> {
    List<Deployment> findByApplicationFamilyAndApplicationIdAndEnvironmentOrderByTimestampDesc(ApplicationFamily applicationFamily, String applicationId, String environment);
    Optional<Deployment> findTopOneByApplicationFamilyAndApplicationIdAndEnvironmentOrderByTimestampDesc(ApplicationFamily applicationFamily, String applicationId, String environment);
}
