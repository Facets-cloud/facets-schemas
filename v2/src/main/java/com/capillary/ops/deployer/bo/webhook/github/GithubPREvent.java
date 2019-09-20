package com.capillary.ops.deployer.bo.webhook.github;

import com.capillary.ops.deployer.bo.PullRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GithubPREvent {
    private String action;

    private Integer number;

    @JsonProperty(value = "pull_request")
    private GithubPRField githubPRField;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public GithubPRField getGithubPRField() {
        return githubPRField;
    }

    public void setGithubPRField(GithubPRField githubPRField) {
        this.githubPRField = githubPRField;
    }

    public PullRequest toPullRequest() throws ParseException {
        GithubPRField prField = this.getGithubPRField();
        PullRequest pullRequest = new PullRequest();
        pullRequest.setNumber(this.getNumber());
        pullRequest.setAction(this.getAction());
        pullRequest.setCommentsUrl(prField.getCommentsUrl());
        pullRequest.setSha(prField.getHead().getSha());
        pullRequest.setUrl(prField.getUrl());
        pullRequest.setState(prField.getState());
        pullRequest.setSourceBranch(prField.getHead().getRef());
        pullRequest.setDestinationBranch(prField.getBase().getRef());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        pullRequest.setUpdatedAt(simpleDateFormat.parse(prField.getUpdatedAt()));

        return pullRequest;
    }
}
