package com.capillary.ops.deployer.bo;

public class HPA {

    public HPA() {
    }

    public HPA(HPADetails hpaDetails) {
        this.threshold = hpaDetails.getTargetCPUAvg();
        this.minReplicas = hpaDetails.getMinReplicas();
        this.maxReplicas = hpaDetails.getMaxReplicas();
    }

    public HPA(int threshold, int minReplicas, int maxReplicas) {
        this.threshold = threshold;
        this.minReplicas = minReplicas;
        this.maxReplicas = maxReplicas;
    }

    private String metricName;

    private int threshold;

    private int minReplicas;

    private int maxReplicas;

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getMinReplicas() {
        return minReplicas;
    }

    public void setMinReplicas(int minReplicas) {
        this.minReplicas = minReplicas;
    }

    public int getMaxReplicas() {
        return maxReplicas;
    }

    public void setMaxReplicas(int maxReplicas) {
        this.maxReplicas = maxReplicas;
    }
}
