package com.capillary.ops.deployer.service.helm;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.bo.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CronJobChartValueProvider extends AbstractValueProvider {

    @Override
    public Map<String, Object> getValues(Application application, Environment environment, Deployment deployment) {
        Map<String, Object> yaml = new HashMap<>();
        yaml.putAll(this.addBaseDetails(application, environment, deployment));
        this.addField("schedule", getSchedule(deployment), yaml);
        this.addFields(getFamilySpecificAttributes(application, deployment), yaml);
        return yaml;
    }
}
