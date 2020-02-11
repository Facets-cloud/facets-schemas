package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.service.TFBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeploymentFacade {

    @Autowired
    ClusterFacade clusterFacade;

    @Autowired
    TFBuildService tfBuildService;

    /**
     * Create a new Deployment
     *
     * @param clusterId
     * @param deploymentRequest
     * @return
     */
    public DeploymentLog createDeployment(String clusterId, DeploymentRequest deploymentRequest) {
        AbstractCluster cluster = clusterFacade.getCluster(clusterId);
        String buildId = tfBuildService.deployLatest(cluster);
        DeploymentLog log = new DeploymentLog(buildId);
        return log;
    }
}
