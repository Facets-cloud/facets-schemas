package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.service.interfaces.IHelmService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("dev")
@Service
public class MockHelmService implements IHelmService {

    @Override
    public void deploy(Application application, Deployment deployment) {

    }

    @Override
    public String getReleaseName(Application application, Environment environment) {
        return application.getName();
    }

    @Override
    public void rollback(Application application, Environment environmentName){

    }
}
