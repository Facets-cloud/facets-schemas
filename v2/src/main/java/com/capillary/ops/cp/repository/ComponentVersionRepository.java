package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.components.SupportedVersions;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ComponentVersionRepository extends MongoRepository<SupportedVersions, String> {
}
