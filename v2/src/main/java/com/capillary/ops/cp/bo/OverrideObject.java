package com.capillary.ops.cp.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.JsonObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

/**
 * All overrides together more to come
 */
@Document
@CompoundIndex(def = "{'clusterId':1, 'resourceType':1, 'resourceName':1}", name = "uniqueRTPerCluster")
public class OverrideObject {

    @Id
    private String id;

    @JsonIgnore
    @Indexed(name = "cluster_id_index", unique = true)
    private String clusterId;

    private String resourceType;

    private String resourceName;

    private JsonObject overrides;


    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
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

    public JsonObject getOverrides() {
        return overrides;
    }

    public void setOverrides(JsonObject overrides) {
        this.overrides = overrides;
    }
}
