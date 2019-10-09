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
    public BaseChartValueProvider getHelmValuesBuilder() {
        return null;
    }

    @Autowired
    public StatefulSetChartValueProvider getStatefulSetValuesBuilder() {
        return null;
    }

    @Autowired
    public CronJobChartValueProvider getCronJobBuilder() {
        return null;
    }

    public Map<String, Object> getValues(Application application, Environment environment, Deployment deployment) {
        switch (application.getApplicationType()) {
            case SERVICE:
                return getHelmValuesBuilder().getValues(application, environment, deployment);
            case STATEFUL_SET:
                return getStatefulSetValuesBuilder().getValues(application, environment, deployment);
            case SCHEDULED_JOB:
                return getCronJobBuilder().getValues(application, environment, deployment);
        }

        throw new  NotFoundException("could not find a valid deployment configuration");
    }
}
