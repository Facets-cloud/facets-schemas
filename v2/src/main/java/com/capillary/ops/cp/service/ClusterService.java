package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.requests.ClusterRequest;

/**
 * Service to handle all cluster related operations
 */
public interface ClusterService<C extends AbstractCluster, CR extends ClusterRequest> extends Service {

    /**
     * Create a cluster as per the cloud specific implementation
     *
     * @param request Cluster creation request
     * @param stackName Name of the Tag
     * @return Cluster Object
     */
    C createCluster(CR request, String stackName);
}
