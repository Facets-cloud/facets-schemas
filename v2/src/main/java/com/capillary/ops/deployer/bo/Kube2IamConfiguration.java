package com.capillary.ops.deployer.bo;

public class Kube2IamConfiguration {
    boolean enabled;
    String awsAccessKeyID;
    String awsSecretAccessKey;
    String hostNetworkInterface;

    public Kube2IamConfiguration(){
    };

    public Kube2IamConfiguration(boolean enabled, String awsAccessKeyID, String awsSecretAccessKey){
        this.enabled = enabled;
        this.awsAccessKeyID = awsAccessKeyID;
        this.awsSecretAccessKey = awsSecretAccessKey;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getHostNetworkInterface() {
        return hostNetworkInterface;
    }

    public void setHostNetworkInterface(String hostNetworkInterface) {
        this.hostNetworkInterface = hostNetworkInterface;
    }
}


