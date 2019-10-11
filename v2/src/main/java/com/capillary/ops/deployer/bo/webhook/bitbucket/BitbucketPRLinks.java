package com.capillary.ops.deployer.bo.webhook.bitbucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class BitbucketPRLinks {

    private String commentsUrl;

    @JsonProperty(value = "comments")
    private void unpackCommentsUrl(Map<String, String> comments) {
        commentsUrl = comments.get("href");
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }
}
