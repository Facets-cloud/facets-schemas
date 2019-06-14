package com.capillary.ops.deployer.bo;

public class PodResource {

    public PodResource(){}

    public PodResource(String cpu, String memory) {
        this.cpu = cpu;
        this.memory = memory;
    }

    private String cpu;

    private String memory;

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }
}

