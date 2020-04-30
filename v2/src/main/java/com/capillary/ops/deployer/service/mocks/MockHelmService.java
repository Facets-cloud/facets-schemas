package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.service.interfaces.IHelmService;
import com.github.alturkovic.lock.Interval;
import com.github.alturkovic.lock.redis.alias.RedisLocked;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Profile("dev & !helminttest")
@Service
public class MockHelmService implements IHelmService {

    @RedisLocked(
            expression = "#application.getApplicationFamily() + '_' + #deployment.getEnvironment() + '_' + #application.getId() + '_' + #deployment.getBuildId()",
            expiration = @Interval(value = "5", unit = TimeUnit.MINUTES)
    )
    @Override
    public void deploy(Application application, Deployment deployment) {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deploy(Environment environment, String releaseName, String chartName, Map<String, Object> valueMap) {

    }

    @Override
    public String getReleaseName(Application application, Environment environment) {
        return application.getName();
    }

    @Override
    public void rollback(Application application, Environment environmentName){

    }

    @Override
    public boolean doesReleaseExist(ApplicationFamily applicationFamily, Environment environment, String releaseName) {
        return true;
    }

    @Override
    public void purge(Application application, Environment environment) {
        return;
    }
}
