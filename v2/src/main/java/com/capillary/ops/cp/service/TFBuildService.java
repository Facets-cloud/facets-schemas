package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import software.amazon.awssdk.services.codebuild.model.EnvironmentVariable;

import java.util.List;

public interface TFBuildService {

    String deployLatest(AbstractCluster cluster, DeploymentRequest deploymentRequest);
}
