package com.capillary.ops.cp.bo;

import com.amazonaws.regions.Regions;
import com.capillary.ops.cp.bo.requests.Cloud;

import java.util.List;

/**
 * <p>
 * SAMPLE OBJECT
 * "{\n" +
 * "\"name\": \"anshul\",\n" +
 * "\"awsRegion\": \"us-west-2\",\n" +
 * "\"azs\": [\"us-west-2a\", \"us-west-2b\"],\n" +
 * "\"privateSubnetCIDR\": [\"10.250.100.0/24\", \"10.250.101.0/24\"],\n" +
 * "\"publicSubnetCIDR\": [\"10.250.110.0/24\", \"10.250.111.0/24\"],\n" +
 * "\"vpcCIDR\": \"10.250.0.0/16\"\n" +
 * "}"
 */
public class AwsCluster extends AbstractCluster {

    private Regions awsRegion;

    private List<String> azs;

    private List<String> privateSubnetCIDR;

    private List<String> publicSubnetCIDR;

    private String vpcCIDR;

    private String externalId;

    private String roleARN;

    public AwsCluster(String name) {
        super(name, Cloud.AWS);
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

    public Regions getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(Regions awsRegion) {
        this.awsRegion = awsRegion;
    }

    public List<String> getAzs() {
        return azs;
    }

    public void setAzs(List<String> azs) {
        this.azs = azs;
    }

    public List<String> getPrivateSubnetCIDR() {
        return privateSubnetCIDR;
    }

    public void setPrivateSubnetCIDR(List<String> privateSubnetCIDR) {
        this.privateSubnetCIDR = privateSubnetCIDR;
    }

    public List<String> getPublicSubnetCIDR() {
        return publicSubnetCIDR;
    }

    public void setPublicSubnetCIDR(List<String> publicSubnetCIDR) {
        this.publicSubnetCIDR = publicSubnetCIDR;
    }

    public String getVpcCIDR() {
        return vpcCIDR;
    }

    public void setVpcCIDR(String vpcCIDR) {
        this.vpcCIDR = vpcCIDR;
    }
}
