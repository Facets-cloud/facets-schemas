package com.capillary.ops.deployer.repository.capillaryCloud;

import com.capillary.ops.deployer.bo.capillaryCloud.Cluster;
import com.capillary.ops.deployer.bo.capillaryCloud.InfrastructureResource;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InfrastructureResourceRepository extends MongoRepository<InfrastructureResource, String> {
}
