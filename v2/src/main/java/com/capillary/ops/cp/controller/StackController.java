package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.*;
import com.capillary.ops.cp.bo.requests.ClusterTaskRequest;
import com.capillary.ops.cp.facade.ClusterFacade;
import com.capillary.ops.cp.facade.StackFacade;
import com.capillary.ops.cp.service.AclService;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @PreAuthorize("hasRole('CC-ADMIN')")
    @PostMapping("{stackName}/toggleRelease")
    public ToggleRelease toggleRelease(@PathVariable String stackName, @RequestBody ToggleRelease toggleRelease){
        return stackFacade.toggleRelease(toggleRelease);
    }


    @PostMapping("substack/{substackName}")
    public Substack createSubstack(@RequestBody Substack subStack, @PathVariable String substackName) throws IOException {
        return stackFacade.createSubstack(subStack);
    }

    @GetMapping("substack")
    public List<Substack> getSubstacks(@PathVariable String stackName) {
        return stackFacade.getSubstacks(stackName);
    }

    @GetMapping("substack/{substackName}")
    public Substack getSubstack(@PathVariable String stackName, @PathVariable String substackName) {
        return stackFacade.getSubstacks(stackName, substackName);
    }

    @PostMapping("clusterTask")
    public List<ClusterTask> createClusterTasks(@RequestBody ClusterTaskRequest taskRequest) throws Exception{
        return stackFacade.createClusterTasks(taskRequest);
    }

    @GetMapping("clusterTask/{stackName}")
    public List<ClusterTask> getClusterTasks(@PathVariable String stackName){
        return stackFacade.getClusterTasks(stackName);
    }
}
