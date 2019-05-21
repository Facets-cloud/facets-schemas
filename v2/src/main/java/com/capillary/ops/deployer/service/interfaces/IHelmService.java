package com.capillary.ops.deployer.service.interfaces;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.bo.Environment;

public interface IHelmService {
    void deploy(Application application, Deployment deployment);

    String getReleaseName(Application application, Environment environment);

    void rollback(Application application, Environment environmentName);
}
