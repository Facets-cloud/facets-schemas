package com.capillary.ops.cp.bo;

public enum MemoryUnits {

    MegaBytes("Mi"),
    GigaBytes("Gi");

    public final String format;

    private MemoryUnits(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return this.format;
    }

}
