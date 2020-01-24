package com.capillary.ops.deployer.repository.capillaryCloud;

import com.capillary.ops.deployer.bo.capillaryCloud.InfrastructureResourceInstance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.Optional;

public interface InfrastructureResourceInstanceRepository extends MongoRepository<InfrastructureResourceInstance, String> {
    Optional<InfrastructureResourceInstance> findOneByInfrastructureResourceNameAndClusterName(String infrastructureResourceId, String clusterId);
    Collection<InfrastructureResourceInstance> findByClusterName(String clusterName);
}