package com.capillary.ops.bo.helm;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelmApplication {

    public HelmApplication() {}

    public HelmApplication(ApplicationFamily applicationFamily, String name,
                           String instanceType, Integer replicas,
                           Map<String, String> configs, List<String> domains,
                           ExposureType exposureType, String repositoryUrl,
                           String pathFromRoot, List<Port> portMapping) {
        this.applicationFamily = applicationFamily;
        this.name = name;
        this.instanceType = instanceType;
        this.replicas = replicas;
        this.configs = configs;
        this.domains = domains;
        this.exposureType = exposureType;
        this.repositoryUrl = repositoryUrl;
        this.pathFromRoot = pathFromRoot;
        this.setPortMapping(portMapping);
    }

    public enum ExposureType {
        INTERNAL,
        EXTERNAL
    }

    public enum BuildType {
        MAVEN_JAVA,
        DOCKER,
        NETCORE
    }

    private ApplicationFamily applicationFamily;

    private String name;

    private String instanceType;

    private Integer replicas = 0;

    private Map<String, String> configs = new HashMap<>();

    private List<String> domains = new ArrayList<>();

    private ExposureType exposureType = ExposureType.INTERNAL;

    private String repositoryUrl;

    private String pathFromRoot = "";

    private BuildType buildType;

    private List<Port> portMapping;

    public BuildType getBuildType() {
        return buildType;
    }

    public void setBuildType(BuildType buildType) {
        this.buildType = buildType;
    }

    public ApplicationFamily getApplicationFamily() {
        return applicationFamily;
    }

    public void setApplicationFamily(ApplicationFamily applicationFamily) {
        this.applicationFamily = applicationFamily;
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

    public Map<String, String> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, String> configs) {
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

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public List<Port> getPortMapping() {
        return portMapping;
    }

    public void setPortMapping(List<Port> portMapping) {
        this.portMapping = portMapping;
    }

    public String getPathFromRoot() {
        return pathFromRoot;
    }

    public void setPathFromRoot(String pathFromRoot) {
        this.pathFromRoot = pathFromRoot;
    }


}
