package com.capillary.ops.deployer.service.helm;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.service.helm.impl.AbstractValueProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
