package com.capillary.ops.service.helm.build.impl;

import com.amazonaws.services.codebuild.AWSCodeBuild;
import com.amazonaws.services.ecr.AmazonECR;
import com.amazonaws.services.ecr.model.CreateRepositoryRequest;
import com.capillary.ops.bo.helm.BuildStatus;
import com.capillary.ops.service.helm.build.BuildService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class HelmBuildServiceImpl implements BuildService {

    @Autowired
    private AWSCodeBuild awsCodeBuild;

    @Autowired
    private AmazonECR amazonECR;

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
}
