package com.capillary.ops.repository;

import com.capillary.ops.bo.ConfigSet;
import com.capillary.ops.bo.Deployment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ConfigSetRepository extends MongoRepository<ConfigSet, String> {

    public List<ConfigSet> findByName(String name);
}
