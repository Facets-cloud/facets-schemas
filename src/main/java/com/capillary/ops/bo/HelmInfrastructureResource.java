package com.capillary.ops.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.util.LinkedHashMap;
import java.util.Map;

public class HelmInfrastructureResource {

    @Id
    @JsonIgnore
    private String id;

    private String appName;

    private String type;

    private String repository;

    private String chartVersion;

    private String appVersion;

    private String description;

    private Map<String, Object> valueParams = new LinkedHashMap<>();

    public HelmInfrastructureResource() {}

    public HelmInfrastructureResource(String appName, String type, Map<String, Object> valueParams) {
        this.appName = appName;
        this.type = type;
        this.valueParams = valueParams;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getValueParams() {
        return valueParams;
    }

    public void setValueParams(Map<String, Object> valueParams) {
        this.valueParams = valueParams;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getChartVersion() {
        return chartVersion;
    }

    public void setChartVersion(String chartVersion) {
        this.chartVersion = chartVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "HelmInfrastructureResource{" +
                "id='" + id + '\'' +
                ", appName='" + appName + '\'' +
                ", type='" + type + '\'' +
                ", repository='" + repository + '\'' +
                ", chartVersion='" + chartVersion + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", description='" + description + '\'' +
                ", valueParams=" + valueParams +
                '}';
    }
}
