package com.capillary.ops.repository.mongodb;

import com.capillary.ops.bo.Environments;
import com.capillary.ops.bo.mongodb.MongoResource;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoInfraRepository extends MongoRepository<MongoResource, String> {

  public List<MongoResource> findByResourceNameAndEnvironment(
      String resourceName, Environments environment);
}
