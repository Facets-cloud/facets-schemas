package com.capillary.ops.bo;

import java.util.LinkedHashMap;
import java.util.Map;

public class HelmInfrastructureResource extends AbstractDeploymentResource {

  private String repository;

  private String chartVersion;

  private String appVersion;

  private String description;

  private String deploymentName;

  private Map<String, Object> valueParams = new LinkedHashMap<>();

  public HelmInfrastructureResource() {}

  public HelmInfrastructureResource(
      String deploymentName, String type, Map<String, Object> valueParams) {
    this.deploymentName = deploymentName;
    this.type = type;
    this.valueParams = valueParams;
  }

  public HelmInfrastructureResource(String type, Map<String, Object> valueParams) {
    this(null, type, valueParams);
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Map<String, Object> getValueParams() {
    return valueParams;
  }

  public void setValueParams(Map<String, Object> valueParams) {
    this.valueParams = valueParams;
  }

  public String getRepository() {
    return repository;
  }

  public void setRepository(String repository) {
    this.repository = repository;
  }

  public String getChartVersion() {
    return chartVersion;
  }

  public void setChartVersion(String chartVersion) {
    this.chartVersion = chartVersion;
  }

  public String getAppVersion() {
    return appVersion;
  }

  public void setAppVersion(String appVersion) {
    this.appVersion = appVersion;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDeploymentName() {
    return deploymentName;
  }

  public void setDeploymentName(String deploymentName) {
    this.deploymentName = deploymentName;
  }

  @Override
  public String toString() {
    return "HelmInfrastructureResource{"
        + "repository='"
        + repository
        + '\''
        + ", chartVersion='"
        + chartVersion
        + '\''
        + ", appVersion='"
        + appVersion
        + '\''
        + ", description='"
        + description
        + '\''
        + ", deploymentName='"
        + deploymentName
        + '\''
        + ", valueParams="
        + valueParams
        + ", id='"
        + id
        + '\''
        + ", type='"
        + type
        + '\''
        + ", resourceName='"
        + resourceName
        + '\''
        + '}';
  }
}
