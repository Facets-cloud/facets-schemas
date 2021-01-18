package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.bo.ClusterTask;
import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.OverrideObject;
import com.capillary.ops.cp.bo.ResourceDetails;
import com.capillary.ops.cp.bo.SnapshotInfo;
import com.capillary.ops.cp.bo.requests.OverrideRequest;
import com.capillary.ops.cp.bo.requests.SilenceAlarmRequest;
import com.capillary.ops.cp.facade.ClusterFacade;
import com.capillary.ops.cp.facade.DeploymentFacade;
import com.capillary.ops.cp.service.AclService;
import com.jcabi.aspects.Loggable;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("cc-ui/v1/clusters")
@Loggable
public class UiCommonClusterController {

    @Autowired
    ClusterFacade clusterFacade;

    @Autowired
    DeploymentFacade deploymentFacade;

    @Autowired
    private AclService aclService;

    //@GetMapping("{clusterId}/deployments/{value}")
    public Deployment getDeploymentInCluster(@PathVariable String clusterId, @PathVariable String value,
        @RequestParam(value = "lookup", defaultValue = "deployerid") String lookupKey) {
        return clusterFacade.getApplicationData(clusterId, lookupKey, value);
    }

    @GetMapping("{clusterId}/alerts")
    public HashMap<String, Object> getAlerts(@PathVariable String clusterId){
        return clusterFacade.getAllClusterAlerts(clusterId);
    }

    @GetMapping("{clusterId}/open-alerts")
    public HashMap<String, Object> getOpenAlerts(@PathVariable String clusterId){
        return clusterFacade.getOpenClusterAlerts(clusterId);
    }

    @PostMapping("{clusterId}/silence-alerts")
    public HashMap<String, Object> silenceAlerts(@PathVariable String clusterId,
                                                 @RequestBody SilenceAlarmRequest request){
        return clusterFacade.silenceAlert(clusterId, request);
    }

    @PreAuthorize("hasRole('CC-ADMIN') or @aclService.hasClusterMaintainerAccess(authentication, #clusterId)")
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

    @PreAuthorize("hasRole('CC-ADMIN') or @aclService.hasClusterMaintainerAccess(authentication, #clusterId)")
    @DeleteMapping("{clusterId}/overrides/{resourceType}/{resourceName}")
    public List<OverrideObject> deleteOverrides(@PathVariable String clusterId,
                                                @PathVariable String resourceType,
                                                @PathVariable String resourceName) {
        return clusterFacade.deleteOverride(clusterId, resourceType, resourceName);
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
    @PreAuthorize("hasRole('CC-ADMIN')")
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

    /**
     *
     * @param clusterId Cluster Id -> cluster id of nightly cluster
     * @param resourceType Resource type -> e.g. aurora
     * @param instanceName Instance name -> e.g. billdump
     * @return true/false
     */
    @PreAuthorize("hasRole('CC-ADMIN') or @aclService.hasClusterWriteAccess(authentication, #clusterId)")
    @PostMapping("{clusterId}/dr/{resourceType}/snapshots/{instanceName}")
    public boolean createSnapshot(@PathVariable String clusterId, @PathVariable String resourceType,
                                          @PathVariable String instanceName) {
        return clusterFacade.createSnapshot(clusterId, resourceType, instanceName);
    }

    @PreAuthorize("hasRole('CC-ADMIN') or hasRole('K8S_READER') or hasRole('K8S_DEBUGGER')")
    @GetMapping(value = "{clusterId}/kubeconfig")
    public ResponseEntity<String> getKubeConfig(@PathVariable String clusterId) {
        String kubeConfig = clusterFacade.getKubeConfig(clusterId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>(kubeConfig, headers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CC-ADMIN') or hasRole('K8S_READER') or hasRole('K8S_DEBUGGER')")
    @GetMapping(value = "{clusterId}/kubeconfig/refresh")
    public ResponseEntity<Boolean> refreshKubeConfig(@PathVariable String clusterId) {
        Boolean result = clusterFacade.refreshKubernetesSARoles(clusterId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CC-ADMIN') or @aclService.hasClusterWriteAccess(authentication, #clusterId)")
    @PostMapping("{clusterId}/refreshResource")
    DeploymentLog refreshResource(@PathVariable String clusterId){
        return deploymentFacade.createClusterResourceDetails(clusterId);
    }

    @GetMapping("{clusterId}/resourceDetails")
    List<ResourceDetails> resourceDetails(@PathVariable String clusterId){
        return deploymentFacade.getClusterResourceDetails(clusterId);
    }

    @GetMapping("{clusterId}/clusterTask")
    public ClusterTask getClusterTask(String clusterId){
        return clusterFacade.getQueuedClusterTaskForClusterId(clusterId);
    }
}
