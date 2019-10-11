package com.capillary.ops.deployer.bo.webhook.bitbucket;

import com.capillary.ops.deployer.bo.PullRequest;
import com.capillary.ops.deployer.bo.webhook.github.GithubPRField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class BitbucketPREvent {

    @JsonProperty(value = "pullrequest")
    private BitbucketPRField bitbucketPRField;

    public BitbucketPRField getBitbucketPRField() {
        return bitbucketPRField;
    }

    public void setBitbucketPRField(BitbucketPRField bitbucketPRField) {
        this.bitbucketPRField = bitbucketPRField;
    }

    public PullRequest toPullRequest() throws ParseException {
        BitbucketPRField prField = this.getBitbucketPRField();
        PullRequest pullRequest = new PullRequest();
        pullRequest.setNumber(prField.getId());
        pullRequest.setCommentsUrl(prField.getBitbucketPRLinks().getCommentsUrl());
        pullRequest.setSha(prField.getBitbucketPRBranchSource().getSha());
        pullRequest.setSourceBranch(prField.getBitbucketPRBranchSource().getBranchName());
        pullRequest.setDestinationBranch(prField.getBitbucketPRBranchDestination().getBranchName());
        pullRequest.setState(prField.getState());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        pullRequest.setUpdatedAt(simpleDateFormat.parse(prField.getUpdatedOn()));
        return pullRequest;
    }
}
