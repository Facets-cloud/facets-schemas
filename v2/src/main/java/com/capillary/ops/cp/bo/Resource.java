package com.capillary.ops.cp.bo;

import java.util.Objects;

public class Resource{
    private String resourceType;
    private String resourceName;

  public Resource() {
  }

  public Resource(String resourceType, String resourceName) {
    this.resourceType = resourceType;
    this.resourceName = resourceName;
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
    Resource resource = (Resource) o;
    return resourceType.equals(resource.resourceType) && resourceName.equals(resource.resourceName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resourceType, resourceName);
  }
}
