package com.capillary.ops.deployer.service.facade;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.bo.LogEvent;
import com.capillary.ops.deployer.repository.ApplicationRepository;
import com.capillary.ops.deployer.repository.BuildRepository;
import com.capillary.ops.deployer.repository.DeploymentRepository;
import com.capillary.ops.deployer.service.CodeBuildService;
import com.capillary.ops.deployer.service.ECRService;
import com.capillary.ops.deployer.service.HelmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatchlogs.model.OutputLogEvent;
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

    public Application createApplication(Application application) {
        applicationRepository.save(application);
        ecrService.createRepository(application);
        codeBuildService.createProject(application);
        return application;
    }

    public Build createBuild(Build build) {
        Application application = applicationRepository.findById(build.getApplicationId()).get();
        buildRepository.save(build);
        String codeBuildId = codeBuildService.triggerBuild(application, build);
        build.setCodeBuildId(codeBuildId);
        buildRepository.save(build);
        return build;
    }

    public List<Application> getApplications() {
        return applicationRepository.findAll();
    }

    public Build getBuild(String buildId) {
        Build build = buildRepository.findById(buildId).get();
        return getBuildDetails(build);
    }

    private Build getBuildDetails(Build build) {
        StatusType status = codeBuildService.getBuild(build.getCodeBuildId()).buildStatus();
        build.setStatus(status);
        return build;
    }

    public List<Build> getBuilds() {
        List<Build> builds = buildRepository.findAll();
        builds = builds.stream().parallel().map(x -> getBuildDetails(x)).collect(Collectors.toList());
        return builds;
    }

    public List<LogEvent> getBuildLogs(String buildId) {
        Build build = getBuild(buildId);
        return codeBuildService.getBuildLogs(build.getCodeBuildId());
    }

    public Deployment createDeployment(Deployment deployment) {
        deploymentRepository.save(deployment);
        Application application = applicationRepository.findById(deployment.getApplicationId()).get();
        helmService.deploy(application, deployment);
        return deployment;
    }
}
