package com.capillary.ops.deployer.bo;

import java.util.HashMap;
import java.util.Map;

public class ApplicationDeploymentDetails {

    public ApplicationDeploymentDetails() {}

    public ApplicationDeploymentDetails(String name, PodReplicationDetails replicas, Map<String, String> labels, String creationTimestamp) {
        this.name = name;
        this.replicas = replicas;
        this.labels = labels;
        this.creationTimestamp = creationTimestamp;
    }

    private String name;

    private PodReplicationDetails replicas;

    private Map<String, String> labels = new HashMap<>();

    private String creationTimestamp;

    public String getName() {
        return name;
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
}
