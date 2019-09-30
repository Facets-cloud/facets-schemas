package com.capillary.ops.deployer.service.helm;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HelmValueProviderFactory {

    @Lookup
    public BaseChartValueProvider getHelmValuesBuilder() {
        return null;
    }

    @Lookup
    public StatefulSetChartValueProvider getStatefulSetValuesBuilder() {
        return null;
    }

    @Lookup
    public CronJobChartValueProvider getCronJobBuilder() {
        return null;
    }

    public Map<String, Object> getValues(String chart, Application application, Environment environment, Deployment deployment) {
        switch (chart) {
            case "capillary-base":
                return getHelmValuesBuilder().getValues(application, environment, deployment);
            case "capillary-base-statefulset":
                return getStatefulSetValuesBuilder().getValues(application, environment, deployment);
            case "capillary-base-cronjob":
                return getCronJobBuilder().getValues(application, environment, deployment);
        }

        throw new  NotFoundException("could not find a valid deployment configuration");
    }
}
