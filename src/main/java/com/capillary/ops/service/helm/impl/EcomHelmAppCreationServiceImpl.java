package com.capillary.ops.service.helm.impl;

import com.capillary.ops.bo.codebuild.CodeBuildApplication;
import com.capillary.ops.bo.exceptions.ResourceAlreadyExists;
import com.capillary.ops.bo.helm.HelmApplication;
import com.capillary.ops.bo.helm.Port;
import com.capillary.ops.repository.helm.EcommerceHelmApplicationRepository;
import com.capillary.ops.service.CodeBuildService;
import com.capillary.ops.service.helm.HelmAppCreationService;
import com.capillary.ops.service.helm.build.BuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EcomHelmAppCreationServiceImpl extends BaseHelmAppCreationServiceImpl implements HelmAppCreationService {

    @Autowired
    private EcommerceHelmApplicationRepository applicationRepository;

    @Autowired
    private BuildService buildService;

    @Autowired
    private CodeBuildService codeBuildService;

    @Override
    public HelmApplication create(HelmApplication helmApplication) {
        if (!Port.areValid(helmApplication.getPortMapping())) {
            throw new RuntimeException("invalid port mapping");
        }

        List<HelmApplication> existingApplications = applicationRepository.findByName(helmApplication.getName());
        if (!existingApplications.isEmpty()) {
            throw new ResourceAlreadyExists("helm application already exists: " + helmApplication.getName());
        }

        buildService.createEcrRepository(helmApplication);

        codeBuildService.createApplication(new CodeBuildApplication(
                CodeBuildApplication.ApplicationSource.GITHUB,
                CodeBuildApplication.ApplicationType.valueOf(helmApplication.getBuildType().name()),
                helmApplication.getApplicationFamily().name()));

        return applicationRepository.save(helmApplication);
    }
}
