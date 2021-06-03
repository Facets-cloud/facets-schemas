package com.capillary.ops.cp.bo.requests;

import java.util.TimeZone;

public class LocalClusterRequest extends ClusterRequest {
    public LocalClusterRequest() {
        this.setTz(TimeZone.getDefault());
    }
}
