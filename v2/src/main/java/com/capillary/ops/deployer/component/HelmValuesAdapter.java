package com.capillary.ops.deployer.component;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HelmValuesAdapter {

    @Lookup
    public BaseChartValueFactory getHelmValuesBuilder() {
        return null;
    }

    @Lookup
    public StatefulSetChartValueFactory getStatefulSetValuesBuilder() {
        return null;
    }

    @Lookup
    public CronJobChartValueFactory getCronJobBuilder() {
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
