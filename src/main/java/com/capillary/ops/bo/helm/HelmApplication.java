package com.capillary.ops.bo.helm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelmApplication {

    public HelmApplication() {}

    public HelmApplication(HelmApplicationType helmApplicationType, String name, String instanceType, Integer replicas, Map<String, Object> configs, List<String> domains, ExposureType exposureType) {
        this.helmApplicationType = helmApplicationType;
        this.name = name;
        this.instanceType = instanceType;
        this.replicas = replicas;
        this.configs = configs;
        this.domains = domains;
        this.exposureType = exposureType;
    }

    public enum ExposureType {
        INTERNAL,
        EXTERNAL
    }

    private HelmApplicationType helmApplicationType;

    private String name;

    private String instanceType;

    private Integer replicas = 0;

    private Map<String, Object> configs = new HashMap<>();

    private List<String> domains = new ArrayList<>();

    private ExposureType exposureType = ExposureType.INTERNAL;

    public HelmApplicationType getHelmApplicationType() {
        return helmApplicationType;
    }

    public void setHelmApplicationType(HelmApplicationType helmApplicationType) {
        this.helmApplicationType = helmApplicationType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }

    public Map<String, Object> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, Object> configs) {
        this.configs = configs;
    }

    public List<String> getDomains() {
        return domains;
    }

    public void setDomains(List<String> domains) {
        this.domains = domains;
    }

    public ExposureType getExposureType() {
        return exposureType;
    }

    public void setExposureType(ExposureType exposureType) {
        this.exposureType = exposureType;
    }
}
