package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.QASuite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QASuiteRepository extends MongoRepository<QASuite, String> {


}
