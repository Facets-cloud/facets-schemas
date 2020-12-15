package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.ToggleRelease;
import com.capillary.ops.cp.facade.ClusterFacade;
import com.capillary.ops.cp.facade.StackFacade;
import com.capillary.ops.cp.service.AclService;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * All calls which can be made by the "Stack Managers"
 */
@RestController
@RequestMapping("cc/v1/stacks/")
@Loggable()
public class StackController {

    @Autowired
    StackFacade stackFacade;

    @Autowired
    ClusterFacade clusterFacade;

    @Autowired
    private AclService aclService;

    @GetMapping("{stackName}/clusters")
    public List<AbstractCluster> getClusters(@PathVariable String stackName) {
        return clusterFacade.getClustersByStackName(stackName);
    }

    @GetMapping()
    public List<Stack> getStacks() {
        return stackFacade.getAllStacks();
    }

    /**
     * Create a new stack
     *
     * @param stack Stack definition object
     * @return
     */
    @PostMapping
    public Stack createStack(@RequestBody Stack stack) {
        return stackFacade.createStack(stack);
    }

    @GetMapping("{stackName}/reload")
    public Stack reloadStack(@PathVariable String stackName) {
        return stackFacade.reloadStack(stackName);
    }

    public Stack getStack(String stackName) {
        return stackFacade.getStackByName(stackName);
    }

}
