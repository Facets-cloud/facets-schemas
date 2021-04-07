package com.capillary.ops.deployer.bo;

import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

public class TokenPaginatedResponse<T> {

    public TokenPaginatedResponse() {
    }

    public TokenPaginatedResponse(List<T> logEventList, String nextToken) {
        this.data = logEventList;
        this.nextToken = nextToken;
    }

    public TokenPaginatedResponse(List<T> logEventList, String nextToken, software.amazon.awssdk.services.codebuild.model.Build build) {
        this.data = logEventList;
        this.nextToken = nextToken;
        this.build = build;
    }

    List<T> data;
    String nextToken;
    @JsonIgnore
    software.amazon.awssdk.services.codebuild.model.Build build;
    public software.amazon.awssdk.services.codebuild.model.Build getBuild() {
        return build;
    }

    public void setBuild(software.amazon.awssdk.services.codebuild.model.Build build) {
        this.build = build;
    }

    public List<T> getLogEventList() {
        return data;
    }

    public void setLogEventList(List<T> logEventList) {
        this.data = logEventList;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }
}
