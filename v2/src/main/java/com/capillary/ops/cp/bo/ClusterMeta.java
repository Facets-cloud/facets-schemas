package com.capillary.ops.cp.bo;

public enum ClusterMeta {
     TOOLS_PASS("prom_pass"), TOOLS_URL("am_url");

    String name;

    ClusterMeta(String name) {
        this.name = name;
    }
}
