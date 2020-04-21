package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.requests.ClusterRequest;

import java.util.Optional;

/**
 * Service to handle all cluster related operations
 */
public interface ClusterService<C extends AbstractCluster, CR extends ClusterRequest> extends Service {

    /**
     * Create a cluster as per the cloud specific implementation
     *
     * @param request Cluster creation request
     * @return Cluster Object
     */
    C createCluster(CR request);

    C updateCluster(CR request, C existing);
}
