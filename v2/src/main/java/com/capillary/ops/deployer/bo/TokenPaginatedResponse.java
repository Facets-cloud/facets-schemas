package com.capillary.ops.deployer.bo;

import java.util.List;

public class TokenPaginatedResponse<T> {

    public TokenPaginatedResponse() {
    }

    public TokenPaginatedResponse(List<T> logEventList, String nextToken) {
        this.data = logEventList;
        this.nextToken = nextToken;
    }

    List<T> data;
    String nextToken;

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
