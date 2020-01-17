package com.capillary.ops.deployer.bo.capillaryCloud;

import java.util.List;

public class VPC extends InfrastructureResourceInstance {
    private String vpcId;
    private String vpcCIDR;
    private List<String> privateSubnets;
    private List<String> publicSubnets;

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getVpcCIDR() {
        return vpcCIDR;
    }

    public void setVpcCIDR(String vpcCIDR) {
        this.vpcCIDR = vpcCIDR;
    }

    public List<String> getPrivateSubnets() {
        return privateSubnets;
    }

    public void setPrivateSubnets(List<String> privateSubnets) {
        this.privateSubnets = privateSubnets;
    }

    public List<String> getPublicSubnets() {
        return publicSubnets;
    }

    public void setPublicSubnets(List<String> publicSubnets) {
        this.publicSubnets = publicSubnets;
    }
}
