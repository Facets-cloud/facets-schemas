package com.capillary.ops.deployer.bo;

import java.util.List;

public class LogsResponse {

    public LogsResponse() {
    }

    public LogsResponse(List<LogEvent> logEventList, String nextToken) {
        this.logEventList = logEventList;
        this.nextToken = nextToken;
    }

    List<LogEvent> logEventList;
    String nextToken;

    public List<LogEvent> getLogEventList() {
        return logEventList;
    }

    public void setLogEventList(List<LogEvent> logEventList) {
        this.logEventList = logEventList;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }
}
