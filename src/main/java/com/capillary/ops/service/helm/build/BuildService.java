package com.capillary.ops.service.helm.build;

import com.capillary.ops.bo.helm.BuildStatus;
import com.capillary.ops.bo.helm.HelmApplication;

import java.util.List;

public interface BuildService {

    public BuildStatus triggerBuild(String appName, String branch);

    public BuildStatus getBuildStatus(String buildId);

    public List<BuildStatus> getAllBuilds(String appName, boolean pending);

    public void createEcrRepository(HelmApplication helmApplication);
}
