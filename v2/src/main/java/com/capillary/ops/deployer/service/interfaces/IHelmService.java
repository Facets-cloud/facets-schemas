package com.capillary.ops.deployer.service.interfaces;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.bo.Environment;

import java.util.Map;

public interface IHelmService {
    void deploy(Application application, Deployment deployment);

    public void deploy(Environment environment, String releaseName, String chartName, Map<String, Object> valueMap);

    String getReleaseName(Application application, Environment environment);
}
