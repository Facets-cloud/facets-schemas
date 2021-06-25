package com.capillary.ops.cp.bo.requests;

// import com.microsoft.azure.management.resources.fluentcore.arm.Region;

import com.amazonaws.regions.Regions;

import java.util.List;

public class AzureClusterRequest extends ClusterRequest {
    private String tenantId;
    private String subscriptionId;
    private String clientId;
    private String clientSecret;
    private String region;
    private List<String> azs;
    private String vpcCIDR;
    private List<String> instanceTypes;

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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<String> getAzs() {
        return azs;
    }

    public void setAzs(List<String> azs) {
        this.azs = azs;
    }

    public String getVpcCIDR() {
        return vpcCIDR;
    }

    public void setVpcCIDR(String vpcCIDR) {
        this.vpcCIDR = vpcCIDR;
    }

    public List<String> getInstanceTypes() {
        return instanceTypes;
    }

    public void setInstanceTypes(List<String> instanceTypes) {
        this.instanceTypes = instanceTypes;
    }
}
