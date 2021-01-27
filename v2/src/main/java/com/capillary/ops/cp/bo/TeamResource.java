package com.capillary.ops.cp.bo;

import java.util.Objects;

public class TeamResource {
    private String stackName;
    private String resourceType;
    private String resourceName;

    public TeamResource() {
    }

    public TeamResource(String stackName, String resourceType, String resourceName) {
        this.stackName = stackName;
        this.resourceType = resourceType;
        this.resourceName = resourceName;
    }

    public String getStackName() {
        return stackName;
    }

    public void setStackName(String stackName) {
        this.stackName = stackName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamResource that = (TeamResource) o;
        return Objects.equals(stackName, that.stackName) &&
                Objects.equals(resourceType, that.resourceType) &&
                Objects.equals(resourceName, that.resourceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stackName, resourceType, resourceName);
    }
}
