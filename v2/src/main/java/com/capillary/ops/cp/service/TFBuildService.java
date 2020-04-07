package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.requests.ReleaseType;

public interface TFBuildService {

    String deployLatest(AbstractCluster cluster, ReleaseType releaseType);
}
