package com.capillary.ops.cp.bo;

import java.time.Instant;
import java.util.Map;

public class AuditLog {
    public AuditLog() {}

    public AuditLog(String action, Instant timestamp, String triggeredBy, String reason, Map<String, Object> parameters) {
        this.action = action;
        this.timestamp = timestamp;
        this.triggeredBy = triggeredBy;
        this.reason = reason;
        this.parameters = parameters;
    }

    private String action;
    private Instant timestamp;
    private String triggeredBy;
    private String reason;
    private Map<String, Object> parameters;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
