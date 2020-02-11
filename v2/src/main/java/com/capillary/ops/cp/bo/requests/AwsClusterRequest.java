package com.capillary.ops.cp.bo.requests;

import com.amazonaws.regions.Regions;

import java.util.List;

public class AwsClusterRequest extends ClusterRequest {

    private String externalId;

    private String roleARN;

    private Regions region;

    private List<String> azs;

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
}
