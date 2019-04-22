package com.capillary.ops.deployer.bo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceCheckDetails {

    public enum ServiceType {
        ClusterIP,
        NodePort,
        LoadBalancer,
        ExternalName
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PortMapping {
        public PortMapping(String protocol, String endpoint) {
            this.protocol = protocol;
            this.endpoint = endpoint;
        }

        String protocol;

        String endpoint;
    }

    private String name;

    private ServiceType serviceType;

    private List<PortMapping> internalEndpoints = new ArrayList<>();

    private List<PortMapping> externalEndpoints = new ArrayList<>();

    private Map<String, String> labels = new HashMap<>();

    private Map<String, String> selectors = new HashMap<>();

    private String creationTimestamp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public List<PortMapping> getInternalEndpoints() {
        return internalEndpoints;
    }

    public void setInternalEndpoints(List<PortMapping> internalEndpoints) {
        this.internalEndpoints = internalEndpoints;
    }

    public List<PortMapping> getExternalEndpoints() {
        return externalEndpoints;
    }

    public void setExternalEndpoints(List<PortMapping> externalEndpoints) {
        this.externalEndpoints = externalEndpoints;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public Map<String, String> getSelectors() {
        return selectors;
    }

    public void setSelectors(Map<String, String> selectors) {
        this.selectors = selectors;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }
}
