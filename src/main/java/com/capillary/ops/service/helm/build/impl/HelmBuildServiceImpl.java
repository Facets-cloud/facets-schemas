package com.capillary.ops.service.helm.build.impl;

import com.capillary.ops.bo.helm.BuildStatus;
import com.capillary.ops.bo.helm.HelmApplication;
import com.capillary.ops.service.helm.build.BuildService;
import com.capillary.ops.service.helm.impl.HelmEnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ecr.EcrClient;
import software.amazon.awssdk.services.ecr.model.CreateRepositoryRequest;
import software.amazon.awssdk.services.ecr.model.CreateRepositoryResponse;
import software.amazon.awssdk.services.ecr.model.SetRepositoryPolicyRequest;

import java.util.List;

@Service
public class HelmBuildServiceImpl implements BuildService {

    @Autowired
    private EcrClient ecrClient;

    @Autowired
    private HelmEnvironmentService helmEnvironmentService;

    @Override
    public BuildStatus triggerBuild(String appName, String branch) {
        return null;
    }

    @Override
    public BuildStatus getBuildStatus(String buildId) {
        return null;
    }

    @Override
    public List<BuildStatus> getAllBuilds(String appName, boolean pending) {
        return null;
    }

    @Override
    public void createEcrRepository(HelmApplication application) {
        String repositoryName = application.getName();
        System.out.println("creating repository with name = " + repositoryName);
        CreateRepositoryRequest createRepositoryRequest = CreateRepositoryRequest.builder()
                .repositoryName(application.getName())
                .build();

        ecrClient.createRepository(createRepositoryRequest);
        setEcrPolicy(application, repositoryName);
    }

    private void setEcrPolicy(HelmApplication application, String repositoryName) {
        SetRepositoryPolicyRequest policyRequest = SetRepositoryPolicyRequest.builder().repositoryName(repositoryName)
                .policyText(helmEnvironmentService.getEcrPolicyForFamily(application.getApplicationFamily())).build();

        ecrClient.setRepositoryPolicy(policyRequest);
    }
}
