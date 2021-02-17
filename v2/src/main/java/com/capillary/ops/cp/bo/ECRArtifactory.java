package com.capillary.ops.cp.bo;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ECRArtifactory extends Artifactory {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String awsKey;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String awsSecret;

    @NotNull
    private String awsRegion;

    @NotNull
    private String awsAccountId;

    public String getAwsKey() {
        return awsKey;
    }

    public void setAwsKey(String awsKey) {
        this.awsKey = awsKey;
    }

    public String getAwsSecret() {
        return awsSecret;
    }

    public void setAwsSecret(String awsSecret) {
        this.awsSecret = awsSecret;
    }

    public String getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
    }

    public String getAwsAccountId() {
        return awsAccountId;
    }

    public void setAwsAccountId(String awsAccountId) {
        this.awsAccountId = awsAccountId;
    }
}
