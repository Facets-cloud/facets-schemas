package com.capillary.ops.deployer.bo;


import com.capillary.ops.deployer.bo.Registry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;

public final class ECRRegistry extends Registry {

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

    public ECRRegistry(){

    }

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
