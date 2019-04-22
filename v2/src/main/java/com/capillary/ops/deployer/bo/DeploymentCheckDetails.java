package com.capillary.ops.deployer.bo;

import java.util.HashMap;
import java.util.Map;

public class DeploymentCheckDetails {

    private String name;

    private Map<String, Integer> replicas = new HashMap<>();

    private Map<String, String> labels = new HashMap<>();

    private String creationTimestamp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getReplicas() {
        return replicas;
    }

    public void setReplicas(Map<String, Integer> replicas) {
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
