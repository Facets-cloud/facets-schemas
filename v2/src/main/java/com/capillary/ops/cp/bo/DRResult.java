package com.capillary.ops.cp.bo;

public class DRResult {
    public enum DRStatus {
        SUCCESS,
        FAILURE
    }

    public enum DRAction {
        CREATE_SNAPSHOT,
        DELETE_SNAPSHOT
    }

    private String resourceType;

    private String instanceName;

    private DRAction action;

    private DRStatus status;

    private String exception;

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

    public DRAction getAction() {
        return action;
    }

    public void setAction(DRAction action) {
        this.action = action;
    }

    public DRStatus getStatus() {
        return status;
    }

    public void setStatus(DRStatus status) {
        this.status = status;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "DRResult{" +
                "resourceType='" + resourceType + '\'' +
                ", instanceName='" + instanceName + '\'' +
                ", action=" + action +
                ", status=" + status +
                ", exception='" + exception + '\'' +
                '}';
    }
}
