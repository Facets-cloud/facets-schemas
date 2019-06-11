package com.capillary.ops.deployer.bo;

public class HPADetails {

    public HPADetails() {}

    public HPADetails(Integer minReplicas, Integer maxReplicas, Integer currentReplicas, Integer desiredReplicas, Integer targetCPUAvg, Integer currentCPUAvg) {
        this.minReplicas = minReplicas;
        this.maxReplicas = maxReplicas;
        this.currentReplicas = currentReplicas;
        this.desiredReplicas = desiredReplicas;
        this.targetCPUAvg = targetCPUAvg;
        this.currentCPUAvg = currentCPUAvg;
    }

    private Integer minReplicas;

    private Integer maxReplicas;

    private Integer currentReplicas;

    private Integer desiredReplicas;

    private Integer targetCPUAvg;

    private Integer currentCPUAvg;

    public Integer getMinReplicas() {
        return minReplicas;
    }

    public void setMinReplicas(Integer minReplicas) {
        this.minReplicas = minReplicas;
    }

    public Integer getMaxReplicas() {
        return maxReplicas;
    }

    public void setMaxReplicas(Integer maxReplicas) {
        this.maxReplicas = maxReplicas;
    }

    public Integer getCurrentReplicas() {
        return currentReplicas;
    }

    public void setCurrentReplicas(Integer currentReplicas) {
        this.currentReplicas = currentReplicas;
    }

    public Integer getDesiredReplicas() {
        return desiredReplicas;
    }

    public void setDesiredReplicas(Integer desiredReplicas) {
        this.desiredReplicas = desiredReplicas;
    }

    public Integer getTargetCPUAvg() {
        return targetCPUAvg;
    }

    public void setTargetCPUAvg(Integer targetCPUAvg) {
        this.targetCPUAvg = targetCPUAvg;
    }

    public Integer getCurrentCPUAvg() {
        return currentCPUAvg;
    }

    public void setCurrentCPUAvg(Integer currentCPUAvg) {
        this.currentCPUAvg = currentCPUAvg;
    }
}
