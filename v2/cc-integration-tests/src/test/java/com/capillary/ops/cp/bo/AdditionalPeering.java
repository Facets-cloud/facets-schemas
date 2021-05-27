package com.capillary.ops.cp.bo;

public class AdditionalPeering {
    private String name;

    private String accountId;

    private String vcpId;

    private String cidr;

    private String region;

    private Boolean disabled = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getVcpId() {
        return vcpId;
    }

    public void setVcpId(String vcpId) {
        this.vcpId = vcpId;
    }

    public String getCidr() {
        return cidr;
    }

    public void setCidr(String cidr) {
        this.cidr = cidr;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        return "AdditionalPeerings{" +
                "name='" + name + '\'' +
                ", accountId='" + accountId + '\'' +
                ", vcpId='" + vcpId + '\'' +
                ", cidr='" + cidr + '\'' +
                ", region='" + region + '\'' +
                ", disabled=" + disabled +
                '}';
    }
}
