package com.capillary.ops.deployer.service.helm;

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
public class StatefulSetChartValueProvider extends BaseChartValueProvider {

    @Autowired
    IHelmValueComponents helmValueComponents;

    @Override
    public Map<String, Object> getValues(Application application, Environment environment, Deployment deployment) {
        Map<String, Object> yaml = new HashMap<>();
        IHelmValueComponents components = this.helmValueComponents;
        this.addBaseDetails(application, environment, deployment);
        this.addField("persistentVolumeClaims", components.getPVCList(application), yaml);
        this.addField("lbType", components.getLbType(application), yaml);
        this.addField("sslCertName", components.getSSLCertificateName(application, environment), yaml);
        this.addField("domainName", components.getPrivateZoneDns(application, environment), yaml);
        this.addField("domainName", components.getPublicZoneDns(application, environment), yaml);
        this.addFields(components.getFamilySpecificAttributes(application, deployment), yaml);
        this.addFields(components.getHPAConfigs(deployment), yaml);
        this.addFields(components.getHealthCheckConfigs(application), yaml);
        return yaml;
    }
}
