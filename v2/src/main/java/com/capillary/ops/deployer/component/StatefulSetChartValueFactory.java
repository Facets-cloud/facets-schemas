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
public class StatefulSetChartValueFactory extends BaseChartValueFactory {

    @Autowired
    IHelmValueComponents helmValueComponents;

    @PostConstruct
    public void init() {
        yaml = new HashMap<>();
    }

    @Override
    public Map<String, Object> getValues(Application application, Environment environment, Deployment deployment) {
        IHelmValueComponents components = this.helmValueComponents;
        this.addBaseDetails(application, environment, deployment);
        this.addField("persistentVolumeClaims", components.getPVCList(application));
        this.addField("lbType", components.getLbType(application));
        this.addField("sslCertName", components.getSSLCertificateName(application, environment));
        this.addField("domainName", components.getPrivateZoneDns(application, environment));
        this.addField("domainName", components.getPublicZoneDns(application, environment));
        this.addFields(components.getFamilySpecificAttributes(application, deployment));
        this.addFields(components.getHPAConfigs(deployment));
        this.addFields(components.getHealthCheckConfigs(application));

        return this.yaml;
    }
}
