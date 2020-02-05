package com.capillary.ops.deployer.bo;

public class Alerting {

    public Alerting() {
    }

    public Alerting(ApplicationFamily applicationFamily, String applicationId,
                    String environmentName, String newRelicDashboardUrl) {
        this.applicationFamily = applicationFamily;
        this.applicationId = applicationId;
        this.newRelicAlertsUrl = newRelicDashboardUrl;
        this.environmentName = environmentName;
    }

    private ApplicationFamily applicationFamily;

    private String applicationId;

    private String newRelicAlertsUrl;

    private String environmentName;

    public String getNewRelicAlertsUrl() {
        return newRelicAlertsUrl;
    }

    public void setNewRelicAlertsUrl(String newRelicAlertsUrl) {
        this.newRelicAlertsUrl = newRelicAlertsUrl;
    }

    public ApplicationFamily getApplicationFamily() {
        return applicationFamily;
    }

    public void setApplicationFamily(ApplicationFamily applicationFamily) {
        this.applicationFamily = applicationFamily;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }
}
