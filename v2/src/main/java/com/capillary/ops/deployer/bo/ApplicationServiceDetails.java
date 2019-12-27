package com.capillary.ops.deployer.bo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationServiceDetails {

    public ApplicationServiceDetails() {}

    public ApplicationServiceDetails(String name, ServiceType serviceType, List<Endpoint> internalEndpoints, List<Endpoint> externalEndpoints, Map<String, String> labels, Map<String, String> selectors, String creationTimestamp, String externalDns) {
        this.name = name;
        this.serviceType = serviceType;
        this.internalEndpoints = internalEndpoints;
        this.externalEndpoints = externalEndpoints;
        this.labels = labels;
        this.selectors = selectors;
        this.creationTimestamp = creationTimestamp;
        this.externalDns = externalDns;
    }

    public enum ServiceType {
        ClusterIP,
        NodePort,
        LoadBalancer,
        ExternalName
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Endpoint {
        public Endpoint(String protocol, String endpoint) {
            this.protocol = protocol;
            this.endpoint = endpoint;
        }

        String protocol;

        String endpoint;

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
    }

    private String name;

    private ServiceType serviceType;

    private List<Endpoint> internalEndpoints = new ArrayList<>();

    private List<Endpoint> externalEndpoints = new ArrayList<>();

    private Map<String, String> labels = new HashMap<>();

    private Map<String, String> selectors = new HashMap<>();

    private String creationTimestamp;

    private String externalDns;

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

    public List<Endpoint> getInternalEndpoints() {
        return internalEndpoints;
    }

    public void setInternalEndpoints(List<Endpoint> internalEndpoints) {
        this.internalEndpoints = internalEndpoints;
    }

    public List<Endpoint> getExternalEndpoints() {
        return externalEndpoints;
    }

    public void setExternalEndpoints(List<Endpoint> externalEndpoints) {
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

    public String getExternalDns() {
        return externalDns;
    }

    public void setExternalDns(String externalDns) {
        this.externalDns = externalDns;
    }
}
