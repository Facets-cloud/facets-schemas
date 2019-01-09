package com.capillary.ops.repository;

import com.capillary.ops.bo.Application;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApplicationRepository extends MongoRepository<Application, String> {

  public Application findByName(String name);
}
