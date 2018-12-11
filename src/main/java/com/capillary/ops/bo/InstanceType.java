package com.capillary.ops.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.HashMap;
import java.util.Map;

public class InstanceType {

    @Id
    @JsonIgnore
    private String id;

    @Indexed(unique = true)
    private String name;

    private String minCpu = "0";

    private String maxCpu = "0";

    private String minMemory = "0Mi";

    private String maxMemory = "0Mi";

    public String getName() {
        return name;
    }

    public String getMinCpu() {
        return minCpu;
    }

    public String getMaxCpu() {
        return maxCpu;
    }

    public String getMinMemory() {
        return minMemory;
    }

    public String getMaxMemory() {
        return maxMemory;
    }

    public void setMinMemory(String minMemory) {
        this.minMemory = !minMemory.endsWith("Mi") ? (minMemory + "Mi") : minMemory;
    }

    public void setMaxMemory(String maxMemory) {
        this.maxMemory = !maxMemory.endsWith("Mi") ? (maxMemory + "Mi") : maxMemory;
    }

    public Map<String, Object> toKubeConfig() {
        Map<String, Object> limits = new HashMap<>(2);
        limits.put("cpu", this.maxCpu);
        limits.put("memory", this.maxMemory);

        Map<String, Object> requests = new HashMap<>(2);
        requests.put("cpu", this.minCpu);
        requests.put("memory", this.minMemory);

        Map<String, Object> result = new HashMap<>(2);
        result.put("limits", limits);
        result.put("requests", requests);

        return result;
    }

    @Override
    public String toString() {
        return "InstanceType{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", minCpu='" + minCpu + '\'' +
                ", maxCpu='" + maxCpu + '\'' +
                ", minMemory='" + minMemory + '\'' +
                ", maxMemory='" + maxMemory + '\'' +
                '}';
    }
}
