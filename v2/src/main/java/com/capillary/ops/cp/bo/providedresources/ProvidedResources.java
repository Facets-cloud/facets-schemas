package com.capillary.ops.cp.bo.providedresources;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document
public class ProvidedResources {
  @Id
  private String id;
  @Indexed(unique = true)
  private String clusterId;
  private List<ProvidedUnauthenticatedResource> providedUnauthenticatedResources = new ArrayList<>();
  private List<ProvidedAuthenticatedResource> providedAuthenticatedResources = new ArrayList<>();
  private List<ProvidedShardedService> providedShardedResources = new ArrayList<>();
  private List<ProvidedCloudResource> providedCloudResources = new ArrayList<>();

  public ProvidedResources() {
  }

  public ProvidedResources(String id) {
    this.id = id;
  }

  public List<ProvidedUnauthenticatedResource> getProvidedUnauthenticatedResources() {
    return providedUnauthenticatedResources;
  }

  public void setProvidedUnauthenticatedResources(List<ProvidedUnauthenticatedResource> providedUnauthenticatedResources) {
    this.providedUnauthenticatedResources = providedUnauthenticatedResources;
  }

  public List<ProvidedAuthenticatedResource> getProvidedAuthenticatedResources() {
    return providedAuthenticatedResources;
  }

  public void setProvidedAuthenticatedResources(List<ProvidedAuthenticatedResource> providedAuthenticatedResources) {
    this.providedAuthenticatedResources = providedAuthenticatedResources;
  }

  public List<ProvidedShardedService> getProvidedShardedResources() {
    return providedShardedResources;
  }

  public void setProvidedShardedResources(List<ProvidedShardedService> providedShardedResources) {
    this.providedShardedResources = providedShardedResources;
  }

  public List<ProvidedCloudResource> getProvidedCloudResources() {
    return providedCloudResources;
  }

  public void setProvidedCloudResources(List<ProvidedCloudResource> providedCloudResources) {
    this.providedCloudResources = providedCloudResources;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
