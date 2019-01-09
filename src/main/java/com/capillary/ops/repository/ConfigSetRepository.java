package com.capillary.ops.repository;

import com.capillary.ops.bo.ConfigSet;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigSetRepository extends MongoRepository<ConfigSet, String> {

  public List<ConfigSet> findByName(String name);
}
