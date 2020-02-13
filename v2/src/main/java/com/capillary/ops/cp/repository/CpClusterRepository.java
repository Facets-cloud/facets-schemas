package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.AbstractCluster;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpClusterRepository extends MongoRepository<AbstractCluster, String> {


}
