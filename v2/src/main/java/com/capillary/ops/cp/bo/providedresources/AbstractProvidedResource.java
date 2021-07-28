package com.capillary.ops.cp.bo.providedresources;

abstract public class AbstractProvidedResource {
  private String resourceType;
  private String resourceName;

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
}
