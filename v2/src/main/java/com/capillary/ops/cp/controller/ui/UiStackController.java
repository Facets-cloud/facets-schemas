package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.controller.StackController;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * All calls which can be made by the "Stack Managers"
 */
@RestController
@RequestMapping("cc-ui/v1/stacks/")
@Loggable()
@PreAuthorize("hasAnyRole('ADMIN')")
public class UiStackController {

    @Autowired
    StackController stackController;

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
    public Stack createStack(@RequestBody Stack stack) {
        return stackController.createStack(stack);
    }

    @GetMapping()
    public List<Stack> getStacks() {
        return stackController.getStacks();
    }

    @GetMapping("{stackName}")
    public Stack getStack(@PathVariable String stackName) {
        return stackController.getStack(stackName);
    }
}
