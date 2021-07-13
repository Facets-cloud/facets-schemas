package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.TFRunConfigurations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TFRunConfigurationsRepository extends MongoRepository<TFRunConfigurations, String> {
    Optional<TFRunConfigurations> findOneByClusterId(String clusterId);

    void deleteByClusterId(String clusterId);
}
