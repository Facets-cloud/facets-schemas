package com.capillary.ops.deployer.bo;

public class Monitoring {

    public Monitoring() {
    }

    public Monitoring(ApplicationFamily applicationFamily, String applicationId,
                      String environmentName, String newRelicDashboardUrl) {
        this.applicationFamily = applicationFamily;
        this.applicationId = applicationId;
        this.newRelicDashboardUrl = newRelicDashboardUrl;
        this.environmentName = environmentName;
    }

    private ApplicationFamily applicationFamily;

    private String applicationId;

    private String newRelicDashboardUrl;

    private String environmentName;

    public String getNewRelicDashboardUrl() {
        return newRelicDashboardUrl;
    }

    public void setNewRelicDashboardUrl(String newRelicDashboardUrl) {
        this.newRelicDashboardUrl = newRelicDashboardUrl;
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
