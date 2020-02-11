package com.capillary.ops.cp.controller;

import com.capillary.ops.cp.bo.Resource;
import com.capillary.ops.cp.bo.ResourceType;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.deployer.exceptions.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * All calls which can be made by the "Stack Managers"
 */
@RestController
@RequestMapping("sc/v1/stacks/")
public class StackController {

    @Autowired
    StackRepository repository;

    /**
     * Given a stack get all resources
     * <p>
     * USE: will be used by TF
     * TODO: do we need separate method for TF?
     *
     * @param stackName     Name of the stack
     * @param resourceTypeO Optional Filter
     * @return List of Resource
     */
    @GetMapping("{stackName}/resources")
    public List<Resource> getClusterResources(@PathVariable String stackName,
        @RequestParam(name = "type", required = false) Optional<ResourceType> resourceTypeO) {
        throw new NotImplementedException("Not Implemented Yet");
    }

    /**
     * Create a new stack
     *
     * @param stack Stack definition object
     * @return
     */
    @PostMapping
    public Stack createStack(@RequestBody Stack stack) {
        return repository.save(stack);
    }
}
