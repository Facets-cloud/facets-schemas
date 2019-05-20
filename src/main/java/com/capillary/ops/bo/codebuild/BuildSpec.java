package com.capillary.ops.bo.codebuild;

import java.util.Map;

public class BuildSpec {
  private String version;
  private Map<String, Object> phases;
  private Map<String, Object> cache;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Map<String, Object> getPhases() {
    return phases;
  }

  public void setPhases(Map<String, Object> phases) {
    this.phases = phases;
  }

  public Map<String, Object> getCache() {
    return cache;
  }

  public void setCache(Map<String, Object> cache) {
    this.cache = cache;
  }
}
