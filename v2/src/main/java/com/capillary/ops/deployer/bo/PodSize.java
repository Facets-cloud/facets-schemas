package com.capillary.ops.deployer.bo;

public enum PodSize {
    SMALL(1,2),
    LARGE(2,4),
    XLARGE(3,6);

    private int cpu;
    private int memory;

    PodSize(int cpu, int memory) {
        this.cpu = cpu;
        this.memory = memory;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }
}
