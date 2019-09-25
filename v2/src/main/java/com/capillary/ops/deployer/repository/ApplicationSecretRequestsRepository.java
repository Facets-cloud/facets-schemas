package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.ApplicationSecret;
import com.capillary.ops.deployer.bo.ApplicationSecretRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApplicationSecretRequestsRepository extends MongoRepository<ApplicationSecretRequest, String> {

    public List<ApplicationSecretRequest> findByApplicationFamilyAndApplicationId(ApplicationFamily applicationFamily, String applicationId);
}
