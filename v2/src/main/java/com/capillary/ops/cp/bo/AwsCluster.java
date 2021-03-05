package com.capillary.ops.cp.bo;

import com.amazonaws.regions.Regions;
import com.capillary.ops.cp.bo.requests.Cloud;
import com.capillary.ops.cp.exceptions.BadRequestException;
import org.springframework.data.mongodb.core.mapping.Document;
import software.amazon.awssdk.services.ec2.model.InstanceType;

import java.util.ArrayList;
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

    private String accessKeyId;

    private String secretAccessKey;

    private List<String> instanceTypes = new ArrayList<String>(){
        {add(InstanceType.M5_2_XLARGE.toString());}
        {add(InstanceType.M4_2_XLARGE.toString());}
        {add(InstanceType.R4_2_XLARGE.toString());}
    };

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

    public List<String> getInstanceTypes() {
        return instanceTypes;
    }

    public void setInstanceTypes(List<String> instanceTypes){
        for (String s : instanceTypes) {
            InstanceType instanceType = InstanceType.fromValue(s);
            if (instanceType.equals(InstanceType.UNKNOWN_TO_SDK_VERSION)){
                throw new BadRequestException("invalid instance type " + s);
            }
        }
        this.instanceTypes = instanceTypes;
    }
}
