package com.capillary.ops.deployer.repository.capillaryCloud;

import com.capillary.ops.deployer.bo.capillaryCloud.Cluster;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClusterRepository extends MongoRepository<Cluster, String> {
}
