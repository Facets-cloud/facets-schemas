package com.capillary.ops.deployer.service.newrelic;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Environment;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;

public interface INewRelicService {
    void upsertDashboard(Application application,
                         Environment environment);

    String getDashboardURL(Application application, Environment environment);

    void deleteDashboard(Application application, Environment environment);

    void createAlerts(Application application, Environment environment);

    void disableAlerts(Application application, Environment environment);

    String getAlertsURL(Application application, Environment environment);

    public Map<String, Double> getMetrics(String applicationName, Date startDate, Date endDate);
}
