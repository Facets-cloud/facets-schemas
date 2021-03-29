package com.capillary.ops.cp.bo;

public class PodSize {

    public PodSize(){}

    public PodSize(Double cpu, Double memory){
        this.cpu = cpu;
        this.memory = memory;
    }

    Double cpu;

    Double memory;

    public Double getCpu() {
        return cpu;
    }

    public void setCpu(Double cpu) {
        this.cpu = cpu;
    }

    public Double getMemory() {
        return memory;
    }

    public void setMemory(Double memory) {
        this.memory = memory;
    }
}
