package com.capillary.ops.deployer.bo.webhook.sonar;


import java.util.Map;

public class CallbackBody {

    public static final String APP_ID = "sonar.analysis.appId";
    public static final String PR_NUMBER = "sonar.analysis.pr.number";

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

    public String getPrNumber() {
        return properties.getOrDefault(PR_NUMBER, null);
    }

    public String getAppId() {
        return properties.getOrDefault(APP_ID, null);
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}