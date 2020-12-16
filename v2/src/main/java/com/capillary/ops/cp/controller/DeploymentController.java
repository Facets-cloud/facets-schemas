package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.QASuiteResult;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.bo.QASuite;
import com.capillary.ops.cp.bo.wrappers.ListDeploymentsWrapper;
import com.capillary.ops.cp.facade.DeploymentFacade;
import com.capillary.ops.deployer.exceptions.NotImplementedException;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("cc/v1/clusters/{clusterId}/deployments")
@Loggable
public class DeploymentController {

    @Autowired
    DeploymentFacade deploymentFacade;

    /**
     * Get log for the deployment
     *
     * @param id Deployment Id
     * @return List of Logs
     */
    @GetMapping("/{id}")
    List<String> getLogs(@PathVariable String clusterId, @PathVariable String id) {
        throw new NotImplementedException("Getting Logs is Not implemented yet");
    }

    /**
     * Trigger job for automation suite
     *
     * @param automationSuite QA Automation Suite
     * @param clusterId Cluster Id
     * @return executionId
     */
    @PostMapping("/qa/triggerSuite")
    String triggerAutomationSuite(
            @Valid @RequestBody QASuite automationSuite,
            @PathVariable String clusterId) throws Exception {
        return deploymentFacade.triggerAutomationSuite(clusterId, automationSuite);
    }

    /**
     * Abort job for automation suite
     *
     * @param clusterId Cluster Id
     * @param executionId Execution Id for automation suite
     * @return void
     */
    @DeleteMapping("/qa/{executionId}/abortSuite")
    void abortAutomationSuite(
            @PathVariable String clusterId,
            @PathVariable String executionId) throws Exception {
        deploymentFacade.abortAutomationSuite(clusterId, executionId);
    }

    @GetMapping()
    ListDeploymentsWrapper getDeployments(@PathVariable String clusterId) {
        return deploymentFacade.getAllDeployments(clusterId);
    }

    /**
     * Get job status for automation suite
     *
     * @param clusterId Cluster Id
     * @param executionId Execution Id for automation suite
     * @return String
     */
    @GetMapping("/qa/{executionId}/status")
    String getAutomationSuiteStatus(
            @PathVariable String clusterId,
            @PathVariable String executionId) throws Exception {
        return deploymentFacade.getAutomationSuiteStatus(clusterId, executionId);
    }

    /**
     * Validate the QA suite result
     * @param clusterId
     * @param qaSuiteResult
     */
    @PostMapping("/qa/validateSanityResult")
    void validateSanityResult(
            @PathVariable String clusterId,
            @Valid @RequestBody QASuiteResult qaSuiteResult) throws Exception {
        deploymentFacade.validateSanityResult(clusterId, qaSuiteResult);
    }
}
