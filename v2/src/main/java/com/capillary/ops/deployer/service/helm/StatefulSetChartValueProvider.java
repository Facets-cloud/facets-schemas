package com.capillary.ops.deployer.service.helm;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.bo.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StatefulSetChartValueProvider extends AbstractValueProvider {

    @Override
    public Map<String, Object> getValues(Application application, Environment environment, Deployment deployment) {
        Map<String, Object> yaml = new HashMap<>();
        yaml.putAll(this.addBaseDetails(application, environment, deployment));
        this.addField("persistentVolumeClaims", getPVCList(application), yaml);
        this.addField("lbType", getLbType(application), yaml);
        this.addField("sslCertName", getSSLCertificateName(application, environment), yaml);
        this.addField("domainName", getPrivateZoneDns(application, environment), yaml);
        this.addField("domainName", getPublicZoneDns(application, environment), yaml);
        this.addFields(getFamilySpecificAttributes(application, deployment), yaml);
        //this.addFields(getHPAConfigs(deployment), yaml);
        this.addFields(getHealthCheckConfigs(application), yaml);
        this.addField("elbIdleTimeoutSeconds", application.getElbIdleTimeoutSeconds(), yaml);
        this.addFields(getPortDetails(application), yaml);
        return yaml;
    }
}
