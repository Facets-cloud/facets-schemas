package com.capillary.ops.deployer.bo;

public class ExternalDnsConfiguration {

    public ExternalDnsConfiguration() {
    }

    public ExternalDnsConfiguration(String awsAccessKeyId, String awsSecretAccessKey, String zoneId, String zoneDns) {
        this.awsAccessKeyId = awsAccessKeyId;
        this.awsSecretAccessKey = awsSecretAccessKey;
        this.zoneId = zoneId;
        this.zoneDns = zoneDns;
    }

    private String awsAccessKeyId;
    private String awsSecretAccessKey;
    private String zoneId;
    private String zoneDns;

    public String getAwsAccessKeyId() {
        return awsAccessKeyId;
    }

    public void setAwsAccessKeyId(String awsAccessKeyId) {
        this.awsAccessKeyId = awsAccessKeyId;
    }

    public String getAwsSecretAccessKey() {
        return awsSecretAccessKey;
    }

    public void setAwsSecretAccessKey(String awsSecretAccessKey) {
        this.awsSecretAccessKey = awsSecretAccessKey;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneDns() {
        return zoneDns;
    }

    public void setZoneDns(String zoneDns) {
        this.zoneDns = zoneDns;
    }
}
