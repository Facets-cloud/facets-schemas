package com.capillary.ops.deployer.repository.capillaryCloud.infraResources;

import com.capillary.ops.deployer.bo.capillaryCloud.Cluster;
import com.capillary.ops.deployer.bo.capillaryCloud.K8sCluster;
import com.capillary.ops.deployer.bo.capillaryCloud.VPC;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface K8sClusterRepository extends MongoRepository<K8sCluster, String> {
}
