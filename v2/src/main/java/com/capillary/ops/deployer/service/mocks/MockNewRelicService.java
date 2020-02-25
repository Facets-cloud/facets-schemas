package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.service.newrelic.INewRelicService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("dev")
@Service
public class MockNewRelicService implements INewRelicService {

    @Override
    public void upsertDashboard(Application application, Environment environment) {

    }

    @Override
    public String getDashboardURL(Application application, Environment environment) {
        return null;
    }

    @Override
    public void deleteDashboard(Application application, Environment environment) {

    }

    @Override
    public void createAlerts(Application application, Environment environment) {

    }

    @Override
    public void disableAlerts(Application application, Environment environment) {

    }

    @Override
    public String getAlertsURL(Application application, Environment environment) {
        return null;
    }
}
