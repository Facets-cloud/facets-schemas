package com.capillary.ops.deployer.bo;

public class ClusterAutoscalerConfiguration {

    String autoscalingGroup;
    int minSize = 0;
    int maxSize = 0;

    public String getAutoscalingGroup() {
        return autoscalingGroup;
    }

    public void setAutoscalingGroup(String autoscalingGroup) {
        this.autoscalingGroup = autoscalingGroup;
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
