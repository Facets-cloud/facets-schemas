package com.capillary.ops.deployer.repository.capillaryCloud.infraResources;

import com.capillary.ops.deployer.bo.capillaryCloud.Cluster;
import com.capillary.ops.deployer.bo.capillaryCloud.VPC;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VPCRepository extends MongoRepository<VPC, String> {
}
