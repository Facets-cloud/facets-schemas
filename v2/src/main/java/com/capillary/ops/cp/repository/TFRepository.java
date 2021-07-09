package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.TFDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TFRepository extends MongoRepository<TFDetails, String> {
    Optional<TFDetails> findOneByClusterId(String clusterId);
}
