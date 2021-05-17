package com.capillary.ops.cp.bo.requests;

import com.amazonaws.regions.Regions;

import java.util.List;

public class AwsClusterRequest extends ClusterRequest {

    private String externalId;

    private String roleARN;

    private String accessKeyId;

    private String secretAccessKey;

    private Regions region;

    private List<String> azs;

    private String vpcCIDR;

    private List<String> instanceTypes;

    private String providedVPCId = "";

    public AwsClusterRequest() {
        super(Cloud.AWS);
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getRoleARN() {
        return roleARN;
    }

    public void setRoleARN(String roleARN) {
        this.roleARN = roleARN;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    public Regions getRegion() {
        return region;
    }

    public void setRegion(Regions region) {
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

  public String getProvidedVPCId() {
    return providedVPCId;
  }

  public void setProvidedVPCId(String providedVPCId) {
    this.providedVPCId = providedVPCId;
  }
}
