package com.capillary.ops.deployer.bo.webhook.sonar;

public class Condition{
    public Condition() {
    }

    private String metric;
    private String operator;
    private String value;
    private String status;
    private String errorThreshold;

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorThreshold() {
        return errorThreshold;
    }

    public void setErrorThreshold(String errorThreshold) {
        this.errorThreshold = errorThreshold;
    }
}
