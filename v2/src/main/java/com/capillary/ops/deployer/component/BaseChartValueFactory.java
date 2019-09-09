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
public class BaseChartValueFactory {

    @Autowired
    private IHelmValueComponents helmValueComponents;

    Map<String, Object> yaml;

    @PostConstruct
    public void init() {
        yaml = new HashMap<>();
    }

    boolean addField(String key, Object value) {
        if (key != null) {
            this.yaml.put(key, value);
            return true;
        }

        return false;
    }

    boolean addFields(Map<String, Object> value) {
        if (value != null) {
            this.yaml.putAll(value);
            return true;
        }

        return false;
    }

    public Map<String, Object> getValues(Application application, Environment environment, Deployment deployment) {
        IHelmValueComponents components = BaseChartValueFactory.this.helmValueComponents;
        this.addBaseDetails(application, environment, deployment);
        this.addField("lbType", components.getLbType(application));
        this.addField("sslCertName", components.getSSLCertificateName(application, environment));
        this.addField("domainName", components.getPrivateZoneDns(application, environment));
        this.addField("domainName", components.getPublicZoneDns(application, environment));
        this.addFields(components.getFamilySpecificAttributes(application, deployment));
        this.addFields(components.getHPAConfigs(deployment));
        this.addFields(components.getHealthCheckConfigs(application));

        return this.yaml;
    }

    public void addBaseDetails(Application application, Environment environment, Deployment deployment) {
        IHelmValueComponents components = this.helmValueComponents;
        this.addField("deploymentId", components.getDeploymentId(deployment));
        this.addField("buildId", components.getBuildId(deployment));
        this.addField("image", components.getImage(deployment));
        this.addField("podCPULimit", components.getPodCPULimit(deployment));
        this.addField("podMemoryLimit", components.getPodMemoryLimit(deployment));
        this.addField("nodeSelector", components.getNodeGroup(environment));
        this.addField("ports", components.getPorts(application));
        this.addField("configurations", components.getConfigMap(environment, application, deployment));
        this.addField("credentials", components.getCredentialsMap(environment, application));
        this.addField("secretFileMounts", components.getSecretFileMounts(application, environment, deployment));
    }
}
