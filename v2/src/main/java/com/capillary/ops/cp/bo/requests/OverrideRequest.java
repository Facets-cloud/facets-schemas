package com.capillary.ops.cp.bo.requests;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class OverrideRequest {

    @NotNull
    private String resourceType;

    @NotNull
    private String resourceName;

    @JsonUnwrapped
    private Map<String, Object> overrides;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String toString() {
        return "OverrideRequest{" + "resourceType='" + resourceType + '\'' + ", resourceName='" + resourceName + '\''
            + ", overrides=" + overrides + '}';
    }

    public Map<String, Object> getOverrides() {
        return overrides;
    }

    public void setOverrides(Map<String, Object> overrides) {
        this.overrides = overrides;
    }
}
