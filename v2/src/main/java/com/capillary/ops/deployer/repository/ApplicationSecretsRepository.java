package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.ApplicationSecret;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApplicationSecretsRepository extends MongoRepository<ApplicationSecret, String> {

    public List<ApplicationSecret> findByEnvironmentNameAndApplicationFamilyAndApplicationId(String environmentName, ApplicationFamily applicationFamily, String applicationId);
}
