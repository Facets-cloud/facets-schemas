package com.capillary.ops.service;

import com.capillary.ops.bo.Application;
import com.capillary.ops.bo.exceptions.ApplicationAlreadyExists;
import com.capillary.ops.bo.exceptions.ApplicationDoesNotExist;
import com.capillary.ops.repository.ApplicationRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationMongoService {

  @Autowired private ApplicationRepository applicationRepository;

  public Application getApplication(String name) {
    Application application = applicationRepository.findByName(name);
    return application;
  }

  public Application createApplication(Application application) {
    Application existingApplication = getApplication(application.getName());
    if (existingApplication != null) {
      throw new ApplicationAlreadyExists();
    }
    application = applicationRepository.insert(application);
    return application;
  }

  public List<Application> getAll() {
    return applicationRepository.findAll();
  }

  public Application getApplicationById(String applicationId) {
    Optional<Application> byId = applicationRepository.findById(applicationId);
    if (!byId.isPresent()) {
      throw new ApplicationDoesNotExist();
    }
    return byId.get();
  }

  public Application updateApplication(Application application) {
    return applicationRepository.save(application);
  }

  public void deleteApplication(Application application) {
    applicationRepository.delete(application);
  }
}
