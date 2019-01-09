package com.capillary.ops.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

public abstract class AbstractInfrastructureResource {

  @Id @JsonIgnore protected String id;

  protected String resourceName;

  /** Environment where the resource should be deployed, e.g., NIGHTLY, STAGE, PROD */
  protected Environments environment;

  /** Defines cpu and memory limits for the resource */
  protected String instanceType;

  /** Size of the physical volume */
  protected Integer volumeSize;

  /** Name of helm deployment Should not be exposed to the end user */
  @JsonIgnore protected String deploymentName;

  protected InfrastructureResourceStatus deploymentStatus = InfrastructureResourceStatus.PENDING;

  @JsonProperty
  public String getId() {
    return id;
  }

  public Environments getEnvironment() {
    return environment;
  }

  public void setEnvironment(Environments environment) {
    this.environment = environment;
  }

  public String getInstanceType() {
    return instanceType;
  }

  public void setInstanceType(String instanceType) {
    this.instanceType = instanceType;
  }

  public Integer getVolumeSize() {
    return volumeSize;
  }

  public void setVolumeSize(Integer volumeSize) {
    this.volumeSize = volumeSize;
  }

  public String getResourceName() {
    return resourceName;
  }

  public String getDeploymentName() {
    return deploymentName;
  }

  public void setDeploymentName(String deploymentName) {
    this.deploymentName = deploymentName;
  }

  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }

  public InfrastructureResourceStatus getDeploymentStatus() {
    return deploymentStatus;
  }

  public void setDeploymentStatus(InfrastructureResourceStatus deploymentStatus) {
    this.deploymentStatus = deploymentStatus;
  }
}
