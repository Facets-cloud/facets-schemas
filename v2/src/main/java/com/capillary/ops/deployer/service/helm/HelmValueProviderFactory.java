package com.capillary.ops.deployer.service.helm;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HelmValueProviderFactory {

    @Autowired
    private BaseChartValueProvider baseChartValueProvider;

    @Autowired
    private StatefulSetChartValueProvider statefulSetChartValueProvider;

    @Autowired
    public CronJobChartValueProvider cronJobChartValueProvider;

    public Map<String, Object> getValues(Application application, Environment environment, Deployment deployment) {
        switch (application.getApplicationType()) {
            case SERVICE:
                return baseChartValueProvider.getValues(application, environment, deployment);
            case STATEFUL_SET:
                return statefulSetChartValueProvider.getValues(application, environment, deployment);
            case SCHEDULED_JOB:
                return cronJobChartValueProvider.getValues(application, environment, deployment);
            default:
                return baseChartValueProvider.getValues(application, environment, deployment);
        }
    }
}
