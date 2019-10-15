package com.capillary.ops.deployer.bo;

public class K8sLoggingConfiguration {
    String efsHost;
    String awsAccessKeyID;
    String awsSecretAccessKey;
    String awsRegion;
    String logsS3Bucket;

    public String getEfsHost() {
        return efsHost;
    }

    public void setEfsHost(String efsHost) {
        this.efsHost = efsHost;
    }

    public String getAwsAccessKeyID() {
        return awsAccessKeyID;
    }

    public void setAwsAccessKeyID(String awsAccessKeyID) {
        this.awsAccessKeyID = awsAccessKeyID;
    }

    public String getAwsSecretAccessKey() {
        return awsSecretAccessKey;
    }

    public void setAwsSecretAccessKey(String awsSecretAccessKey) {
        this.awsSecretAccessKey = awsSecretAccessKey;
    }

    public String getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
    }

    public String getLogsS3Bucket() {
        return logsS3Bucket;
    }

    public void setLogsS3Bucket(String logsS3Bucket) {
        this.logsS3Bucket = logsS3Bucket;
    }
}
