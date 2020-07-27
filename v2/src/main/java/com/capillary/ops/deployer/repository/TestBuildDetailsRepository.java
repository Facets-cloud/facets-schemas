package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.TestBuildDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TestBuildDetailsRepository extends MongoRepository<TestBuildDetails, String> {

    List<TestBuildDetails> findByApplicationIdOrderByTimestampDesc(String applicationId);

    Optional<TestBuildDetails> findFirstByBuildId(String buildId);


}
