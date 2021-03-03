package com.capillary.ops.cp.bo.requests;

// import com.microsoft.azure.management.resources.fluentcore.arm.Region;

public class AzureClusterRequest extends ClusterRequest {
    private String tenantId;
    private String subscriptionId;
    private String clientId;
    private String clientSecret;
    //private Region region;

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getSubscriptionId() {
    return subscriptionId;
  }

  public void setSubscriptionId(String subscriptionId) {
    this.subscriptionId = subscriptionId;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

//  public Region getRegion() {
//    return region;
//  }
//
//  public void setRegion(Region region) {
//    this.region = region;
//  }
}
