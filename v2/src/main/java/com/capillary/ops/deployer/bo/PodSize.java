package com.capillary.ops.deployer.bo;

public enum PodSize {
    SMALL(1,2),
    LARGE(2,4),
    XLARGE(3,6),
    XXLARGE(7.0,14),
    TINY(0.5, 1),
    MICRO(0.25, 0.5);
    private double cpu;
    private double memory;

    PodSize(double cpu, double memory) {
        this.cpu = cpu;
        this.memory = memory;
    }

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public double getMemory() {
        return memory;
    }

    public void setMemory(double memory) {
        this.memory = memory;
    }
}
