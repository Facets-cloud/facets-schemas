package com.capillary.ops.cp.bo;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PodSize podSize = (PodSize) o;
        return cpu.equals(podSize.cpu) && memory.equals(podSize.memory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpu, memory);
    }

    @Override
    public String toString() {
        return "PodSize{" +
                "cpu=" + cpu +
                ", memory=" + memory +
                '}';
    }
}
