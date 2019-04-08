package com.capillary.ops.deployer.bo;

public class LogEvent {

    public LogEvent() {
    }

    public LogEvent(Long timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    private Long timestamp;
    private String message;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
