package com.capillary.ops.cp.bo;

import java.util.Objects;

public class PodSize {

    public PodSize(){}

    public PodSize(CpuSize cpuSize, MemorySize memorySize){
        this.cpuSize = cpuSize;
        this.memorySize = memorySize;
    }

    CpuSize cpuSize;

    MemorySize memorySize;

    public CpuSize getCpuSize() {
        return cpuSize;
    }

    public void setCpuSize(CpuSize cpuSize) {
        this.cpuSize = cpuSize;
    }

    public MemorySize getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(MemorySize memorySize) {
        this.memorySize = memorySize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PodSize podSize = (PodSize) o;
        return cpuSize.equals(podSize.cpuSize) && memorySize.equals(podSize.memorySize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpuSize, memorySize);
    }

    @Override
    public String toString() {
        return "PodSize{" +
                "cpuSize=" + cpuSize +
                ", memorySize=" + memorySize +
                '}';
    }
}
