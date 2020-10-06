package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.notifications.Subscription;
import com.capillary.ops.cp.controller.StackController;
import com.capillary.ops.cp.facade.SubscriptionFacade;
import com.capillary.ops.cp.service.AclService;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private AclService aclService;

    @GetMapping("{stackName}/clusters")
    @PostFilter("hasAnyRole('ADMIN') or @aclService.hasClusterReadAccess(authentication, #stackName, filterObject.id)")
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
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Stack createStack(@RequestBody Stack stack) {
        return stackController.createStack(stack);
    }

    @GetMapping("{stackName}/reload")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Stack reloadStack(@PathVariable String stackName) {
        return stackController.reloadStack(stackName);
    }

    @GetMapping()
    @PostFilter("hasAnyRole('ADMIN') or @aclService.hasStackReadAccess(authentication, filterObject.name)")
    public List<Stack> getStacks() {
        return stackController.getStacks();
    }

    @GetMapping("{stackName}")
    @PreAuthorize("hasAnyRole('ADMIN') or @aclService.hasStackReadAccess(authentication, #stackName)")
    public Stack getStack(@PathVariable String stackName) {
        return stackController.getStack(stackName);
    }

    @GetMapping("{stackName}/notification/subscriptions")
    //@PostFilter("hasAnyRole('ADMIN') or @aclService.hasClusterReadAccess(authentication, #stackName, filterObject.id)")
    public List<Subscription> getAllSubscriptions(@PathVariable String stackName) {
        return subscriptionFacade.getAllSubscriptions(stackName);
    }

    @PostMapping("{stackName}/notification/subscriptions")
    //@PostFilter("hasAnyRole('ADMIN') or @aclService.hasClusterReadAccess(authentication, #stackName, filterObject.id)")
    public Subscription createSubscription(@PathVariable String stackName, @RequestBody Subscription subscription) {
        subscription.setStackName(stackName);
        return subscriptionFacade.createSubscription(subscription);
    }

}
