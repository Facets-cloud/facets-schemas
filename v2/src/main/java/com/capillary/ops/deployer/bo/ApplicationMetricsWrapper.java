package com.capillary.ops.deployer.bo;

public class ApplicationMetricsWrapper {

    private Application application;
    private ApplicationMetrics recentMetrics;
    private ApplicationMetrics lastWeekMetrics;

    public ApplicationMetricsWrapper(Application application) {
        this.application = application;
    }

    public ApplicationMetricsWrapper(Application application, ApplicationMetrics recentMetrics, ApplicationMetrics lastWeekMetrics) {
        this.application = application;
        this.recentMetrics = recentMetrics;
        this.lastWeekMetrics = lastWeekMetrics;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public ApplicationMetrics getRecentMetrics() {
        return recentMetrics;
    }

    public void setRecentMetrics(ApplicationMetrics recentMetrics) {
        this.recentMetrics = recentMetrics;
    }

    public ApplicationMetrics getLastWeekMetrics() {
        return lastWeekMetrics;
    }

    public void setLastWeekMetrics(ApplicationMetrics lastWeekMetrics) {
        this.lastWeekMetrics = lastWeekMetrics;
    }
}
