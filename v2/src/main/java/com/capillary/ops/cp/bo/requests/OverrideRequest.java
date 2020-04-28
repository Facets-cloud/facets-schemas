package com.capillary.ops.cp.bo.requests;

import com.google.gson.JsonObject;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

public class OverrideRequest {

    @NotNull
    private String resourceType;

    @NotNull
    private String resourceName;

    @NotNull
    private JsonObject overrides;

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

    public JsonObject getOverrides() {
        return overrides;
    }

    public void setOverrides(JsonObject overrides) {
        this.overrides = overrides;
    }
}
