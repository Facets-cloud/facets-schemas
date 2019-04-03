package com.capillary.ops.service.helm.impl;

import com.capillary.ops.bo.codebuild.CodeBuildApplication;
import com.capillary.ops.bo.exceptions.ResourceAlreadyExists;
import com.capillary.ops.bo.helm.HelmApplication;
import com.capillary.ops.bo.helm.Port;
import com.capillary.ops.repository.helm.EcommerceHelmApplicationRepository;
import com.capillary.ops.service.CodeBuildServiceFactory;
import com.capillary.ops.service.helm.HelmAppCreationService;
import com.capillary.ops.service.helm.build.BuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.codebuild.model.SourceType;

import java.util.List;

@Service
public class EcomHelmAppCreationServiceImpl extends BaseHelmAppCreationServiceImpl
    implements HelmAppCreationService {

  @Autowired private EcommerceHelmApplicationRepository applicationRepository;

  @Autowired private BuildService buildService;

  @Autowired
  private CodeBuildServiceFactory codeBuildServiceFactory;

  @Override
  public HelmApplication create(HelmApplication helmApplication) {
    if (!Port.areValid(helmApplication.getPortMapping())) {
      throw new RuntimeException("invalid port mapping");
    }

    List<HelmApplication> existingApplications =
        applicationRepository.findByName(helmApplication.getName());
    if (!existingApplications.isEmpty()) {
      throw new ResourceAlreadyExists(
          "helm application already exists: " + helmApplication.getName());
    }

        HelmApplication application = applicationRepository.save(helmApplication);

        buildService.createEcrRepository(helmApplication);

        createCodeBuildApplication(helmApplication, application.getId());

        return application;
    }

    private void createCodeBuildApplication(HelmApplication helmApplication, String id) {
        CodeBuildApplication codeBuildApplication = new CodeBuildApplication();
        codeBuildApplication.setSourceType(SourceType.GITHUB.name());
        codeBuildApplication.setApplicationType(helmApplication.getBuildType().name());
        codeBuildApplication.setRepoURL(helmApplication.getRepositoryUrl());
        codeBuildApplication.setProjectFolder(helmApplication.getPathFromRoot());
        codeBuildApplication.setName(helmApplication.getName());
        codeBuildApplication.setId(id);

        codeBuildServiceFactory.getCodeBuildService(
                CodeBuildApplication.ApplicationType.valueOf(
                        helmApplication.getBuildType().name())).createApplication(codeBuildApplication);
    }
}
