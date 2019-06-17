package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.Environment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EnvironmentRepository extends MongoRepository<Environment, String> {
    List<Environment> findByEnvironmentMetaDataApplicationFamily(ApplicationFamily applicationFamily);
    Optional<Environment> findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(ApplicationFamily applicationFamily, String name);
    Optional<Environment> findOneByEnvironmentMetaDataApplicationFamilyAndId(ApplicationFamily applicationFamily, String id);
}
