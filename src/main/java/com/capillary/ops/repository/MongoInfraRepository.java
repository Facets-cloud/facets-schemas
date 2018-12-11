package com.capillary.ops.repository;

import com.capillary.ops.bo.Environments;
import com.capillary.ops.bo.MongoResource;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoInfraRepository extends MongoRepository<MongoResource, String> {
    public List<MongoResource> findByAppNameAndEnvironment(String appName, Environments environment);
}
