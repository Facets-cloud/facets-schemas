package com.capillary.ops.cp.bo;

import com.capillary.ops.cp.bo.requests.Cloud;

import java.util.TimeZone;

public class LocalCluster extends AbstractCluster {

    public LocalCluster(String name) {
        super(name, Cloud.LOCAL);
        this.setReleaseStream(BuildStrategy.QA);
        this.setTz(TimeZone.getDefault());
        this.setRequireSignOff(false);
    }
}
