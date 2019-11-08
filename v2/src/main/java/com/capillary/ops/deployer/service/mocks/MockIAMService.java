package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.service.interfaces.IHelmService;
import com.capillary.ops.deployer.service.interfaces.IIAMService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;

@Profile("dev")
@Service
public class MockIAMService implements IIAMService {
    @Override
    public String getApplicationRole(Application application, Environment environment) {
        return "mockApp";
    }
}
