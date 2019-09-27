package com.capillary.ops.deployer.bo;

public class GlobalStats {
    private long applicationCount;
    private long totalBuilds;
    private long totalDeployments;

    public GlobalStats() {
    }

    public GlobalStats(long applicationCount, long totalBuilds, long totalDeployments) {
        this.applicationCount = applicationCount;
        this.totalBuilds = totalBuilds;
        this.totalDeployments = totalDeployments;
    }

    public long getApplicationCount() {
        return applicationCount;
    }

    public void setApplicationCount(long applicationCount) {
        this.applicationCount = applicationCount;
    }

    public long getTotalBuilds() {
        return totalBuilds;
    }

    public void setTotalBuilds(long totalBuilds) {
        this.totalBuilds = totalBuilds;
    }

    public long getTotalDeployments() {
        return totalDeployments;
    }

    public void setTotalDeployments(long totalDeployments) {
        this.totalDeployments = totalDeployments;
    }
}
