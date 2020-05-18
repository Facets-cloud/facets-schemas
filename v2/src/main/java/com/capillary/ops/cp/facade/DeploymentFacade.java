package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.service.TFBuildService;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Loggable
public class DeploymentFacade {

    @Autowired
    private ClusterFacade clusterFacade;

    @Autowired
    private TFBuildService tfBuildService;

    /**
     * Create a new Deployment
     *
     * @param clusterId         Id of the Cluster
     * @param deploymentRequest Any Additional Deployment Params
     * @return The Deployment Log Object
     */
    public DeploymentLog createDeployment(String clusterId, DeploymentRequest deploymentRequest) {
        AbstractCluster cluster = clusterFacade.getCluster(clusterId);
        //TODO: Save Deployment requests for audit purpose
        String buildId = tfBuildService.deployLatest(cluster, deploymentRequest.getReleaseType());
        DeploymentLog log = new DeploymentLog(buildId);
        return log;
    }
}
