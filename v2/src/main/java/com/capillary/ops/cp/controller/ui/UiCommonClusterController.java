package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.bo.OverrideObject;
import com.capillary.ops.cp.bo.requests.OverrideRequest;
import com.capillary.ops.cp.facade.ClusterFacade;
import com.capillary.ops.cp.service.AclService;
import com.jcabi.aspects.Loggable;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cc-ui/v1/clusters")
@Loggable
public class UiCommonClusterController {

    @Autowired
    ClusterFacade clusterFacade;

    @Autowired
    private AclService aclService;

    //@GetMapping("{clusterId}/deployments/{value}")
    public Deployment getDeploymentInCluster(@PathVariable String clusterId, @PathVariable String value,
        @RequestParam(value = "lookup", defaultValue = "deployerid") String lookupKey) {
        return clusterFacade.getApplicationData(clusterId, lookupKey, value);
    }

    @PreAuthorize("hasAnyRole('ADMIN') or @aclService.hasClusterWriteAccess(authentication, #clusterId)")
    @PostMapping("{clusterId}/overrides")
    public List<OverrideObject> overrideSizing(@PathVariable String clusterId,
        @RequestBody List<OverrideRequest> request) {

        return clusterFacade.override(clusterId, request);
    }

    @PreAuthorize("hasAnyRole('ADMIN') or @aclService.hasClusterReadAccess(authentication, #clusterId)")
    @GetMapping("{clusterId}/overrides")
    public List<OverrideObject> getOverrides(@PathVariable String clusterId) {
        List<OverrideObject> overrides = clusterFacade.getOverrides(clusterId);
        return overrides;
    }
}
