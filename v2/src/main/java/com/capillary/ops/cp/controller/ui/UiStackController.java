package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.StackFile;
import com.capillary.ops.cp.bo.Substack;
import com.capillary.ops.cp.bo.ToggleRelease;
import com.capillary.ops.cp.bo.*;
import com.capillary.ops.cp.bo.notifications.Subscription;
import com.capillary.ops.cp.bo.requests.ClusterTaskRequest;
import com.capillary.ops.cp.controller.StackController;
import com.capillary.ops.cp.facade.DeploymentFacade;
import com.capillary.ops.cp.facade.StackFacade;
import com.capillary.ops.cp.facade.SubscriptionFacade;
import com.capillary.ops.cp.service.AclService;
import com.capillary.ops.cp.service.StackAutoCompleteService;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * All calls which can be made by the "Stack Managers"
 */
@RestController
@RequestMapping("cc-ui/v1/stacks/")
@Loggable()
public class UiStackController {

    @Autowired
    private SubscriptionFacade subscriptionFacade;

    @Autowired
    StackController stackController;

    @Autowired
    StackAutoCompleteService stackAutoCompleteService;

    @Autowired
    private AclService aclService;

    @Autowired
    private StackFacade stackFacade;

    @GetMapping("{stackName}/clusters")
    public List<AbstractCluster> getClusters(@PathVariable String stackName) {
        return stackController.getClusters(stackName);
    }

    /**
     * Create a new stack
     *
     * @param stack Stack definition object
     * @return
     */
    @PostMapping
    @PreAuthorize("hasRole('CC-ADMIN')")
    public Stack createStack(@RequestBody Stack stack) {
        return stackController.createStack(stack);
    }

    @GetMapping("{stackName}/reload")
    @PreAuthorize("hasRole('CC-ADMIN') or @aclService.hasStackWriteAccess(authentication, #stackName)")
    public Stack reloadStack(@PathVariable String stackName) {
        return stackController.reloadStack(stackName);
    }

    @GetMapping()
    public List<Stack> getStacks() {
        return stackController.getStacks();
    }

    @GetMapping("{stackName}")
    public Stack getStack(@PathVariable String stackName) {
        Stack stackObj = stackController.getStack(stackName);
        Map<String, StackFile.VariableDetails> clusterVariablesFiltered = stackObj.getClusterVariablesMeta()
                .entrySet().stream().filter(x -> x.getValue().isSecret()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                .keySet().stream().collect(Collectors.toMap(Function.identity(), x -> new StackFile.VariableDetails(true,"****")));
        stackObj.getClusterVariablesMeta().putAll(clusterVariablesFiltered);
        return stackObj;
    }

    @GetMapping("{stackName}/notification/subscriptions")
    public List<Subscription> getAllSubscriptions(@PathVariable String stackName) {
        return subscriptionFacade.getAllSubscriptions(stackName);
    }

    @GetMapping("{stackName}/suggestions/resourceType")
    public Set<String> getResourceTypes(@PathVariable String stackName) {
        return stackAutoCompleteService.getResourceTypesSuggestion(stackName);
    }

    @GetMapping("{stackName}/suggestions/resourceType/{resourceType}")
    public Set<String> getResourcesByTypes(@PathVariable String stackName, @PathVariable String resourceType) {
        return stackAutoCompleteService.getResourcesSuggestion(stackName, resourceType);
    }

    @PostMapping("{stackName}/notification/subscriptions")
    public Subscription createSubscription(@PathVariable String stackName, @RequestBody Subscription subscription) {
        subscription.setStackName(stackName);
        return subscriptionFacade.createSubscription(subscription);
    }

    @PreAuthorize("hasRole('CC-ADMIN') or @aclService.hasClusterWriteAccess(authentication, #stackName, null)")
    @PostMapping("{stackName}/toggleRelease")
    public ToggleRelease toggleRelease(@PathVariable String stackName, @RequestBody ToggleRelease toggleRelease){
        return stackFacade.toggleRelease(toggleRelease);
    }

    @PostMapping("substack/{substackName}")
    public Substack createSubStack(@RequestBody Substack subStack, @PathVariable String substackName) throws IOException {
        return stackController.createSubstack(subStack, substackName);
    }

    @GetMapping("{stackName}/localDeploymentContext")
    public DeploymentContext getLocalDeploymentContext(@PathVariable String stackName) {
        return stackFacade.getLocalDeploymentContext(stackName);
    }

    @PreAuthorize("hasRole('CC-ADMIN') or @aclService.hasClusterWriteAccess(authentication, #clusterId)")
    @PostMapping("clusterTask")
    public List<ClusterTask> createClusterTasks(@RequestBody ClusterTaskRequest taskRequest) throws Exception {
        return stackController.createClusterTasks(taskRequest);
    }

    @GetMapping("clusterTask/{stackName}")
    public List<ClusterTask> getAllClusterTasks(@PathVariable String stackName){
        return stackController.getClusterTasks(stackName);
    }
}
