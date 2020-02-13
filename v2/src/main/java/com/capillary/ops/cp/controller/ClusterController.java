package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.requests.ClusterRequest;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.facade.DeploymentFacade;
import com.capillary.ops.deployer.bo.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Cloud agnostic Interface definition for the controllers
 *
 * @param <T>
 */
public interface ClusterController<T extends AbstractCluster, CR extends ClusterRequest> {

    @PostMapping()
    T createCluster(@RequestBody CR request);

    @GetMapping("{clusterId}")
    T getCluster(@PathVariable String clusterId);

}
