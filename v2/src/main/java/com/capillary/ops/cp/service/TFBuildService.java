package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.*;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import software.amazon.awssdk.services.codebuild.model.Build;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.List;
import java.util.Map;

public interface TFBuildService {

    DeploymentLog deployLatest(AbstractCluster cluster, DeploymentRequest deploymentRequest, DeploymentContext deploymentContext);

    DeploymentLog loadDeploymentStatus(DeploymentLog deploymentLog, boolean loadBuildDetails);

    List<TerraformChange> getTerraformChanges(String codeBuildId);
}
