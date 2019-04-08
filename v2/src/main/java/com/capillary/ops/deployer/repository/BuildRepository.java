package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.Deployment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BuildRepository extends MongoRepository<Build, String> {
}
