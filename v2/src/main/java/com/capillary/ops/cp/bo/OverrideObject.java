package com.capillary.ops.cp.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * All overrides together more to come
 */
@Document
@CompoundIndex(def = "{'clusterId':1, 'resourceType':1, 'resourceName':1}", name = "uniqueRTPerCluster")
public class OverrideObject {

    @Id
    private String id;

    @JsonIgnore
    @Indexed(name = "cluster_id_index", unique = false)
    private String clusterId;

    private String resourceType;

    private String resourceName;

    private Map<String, Object> overrides;

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

    public Map<String, Object> getOverrides() {
        return overrides;
    }

    public void setOverrides(Map<String, Object> overrides) {
        this.overrides = overrides;
    }

    @Override
    public String toString() {
        return "OverrideObject{" +
                "clusterId='" + clusterId + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", overrides=" + overrides +
                '}';
    }
}
