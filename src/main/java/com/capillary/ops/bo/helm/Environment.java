package com.capillary.ops.bo.helm;

public class Environment {

  private String name;

  private String kubernetesAPIEndpoint;

  private String nodeGroup;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getKubernetesAPIEndpoint() {
    return kubernetesAPIEndpoint;
  }

  public void setKubernetesAPIEndpoint(String kubernetesAPIEndpoint) {
    this.kubernetesAPIEndpoint = kubernetesAPIEndpoint;
  }

  public String getNodeGroup() {
    return nodeGroup;
  }

  public void setNodeGroup(String nodeGroup) {
    this.nodeGroup = nodeGroup;
  }
}
