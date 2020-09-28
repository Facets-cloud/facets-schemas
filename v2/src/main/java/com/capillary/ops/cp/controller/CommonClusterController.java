package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.K8sCredentials;
import com.capillary.ops.cp.bo.OverrideObject;
import com.capillary.ops.cp.bo.SnapshotInfo;
import com.capillary.ops.cp.bo.requests.OverrideRequest;
import com.capillary.ops.cp.facade.ClusterFacade;
import com.jcabi.aspects.Loggable;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cc/v1/clusters")
@Loggable
public class CommonClusterController {

    @Autowired
    ClusterFacade clusterFacade;

    @PostMapping("{clusterId}/credentials")
    public Boolean addClusterK8sCredentials(@RequestBody K8sCredentials request, @PathVariable String clusterId) {
        request.setClusterId(clusterId);
        return clusterFacade.addClusterK8sCredentials(request);
    }

    //@GetMapping("{clusterId}/deployments/{value}")
    public Deployment getDeploymentInCluster(@PathVariable String clusterId, @PathVariable String value,
        @RequestParam(value = "lookup", defaultValue = "deployerid") String lookupKey) {
        return clusterFacade.getApplicationData(clusterId, lookupKey, value);
    }

    @PostMapping("{clusterId}/overrides")
    public List<OverrideObject> overrideSizing(@PathVariable String clusterId,
        @RequestBody List<OverrideRequest> request) {

        return clusterFacade.override(clusterId, request);
    }

    @GetMapping("{clusterId}/overrides")
    public List<OverrideObject> getOverrides(@PathVariable String clusterId) {
        List<OverrideObject> overrides = clusterFacade.getOverrides(clusterId);
        return overrides;
    }

    /**
     * List snapshots for a particular resource inside a given cluster
     *
     * @param clusterId Cluster Id -> cluster id of nightly cluster
     * @param resourceType  Resource type -> e.g. aurora
     * @param instanceName  Instance name -> e.g. billdump
     * @return List of SnapshotInfo
     */
    @GetMapping("{clusterId}/dr/{resourceType}/snapshots/{instanceName}")
    public List<SnapshotInfo> listSnapshots(@PathVariable String clusterId, @PathVariable String resourceType,
                                            @PathVariable String instanceName) {
        return clusterFacade.listSnapshots(clusterId, resourceType, instanceName);
    }

    /**
     *
     * @param clusterId Cluster Id -> cluster id of nightly cluster
     * @param resourceType Resource type -> e.g. aurora
     * @param instanceName Instance name -> e.g. billdump
     * @param snapshotInfo Information about snapshot to be pinned
     * @return SnapshotInfo
     */
    @PreAuthorize("hasAnyRole('ADMIN') or @aclService.hasClusterWriteAccess(authentication, #clusterId)")
    @PostMapping("{clusterId}/dr/{resourceType}/snapshots/{instanceName}/pinnedSnapshot")
    public SnapshotInfo pinSnapshot(@PathVariable String clusterId, @PathVariable String resourceType,
                                    @PathVariable String instanceName, @RequestBody SnapshotInfo snapshotInfo) {
        return clusterFacade.pinSnapshot(clusterId, resourceType, instanceName, snapshotInfo);
    }

    /**
     *
     * @param clusterId Cluster Id -> cluster id of nightly cluster
     * @param resourceType Resource type -> e.g. aurora
     * @param instanceName Instance name -> e.g. billdump
     * @return SnapshotInfo
     */
    @GetMapping("{clusterId}/dr/{resourceType}/snapshots/{instanceName}/pinnedSnapshot")
    public SnapshotInfo getPinnedSnapshot(@PathVariable String clusterId, @PathVariable String resourceType,
                                          @PathVariable String instanceName) {
        return clusterFacade.getPinnedSnapshot(clusterId, resourceType, instanceName);
    }
}
