package com.capillary.ops.deployer.service.facade;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.repository.ApplicationRepository;
import com.capillary.ops.deployer.repository.BuildRepository;
import com.capillary.ops.deployer.repository.DeploymentRepository;
import com.capillary.ops.deployer.service.CodeBuildService;
import com.capillary.ops.deployer.service.ECRService;
import com.capillary.ops.deployer.service.HelmService;
import com.capillary.ops.deployer.service.KubectlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationFacade {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ECRService ecrService;

    @Autowired
    private CodeBuildService codeBuildService;

    @Autowired
    private BuildRepository buildRepository;

    @Autowired
    private HelmService helmService;

    @Autowired
    private DeploymentRepository deploymentRepository;

    @Autowired
    private KubectlService kubectlService;

    public Application createApplication(Application application) {
        applicationRepository.save(application);
        ecrService.createRepository(application);
        codeBuildService.createProject(application);
        return application;
    }

    public Build createBuild(ApplicationFamily applicationFamily, Build build) {
        String applicationId = build.getApplicationId();
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        buildRepository.save(build);
        String codeBuildId = codeBuildService.triggerBuild(application, build);
        build.setCodeBuildId(codeBuildId);
        buildRepository.save(build);
        return build;
    }

    public List<Application> getApplications(ApplicationFamily applicationFamily) {
        return applicationRepository.findByApplicationFamily(applicationFamily);
    }

    public Build getBuild(ApplicationFamily applicationFamily, String applicationId, String buildId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Build build = buildRepository.findOneByApplicationIdAndId(application.getId(), buildId).get();
        return getBuildDetails(build);
    }

    private Build getBuildDetails(Build build) {
        StatusType status = codeBuildService.getBuild(build.getCodeBuildId()).buildStatus();
        build.setStatus(status);
        return build;
    }

    public List<Build> getBuilds(ApplicationFamily applicationFamily, String applicationId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        List<Build> builds = buildRepository.findByApplicationId(application.getId());
        builds = builds.stream().parallel().map(x -> getBuildDetails(x)).collect(Collectors.toList());
        return builds;
    }

    public List<Deployment> getDeployments(String applicationId, String environment) {
        List<Deployment> deployments = deploymentRepository.findByApplicationIdAndEnvironmentOrderByTimestampDesc(applicationId, environment);
        return deployments;
    }


    public List<LogEvent> getBuildLogs(ApplicationFamily applicationFamily, String applicationId, String buildId) {
        Build build = getBuild(applicationFamily, applicationId, buildId);
        return codeBuildService.getBuildLogs(build.getCodeBuildId());
    }

    public Deployment createDeployment(ApplicationFamily applicationFamily, String environment, String applicationId, Deployment deployment) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        deployment.setApplicationId(application.getId());
        deployment.setEnvironment(environment);
        deploymentRepository.save(deployment);
        helmService.deploy(application, deployment);
        return deployment;
    }

    public DeploymentStatusDetails getDeploymentStatus(ApplicationFamily applicationFamily, String environment, String applicationId) {
        Optional<Application> existingApplication = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId);
        if (!existingApplication.isPresent()) {
            throw new RuntimeException("application with id: " + applicationId + "does not exist in family: " + applicationFamily);
        }

        Application application = existingApplication.get();
        return kubectlService.getDeploymentStatus(application, environment, helmService.getReleaseName(application, environment));
    }

    public List<String> getImages(ApplicationFamily applicationFamily, String applicationId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        return ecrService.listImages(application);
    }
}
