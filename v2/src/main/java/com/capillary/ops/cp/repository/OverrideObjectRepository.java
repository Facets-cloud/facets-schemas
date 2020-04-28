package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.OverrideObject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OverrideObjectRepository extends MongoRepository<OverrideObject, String> {

    Optional<OverrideObject> findOneByClusterIdAndResourceTypeAndResourceName(String clusterId, String resourceType,
        String resourceName);
}
