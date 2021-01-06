package com.capillary.ops.cp.bo;


import com.google.gson.annotations.SerializedName;

public class ResourceDetails {
    @SerializedName("name")
    private String name;

    @SerializedName("resource_type")
    private String resourceType;

    @SerializedName("resource_name")
    private String resourceName;

    @SerializedName("key")
    private String key;

    @SerializedName("value")
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
