package com.capillary.ops.deployer.service.newrelic;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Environment;

import java.io.IOException;
import java.net.URISyntaxException;

public interface INewRelicService {
    void upsertDashboard(Application application,
                         Environment environment);

    String getDashboardURL(Application application, Environment environment);

    void deleteDashboard(Application application, Environment environment);

    String createAlerts(Application application, Environment environment);
}
