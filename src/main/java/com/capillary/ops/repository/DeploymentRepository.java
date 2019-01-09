package com.capillary.ops.repository;

import com.capillary.ops.bo.Deployment;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeploymentRepository extends MongoRepository<Deployment, String> {

  public List<Deployment> findByApplicationId(String applicationId);
}
