package com.capillary.ops.deployer.repository.capillaryCloud;

import com.capillary.ops.deployer.bo.capillaryCloud.Cluster;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ClusterRepository extends MongoRepository<Cluster, String> {
    Optional<Cluster> findOneByName(String clusterName);
}
