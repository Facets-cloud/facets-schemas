package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.Application;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApplicationRepository extends MongoRepository<Application, String> {
  public Application findByName(String name);
}
