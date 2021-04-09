package com.capillary.ops.cp.bo;

public class Scaling {

    private Boolean hpaEnabled = false;

    private Integer hpaMinReplicas = 1;

    private Integer hpaMaxReplicas = 2;

    private Integer hpaMetricThreshold = 60;

    public Boolean getHpaEnabled() {
        return hpaEnabled;
    }

    public void setHpaEnabled(Boolean hpaEnabled) {
        this.hpaEnabled = hpaEnabled;
    }

    public Integer getHpaMinReplicas() {
        return hpaMinReplicas;
    }

    public void setHpaMinReplicas(Integer hpaMinReplicas) {
        this.hpaMinReplicas = hpaMinReplicas;
    }

    public Integer getHpaMaxReplicas() {
        return hpaMaxReplicas;
    }

    public void setHpaMaxReplicas(Integer hpaMaxReplicas) {
        this.hpaMaxReplicas = hpaMaxReplicas;
    }

    public Integer getHpaMetricThreshold() {
        return hpaMetricThreshold;
    }

    public void setHpaMetricThreshold(Integer hpaMetricThreshold) {
        this.hpaMetricThreshold = hpaMetricThreshold;
    }
}
