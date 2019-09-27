package com.capillary.ops.deployer.bo;

public class UserStats {
    private long totalBuilds;
    private long totalDeployments;

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
