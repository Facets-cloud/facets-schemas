package com.capillary.ops.deployer.bo;

public class Kube2IamConfiguration {
    String awsAccessKeyID;
    String awsSecretAccessKey;

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
}


