package com.capillary.ops.deployer.component;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.bo.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@Scope("prototype")
public class CronJobChartValueFactory extends BaseChartValueFactory {

    @Autowired
    private IHelmValueComponents helmValueComponents;

    @PostConstruct
    public void init() {
        yaml = new HashMap<>();
    }

    @Override
    public Map<String, Object> getValues(Application application, Environment environment, Deployment deployment) {
        IHelmValueComponents components = this.helmValueComponents;
        this.addBaseDetails(application, environment, deployment);
        this.addField("schedule", components.getSchedule(deployment));
        this.addFields(components.getFamilySpecificAttributes(application, deployment));
        return this.yaml;
    }
}
