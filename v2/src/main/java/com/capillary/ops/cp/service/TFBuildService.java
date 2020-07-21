package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.List;
import java.util.Map;

public interface TFBuildService {

    String deployLatest(AbstractCluster cluster, DeploymentRequest deploymentRequest);

    StatusType getDeploymentStatus(String runId);

    Map<String, StatusType> getDeploymentStatuses(List<String> runIds);

    Map<String, Object> getDeploymentReport(String runId);
}
