package com.capillary.ops.cp.bo;

public enum ClusterMeta {
    PROM_URL("prom_url"), PROM_PASS("prom_pass"), AM_URL("am_url");

    String name;

    ClusterMeta(String name) {
        this.name = name;
    }
}
