package com.capillary.ops.deployer.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.util.*;
import java.util.stream.Collectors;

public class Deployment {

    public enum ConcurrencyPolicy {
        Allow,
        Forbid,
        Replace
    }

    @Id
    private String id;
    private ApplicationFamily applicationFamily;
    private String applicationId;
    @JsonIgnore
    private String image;
    private String buildId;
    private String environment;
    private List<EnvironmentVariable> configurations;
    private Date timestamp = new Date();
    private PodSize podSize;
    private HPA horizontalPodAutoscaler;
    private String schedule;
    private boolean rollbackEnabled;
    private String deployedBy;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<EnvironmentVariable> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<EnvironmentVariable> configurations) {
        this.configurations = configurations;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public PodSize getPodSize() {
        return podSize;
    }

    public void setPodSize(PodSize podSize) {
        this.podSize = podSize;
    }

    public HPA getHorizontalPodAutoscaler() {
        return horizontalPodAutoscaler;
    }

    public void setHorizontalPodAutoscaler(HPA horizontalPodAutoscaler) {
        this.horizontalPodAutoscaler = horizontalPodAutoscaler;
    }

    public boolean isRollbackEnabled() {
        return rollbackEnabled;
    }

    public void setRollbackEnabled(boolean rollbackEnabled) {
        this.rollbackEnabled = rollbackEnabled;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public Map<String, String> getConfigurationsMap() {
        if(configurations == null) {
            return new HashMap<>();
        }
        return configurations.stream()
                .collect(Collectors.toMap(EnvironmentVariable::getName, EnvironmentVariable::getValue));
    }

    public ApplicationFamily getApplicationFamily() {
        return applicationFamily;
    }

    public void setApplicationFamily(ApplicationFamily applicationFamily) {
        this.applicationFamily = applicationFamily;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getDeployedBy() {
        return deployedBy;
    }

    public void setDeployedBy(String deployedBy) {
        this.deployedBy = deployedBy;
    }
}
