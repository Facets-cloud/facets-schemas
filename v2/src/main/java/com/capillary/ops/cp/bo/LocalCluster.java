package com.capillary.ops.cp.bo;

import com.capillary.ops.cp.bo.requests.Cloud;

public class LocalCluster extends AbstractCluster {

    public LocalCluster(String name) {
        super(name, Cloud.LOCAL);
    }
}
