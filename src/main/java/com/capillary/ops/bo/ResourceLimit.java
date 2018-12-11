package com.capillary.ops.bo;

public class ResourceLimit {

    private int minMemory = 256;

    private int maxMemory = 0;

    private int minCpu = 0;

    private int maxCpu = 0;

    public ResourceLimit() {
    }

    public ResourceLimit(int minMemory, int maxMemory, int minCpu, int maxCpu) {
        this.minMemory = minMemory;
        this.maxMemory = maxMemory;
        this.minCpu = minCpu;
        this.maxCpu = maxCpu;
    }

    public int getMinMemory() {
        return minMemory;
    }

    public void setMinMemory(int minMemory) {
        this.minMemory = minMemory;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(int maxMemory) {
        this.maxMemory = maxMemory;
    }

    public int getMinCpu() {
        return minCpu;
    }

    public void setMinCpu(int minCpu) {
        this.minCpu = minCpu;
    }

    public int getMaxCpu() {
        return maxCpu;
    }

    public void setMaxCpu(int maxCpu) {
        this.maxCpu = maxCpu;
    }
}
