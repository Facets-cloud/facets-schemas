package com.capillary.ops.deployer.service.facade;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.repository.ApplicationRepository;
import com.capillary.ops.deployer.service.CodeBuildService;
import com.capillary.ops.deployer.service.ECRService;
import com.capillary.ops.deployer.service.HelmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationFacade {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ECRService ecrService;

    @Autowired
    private CodeBuildService codeBuildService;

    @Autowired
    private HelmService helmService;

    public Application createApplication(Application application) {
        applicationRepository.save(application);
        ecrService.createRepository(application);
        codeBuildService.createProject(application);
        return application;
    }
}
