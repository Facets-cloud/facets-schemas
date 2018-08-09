package com.capillary.ops.repository;

import com.capillary.ops.bo.Application;
import com.capillary.ops.bo.Deployment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeploymentRepository extends MongoRepository<Deployment, String> {
}
