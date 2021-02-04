package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.Registry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegistryRepository extends MongoRepository<Registry, String> {

}
