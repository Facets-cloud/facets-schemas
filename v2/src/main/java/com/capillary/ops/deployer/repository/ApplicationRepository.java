package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.ApplicationFamily;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ApplicationRepository extends MongoRepository<Application, String> {
    Application findByName(String name);
    List<Application> findByApplicationFamily(ApplicationFamily applicationFamily);
    Optional<Application> findOneByApplicationFamilyAndId(ApplicationFamily applicationFamily, String applicationId);

    Optional<Application> findFirstByApplicationFamily(ApplicationFamily applicationFamily);

    List<Application> findByApplicationFamilyAndApplicationType(ApplicationFamily applicationFamily,
                                              Application.ApplicationType applicationType);

    List<Application> findByApplicationType(Application.ApplicationType applicationType);

}
