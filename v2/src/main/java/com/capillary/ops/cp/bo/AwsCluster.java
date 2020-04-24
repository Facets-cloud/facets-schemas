package com.capillary.ops.cp.bo;

import com.amazonaws.regions.Regions;
import com.capillary.ops.cp.bo.requests.Cloud;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document
public class AwsCluster extends AbstractCluster {

    private String awsRegion;

    private List<String> azs;

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

    public String getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
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
}
