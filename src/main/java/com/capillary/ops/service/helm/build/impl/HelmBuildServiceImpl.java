package com.capillary.ops.service.helm.build.impl;

import com.capillary.ops.bo.helm.BuildStatus;
import com.capillary.ops.service.helm.build.BuildService;

import java.util.List;

public class HelmBuildServiceImpl implements BuildService {
    @Override
    public BuildStatus triggerBuild(String appName, String branch) {
        return null;
    }

    @Override
    public BuildStatus getBuildStatus(String appName, String buildId) {
        return null;
    }

    @Override
    public List<BuildStatus> getAllBuilds(String appName, boolean pending) {
        return null;
    }
}
