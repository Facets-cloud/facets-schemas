package com.capillary.ops.deployer.repository;


import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.ApplicationFamilyMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ApplicationFamilyMetadataRepository extends MongoRepository<ApplicationFamilyMetadata, String> {
    Optional<ApplicationFamilyMetadata> findOneByApplicationFamily(ApplicationFamily applicationFamily);
}
