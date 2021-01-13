package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.bo.requests.ClusterTaskRequest;
import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.ClusterTask;
import com.capillary.ops.cp.bo.QASuite;
import com.capillary.ops.cp.bo.recipes.AuroraDRDeploymentRecipe;
import com.capillary.ops.cp.bo.recipes.MongoDRDeploymentRecipe;
import com.capillary.ops.cp.bo.recipes.ESDRDeploymentRecipe;
import com.capillary.ops.cp.bo.recipes.MongoVolumeResizeDeploymentRecipe;
import com.capillary.ops.cp.bo.recipes.HotfixDeploymentRecipe;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.bo.wrappers.ListDeploymentsWrapper;
import com.capillary.ops.cp.facade.DeploymentFacade;
import com.capillary.ops.cp.service.AclService;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("cc-ui/v1/clusters/{clusterId}/deployments")
@Loggable
public class UiDeploymentController {

    @Autowired
    DeploymentFacade deploymentFacade;

    @Autowired
    private AclService aclService;

    /**
     * Create a new Deployment for latest definition of this cluster
     *
     * @param clusterId         Cluster Id
     * @param deploymentRequest Request Params for deployments
     * @return The Deployment Log Object
     */

    @PreAuthorize("hasRole('CC-ADMIN') or @aclService.hasClusterWriteAccess(authentication, #clusterId)")
    @PostMapping
    DeploymentLog createDeployment(@PathVariable String clusterId, @RequestBody DeploymentRequest deploymentRequest) {
        return deploymentFacade.createDeployment(clusterId, deploymentRequest);
    }

    /**
     * Trigger job for automation suite
     *
     * @param automationSuite QA Automation Suite
     * @param clusterId       Cluster Id
     * @return executionId
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPLOYERS')")
    @PostMapping("/qa/triggerSuite")
    String triggerAutomationSuite(@Valid @RequestBody QASuite automationSuite, @PathVariable String clusterId)
        throws Exception {
        return deploymentFacade.triggerAutomationSuite(clusterId, automationSuite);
    }

    /**
     * Abort job for automation suite
     *
     * @param clusterId   Cluster Id
     * @param executionId Execution Id for automation suite
     * @return void
     */
    @DeleteMapping("/qa/{executionId}/abortSuite")
    void abortAutomationSuite(@PathVariable String clusterId, @PathVariable String executionId) throws Exception {
        deploymentFacade.abortAutomationSuite(clusterId, executionId);
    }

    @GetMapping()
    ListDeploymentsWrapper getDeployments(@PathVariable String clusterId) {
        return deploymentFacade.getAllDeployments(clusterId);
    }

    @GetMapping("/{deploymentId}")
    DeploymentLog getDeployment(@PathVariable String clusterId, @PathVariable String deploymentId) {
        return deploymentFacade.getDeployment(deploymentId);
    }

    @PreAuthorize("hasRole('CC-ADMIN') or @aclService.hasClusterMaintainerAccess(authentication, #clusterId)")
    @PutMapping("/{deploymentId}/signoff")
    DeploymentLog signOffDeployment(@PathVariable String clusterId, @PathVariable String deploymentId) {
        return deploymentFacade.signOff(clusterId, deploymentId);
    }

    @PreAuthorize("hasRole('CC-ADMIN')")
    @PostMapping("/recipes/aurora/dr")
    DeploymentLog runAuroraDRRecipe(@PathVariable String clusterId,
                                    @RequestBody AuroraDRDeploymentRecipe deploymentRecipe) {
        return deploymentFacade.runAuroraDRRecipe(clusterId, deploymentRecipe);
    }

    @PreAuthorize("hasRole('CC-ADMIN')")
    @PostMapping("/recipes/mongo/dr")
    DeploymentLog runMongoDRRecipe(@PathVariable String clusterId,
                                    @RequestBody MongoDRDeploymentRecipe deploymentRecipe) {
        return deploymentFacade.runMongoDRRecipe(clusterId, deploymentRecipe);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/recipes/es/dr")
    DeploymentLog runESDRRecipe(@PathVariable String clusterId,
                                   @RequestBody ESDRDeploymentRecipe deploymentRecipe) {
        return deploymentFacade.runESDRRecipe(clusterId, deploymentRecipe);
    }

    @PreAuthorize("hasRole('CC-ADMIN')")
    @PostMapping("/recipes/mongo/resize")
    DeploymentLog runMongoResizeRecipe(@PathVariable String clusterId,
                                   @RequestBody MongoVolumeResizeDeploymentRecipe deploymentRecipe) {
        return deploymentFacade.runMongoResizeRecipe(clusterId, deploymentRecipe);
    }

    @PreAuthorize("hasRole('CC-ADMIN') or @aclService.hasClusterWriteAccess(authentication, #clusterId)")
    @PostMapping("/recipes/deployment/hotfix")
    DeploymentLog runHotfixDeploymentRecipe(@PathVariable String clusterId,
                                       @RequestBody HotfixDeploymentRecipe deploymentRecipe) {
        return deploymentFacade.runHotfixDeploymentRecipe(clusterId, deploymentRecipe);
    }

    @PostMapping("/clusterTask")
    List<ClusterTask> createClusterTask(@RequestBody ClusterTaskRequest taskRequest){
        return deploymentFacade.createClusterTask(taskRequest);
    }

    @PostMapping("/clusterTask/{stack}")
    List<ClusterTask> getAllClusterTasks(@PathVariable String stackName){
        return deploymentFacade.getClusterTasks(stackName);
    }
}
