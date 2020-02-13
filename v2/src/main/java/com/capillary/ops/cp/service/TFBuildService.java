package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.Stack;
import org.springframework.stereotype.Service;

public interface TFBuildService {

    String deployLatest(AbstractCluster cluster);
}
