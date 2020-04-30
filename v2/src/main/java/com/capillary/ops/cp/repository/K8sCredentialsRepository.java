package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.K8sCredentials;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface K8sCredentialsRepository extends MongoRepository<K8sCredentials, String> {

    Optional<K8sCredentials> findOneByClusterId(String clusterId);
}