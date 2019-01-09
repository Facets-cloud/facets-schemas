package com.capillary.ops.repository;

import com.capillary.ops.bo.Deployment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeploymentRepository extends
    MongoRepository<Deployment, String> {

    public List<Deployment> findByApplicationId(String applicationId);
}
