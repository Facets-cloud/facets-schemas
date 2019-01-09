package com.capillary.ops.repository;

import com.capillary.ops.bo.InstanceType;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InstanceTypeRepository extends MongoRepository<InstanceType, String> {

  public List<InstanceType> findByName(String name);
}
