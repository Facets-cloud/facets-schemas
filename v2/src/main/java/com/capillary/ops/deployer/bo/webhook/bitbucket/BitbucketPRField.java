package com.capillary.ops.deployer.bo.webhook.bitbucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BitbucketPRField {
    private String type;

    private Integer id;

    private String state;

    @JsonProperty(value = "updated_on")
    private String updatedOn;

    @JsonProperty(value = "links")
    private BitbucketPRLinks bitbucketPRLinks;

    private String description;

    @JsonProperty(value = "source")
    private BitbucketPRBranchRef bitbucketPRBranchSource;

    @JsonProperty(value = "destination")
    private BitbucketPRBranchRef bitbucketPRBranchDestination;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public BitbucketPRLinks getBitbucketPRLinks() {
        return bitbucketPRLinks;
    }

    public void setBitbucketPRLinks(BitbucketPRLinks bitbucketPRLinks) {
        this.bitbucketPRLinks = bitbucketPRLinks;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BitbucketPRBranchRef getBitbucketPRBranchSource() {
        return bitbucketPRBranchSource;
    }

    public void setBitbucketPRBranchSource(BitbucketPRBranchRef bitbucketPRBranchSource) {
        this.bitbucketPRBranchSource = bitbucketPRBranchSource;
    }

    public BitbucketPRBranchRef getBitbucketPRBranchDestination() {
        return bitbucketPRBranchDestination;
    }

    public void setBitbucketPRBranchDestination(BitbucketPRBranchRef bitbucketPRBranchDestination) {
        this.bitbucketPRBranchDestination = bitbucketPRBranchDestination;
    }
}
