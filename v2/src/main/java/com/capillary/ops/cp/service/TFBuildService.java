package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;

public interface TFBuildService {

    String deployLatest(AbstractCluster cluster);
}
