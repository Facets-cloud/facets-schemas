package com.capillary.ops.cp.bo;

public enum CpuUnits {

    MilliCores("m"),
    Cores("c");

    public final String format;

    private CpuUnits(String format){
        this.format = format;
    }

    @Override
    public String toString() {
        return this.format;
    }

}
