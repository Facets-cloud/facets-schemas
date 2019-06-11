package com.capillary.ops.deployer.bo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationDeploymentDetails {

    public ApplicationDeploymentDetails() {}

    public ApplicationDeploymentDetails(String name, Map<String, String> environmentConfigs, List<String> credentialsList, PodReplicationDetails replicas, Map<String, String> labels, HPADetails hpaDetails, String creationTimestamp) {
        this.name = name;
        this.environmentConfigs = environmentConfigs;
        this.credentialsList = credentialsList;
        this.replicas = replicas;
        this.labels = labels;
        this.hpaStatus = hpaDetails;
        this.creationTimestamp = creationTimestamp;
    }

    private String name;

    private Map<String, String> environmentConfigs;

    private List<String> credentialsList;

    private PodReplicationDetails replicas;

    private Map<String, String> labels = new HashMap<>();

    private HPADetails hpaStatus;

    private String creationTimestamp;

    public String getName() {
        return name;
    }

    public Map<String, String> getEnvironmentConfigs() {
        return environmentConfigs;
    }

    public void setEnvironmentConfigs(Map<String, String> environmentConfigs) {
        this.environmentConfigs = environmentConfigs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PodReplicationDetails getReplicas() {
        return replicas;
    }

    public void setReplicas(PodReplicationDetails replicas) {
        this.replicas = replicas;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public List<String> getCredentialsList() {
        return credentialsList;
    }

    public void setCredentialsList(List<String> credentialsList) {
        this.credentialsList = credentialsList;
    }

    public HPADetails getHpaStatus() {
        return hpaStatus;
    }

    public void setHpaStatus(HPADetails hpaStatus) {
        this.hpaStatus = hpaStatus;
    }
}
