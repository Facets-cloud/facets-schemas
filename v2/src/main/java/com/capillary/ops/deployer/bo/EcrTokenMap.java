package com.capillary.ops.deployer.bo;

import java.io.Serializable;

public class EcrTokenMap implements Serializable {
    private String ecrToken;
    private String awsAccountId;

    public EcrTokenMap(String ecrToken, String awsAccountId) {
        this.ecrToken = ecrToken;
        this.awsAccountId = awsAccountId;
    }

    public EcrTokenMap() {
    }

    public void setEcrToken(String ecrToken) {
        this.ecrToken = ecrToken;
    }

    public void setAwsAccountId(String awsAccountId) {
        this.awsAccountId = awsAccountId;
    }

    public String getEcrToken() {
        return ecrToken;
    }

    public String getAwsAccountId() {
        return awsAccountId;
    }
}
