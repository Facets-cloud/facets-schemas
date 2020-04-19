package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.facade.ClusterFacade;
import com.capillary.ops.cp.facade.StackFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * All calls which can be made by the "Stack Managers"
 */
@RestController
@RequestMapping("cc/v1/stacks/")
public class StackController {

    @Autowired
    StackFacade stackFacade;

    @Autowired
    ClusterFacade clusterFacade;

    @GetMapping("{stackName}/clusters")
    public List<AbstractCluster> getClusters(@PathVariable String stackName) {
        return clusterFacade.getClustersByStackName(stackName);
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
}
