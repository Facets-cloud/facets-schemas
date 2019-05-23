package com.capillary.ops.deployer.service.facade;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.exceptions.AlreadyExistsException;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.repository.ApplicationRepository;
import com.capillary.ops.deployer.repository.BuildRepository;
import com.capillary.ops.deployer.repository.DeploymentRepository;
import com.capillary.ops.deployer.service.KubectlService;
import com.capillary.ops.deployer.service.S3DumpService;
import com.capillary.ops.deployer.service.SecretService;
import com.capillary.ops.deployer.service.interfaces.ICodeBuildService;
import com.capillary.ops.deployer.service.interfaces.IECRService;
import com.capillary.ops.deployer.service.interfaces.IHelmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationFacade {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private IECRService ecrService;

    @Autowired
    private ICodeBuildService codeBuildService;

    @Autowired
    private BuildRepository buildRepository;

    @Autowired
    private IHelmService helmService;

    @Autowired
    private DeploymentRepository deploymentRepository;

    @Autowired
    private KubectlService kubectlService;

    @Autowired
    private S3DumpService s3DumpService;

    @Autowired
    private SecretService secretService;

    private static final Logger logger = LoggerFactory.getLogger(ApplicationFacade.class);

    public Application createApplication(Application application) {
        applicationRepository.save(application);
        ecrService.createRepository(application);
        codeBuildService.createProject(application);
        return application;
    }

    public Application getApplication(ApplicationFamily applicationFamily, String applicationId) {
        return applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
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
        return getBuildDetails(application, build);
    }

    private Build getBuildDetails(Application application, Build build) {
        software.amazon.awssdk.services.codebuild.model.Build codeBuildServiceBuild =
                codeBuildService.getBuild(build.getCodeBuildId());
        StatusType status = codeBuildServiceBuild.buildStatus();
        build.setStatus(status);
        if(codeBuildServiceBuild.buildStatus().equals(StatusType.SUCCEEDED)) {
            build.setImage(ecrService.findImageBetweenTimes(application,
                    codeBuildServiceBuild.startTime(), codeBuildServiceBuild.endTime()));
        }
        return build;
    }

    public List<Build> getBuilds(ApplicationFamily applicationFamily, String applicationId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        List<Build> builds = buildRepository.findByApplicationId(application.getId());
        builds = builds.stream().parallel().map(x -> getBuildDetails(application, x)).collect(Collectors.toList());
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

    public DeploymentStatusDetails getDeploymentStatus(ApplicationFamily applicationFamily, String environmentName, String applicationId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = applicationFamily.getEnvironment(environmentName);
        return kubectlService.getDeploymentStatus(application, environment, helmService.getReleaseName(application, environment));
    }

    public List<String> getImages(ApplicationFamily applicationFamily, String applicationId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        return ecrService.listImages(application);
    }

    public S3DumpFile downloadDumpFileFromS3(String applicationName, String environment, String path) {
        return s3DumpService.downloadObject(path);
    }

    public List<String> listDumpFilesFromS3(String environment, String applicationName, String date) {
        return s3DumpService.listObjects(environment, applicationName, getDateForDump(date));
    }

    private String getDateForDump(String date) {
        if (date != null) {
            return date;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

     public boolean isDateValid(@RequestParam(required = false) String date) {
        if (date == null) {
            return false;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
        } catch (ParseException e) {
            logger.error("error parsing the date in yyyy-MM-dd format", e);
            return false;
        }

        return true;
    }

    public List<ApplicationSecret> initializeApplicaitonSecrets(String environmentName, ApplicationFamily applicationFamily, String applicationId, List<ApplicationSecret> applicationSecrets) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = applicationFamily.getEnvironment(environmentName);
        applicationSecrets.parallelStream().forEach(x -> {
            x.setEnvironmentName(environmentName);
            x.setApplicationFamily(applicationFamily);
            x.setApplicationId(applicationId);
        });

        List<ApplicationSecret> savedSecrets = secretService.getApplicationSecrets(environment.getName(), applicationFamily, applicationId);
        if (!Collections.disjoint(savedSecrets, applicationSecrets)) {
            throw new AlreadyExistsException("some secrets have already been created");
        }
        return secretService.initializeApplicationSecrets(applicationSecrets);
    }

    public List<ApplicationSecret> getApplicaitonSecrets(String environmentName, ApplicationFamily applicationFamily, String applicationId) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        List<ApplicationSecret> applicationSecrets = secretService.getApplicationSecrets(environmentName, applicationFamily, applicationId);
        applicationSecrets.forEach(x -> x.setSecretValue(""));

        return applicationSecrets;
    }

    public List<ApplicationSecret> updateApplicaitonSecrets(String environmentName, ApplicationFamily applicationFamily, String applicationId, List<ApplicationSecret> applicationSecrets) {
        Application application = applicationRepository.findOneByApplicationFamilyAndId(applicationFamily, applicationId).get();
        Environment environment = applicationFamily.getEnvironment(environmentName);

        if (!doSecretsExist(environmentName, applicationFamily, applicationId, applicationSecrets)) {
            throw new NotFoundException("some secrets have not been created");
        }
        String releaseName = helmService.getReleaseName(application, environment);
        kubectlService.createOrUpdateSecrets(environment, releaseName + "-credentials", applicationSecrets);
        return secretService.updateApplicationSecrets(environmentName, applicationFamily, applicationId, applicationSecrets);
    }

    private boolean doSecretsExist(String environmentName, ApplicationFamily applicationFamily, String applicationId, List<ApplicationSecret> applicationSecrets) {
        List<ApplicationSecret> savedSecrets = secretService.getApplicationSecrets(environmentName, applicationFamily, applicationId);
        return savedSecrets.parallelStream().map(ApplicationSecret::getSecretName).collect(Collectors.toList())
                .containsAll(applicationSecrets.parallelStream().map(ApplicationSecret::getSecretName).collect(Collectors.toList()));
    }
}
