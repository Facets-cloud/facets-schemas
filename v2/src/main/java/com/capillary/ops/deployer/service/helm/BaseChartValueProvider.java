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
public class BaseChartValueProvider extends AbstractValueProvider {

    public Map<String, Object> getValues(Application application, Environment environment, Deployment deployment) {
        Map<String, Object> yaml = new HashMap<>();
        yaml.putAll(this.addBaseDetails(application, environment, deployment));
        this.addField("lbType", getLbType(application), yaml);
        this.addField("sslCertName", getSSLCertificateName(application, environment), yaml);
        this.addField("domainName", getPrivateZoneDns(application, environment), yaml);
        this.addField("domainName", getPublicZoneDns(application, environment), yaml);
        this.addFields(getFamilySpecificAttributes(application, deployment), yaml);
        this.addFields(getHPAConfigs(deployment), yaml);
        this.addFields(getHealthCheckConfigs(application), yaml);
        return yaml;
    }

}
