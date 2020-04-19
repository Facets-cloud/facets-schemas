package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.K8sCredentials;
import com.capillary.ops.cp.bo.requests.ClusterRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Cloud agnostic Interface definition for the controllers
 *
 * @param <T>
 */
public interface ClusterController<T extends AbstractCluster, CR extends ClusterRequest> {

    T createCluster(@RequestBody CR request);

    Boolean addClusterK8sCredentials(@RequestBody K8sCredentials request, @PathVariable String clusterId);

    T getCluster(@PathVariable String clusterId);

}
