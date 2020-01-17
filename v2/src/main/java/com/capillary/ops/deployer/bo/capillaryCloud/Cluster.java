package com.capillary.ops.deployer.bo.capillaryCloud;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Cluster {
    @Id
    private String id;
    private String name;
    private String awsRegion;
    private List<String> azs;
    private List<String> privateSubnetCIDR;
    private List<String> publicSubnetCIDR;
    private String vpcCIDR;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
    }

    public Cluster() {
    }

    public Cluster(String name, String awsRegion, List<String> azs, List<String> privateSubnetCIDR, List<String> publicSubnetCIDR, String vpcCIDR) {
        this.name = name;
        this.awsRegion = awsRegion;
        this.azs = azs;
        this.privateSubnetCIDR = privateSubnetCIDR;
        this.publicSubnetCIDR = publicSubnetCIDR;
        this.vpcCIDR = vpcCIDR;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
