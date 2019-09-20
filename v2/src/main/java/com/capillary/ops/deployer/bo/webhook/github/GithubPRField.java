package com.capillary.ops.deployer.bo.webhook.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubPRField {

    @JsonProperty(value = "comments_url")
    private String commentsUrl;

    private String url;

    private String state;

    @JsonProperty(value = "updated_at")
    private String updatedAt;

    @JsonProperty(value = "head")
    private GithubPRBranchRef head;

    @JsonProperty(value = "base")
    private GithubPRBranchRef base;

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public GithubPRBranchRef getHead() {
        return head;
    }

    public void setHead(GithubPRBranchRef head) {
        this.head = head;
    }

    public GithubPRBranchRef getBase() {
        return base;
    }

    public void setBase(GithubPRBranchRef base) {
        this.base = base;
    }
}
