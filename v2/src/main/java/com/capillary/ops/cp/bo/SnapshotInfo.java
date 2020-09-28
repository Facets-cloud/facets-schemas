package com.capillary.ops.cp.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.time.Instant;

public class SnapshotInfo {
    public SnapshotInfo() {}

    public SnapshotInfo(String clusterId, String name, String cloudSpecificId, String resourceType, String instanceName, String source,
                        Instant startTime, Instant endTime, Integer storageSize, boolean pinned) {
        this.clusterId = clusterId;
        this.name = name;
        this.cloudSpecificId = cloudSpecificId;
        this.resourceType = resourceType;
        this.instanceName = instanceName;
        this.source = source;
        this.startTime = startTime;
        this.endTime = endTime;
        this.storageSize = storageSize;
        this.pinned = pinned;
    }

    public SnapshotInfo(String clusterId, String name, String cloudSpecificId, String resourceType, String instanceName,
                        String source, Instant startTime, Instant endTime, Integer storageSize) {
        this.clusterId = clusterId;
        this.name = name;
        this.cloudSpecificId = cloudSpecificId;
        this.resourceType = resourceType;
        this.instanceName = instanceName;
        this.source = source;
        this.startTime = startTime;
        this.endTime = endTime;
        this.storageSize = storageSize;
        this.pinned = false;
    }

    @Id
    @JsonIgnore
    private String id;

    private String clusterId;
    private String name;
    private String cloudSpecificId;
    private String resourceType;
    private String instanceName;
    private String source;
    private Instant startTime;
    private Instant endTime;
    private Integer storageSize;
    private boolean pinned;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

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

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getCloudSpecificId() {
        return cloudSpecificId;
    }

    public void setCloudSpecificId(String cloudSpecificId) {
        this.cloudSpecificId = cloudSpecificId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Integer getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(Integer storageSize) {
        this.storageSize = storageSize;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}
