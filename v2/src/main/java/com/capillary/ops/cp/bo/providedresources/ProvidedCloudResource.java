package com.capillary.ops.cp.bo.providedresources;

public class ProvidedCloudResource extends AbstractProvidedResource{
  private String region;
  private String name;

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
