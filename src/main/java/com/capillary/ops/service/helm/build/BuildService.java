package com.capillary.ops.service.helm.build;

import com.capillary.ops.bo.helm.BuildStatus;

import java.util.List;

public interface BuildService {

    public BuildStatus triggerBuild(String appName, String branch);

    public BuildStatus getBuildStatus(String appName, String buildId);

    public List<BuildStatus> getAllBuilds(String appName, boolean pending);
}
