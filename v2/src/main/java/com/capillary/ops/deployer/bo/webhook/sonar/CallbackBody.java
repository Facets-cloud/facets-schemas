package com.capillary.ops.deployer.bo.webhook.sonar;


import java.util.Map;

public class CallbackBody {

    public static final String PR_ID_KEY = "sonar.analysis.pullRequestId";

    public CallbackBody() {
    }

    private String status;
    private QualityGate qualityGate;
    private Map<String, String> properties;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public QualityGate getQualityGate() {
        return qualityGate;
    }

    public void setQualityGate(QualityGate qualityGate) {
        this.qualityGate = qualityGate;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getPullRequestId() {
        return properties.getOrDefault(PR_ID_KEY, null);
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}