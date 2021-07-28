package com.capillary.ops.cp.bo.providedresources;

public class ProvidedUnauthenticatedResource extends AbstractProvidedResource{
  private String endpoint;
  private String port;

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }
}
