package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.StackFile;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.cp.service.GitService;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.google.gson.Gson;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
@Loggable
public class StackFacade {

    @Autowired
    StackRepository stackRepository;

    @Autowired
    GitService gitService;

    /**
     * Create a stack given details
     *
     * @param stack
     * @return
     */
    public Stack createStack(Stack stack) {
        stackRepository.findById(stack.getName());
//        if (stackRepository.findById(stack.getName()).isPresent()) {
//            throw new IllegalStateException("Stack already exists and cannot be created again");
//        }
        Path location;
        try {
            location = gitService.checkout(stack.getVcsUrl(), stack.getUser(), stack.getAppPassword());
        } catch (Throwable e) {
            throw new IllegalArgumentException("Unable to checkout the URL", e);
        }
        String relativePath =
            (stack.getRelativePath() == null || stack.getRelativePath().isEmpty()) ? "" : stack.getRelativePath();
        // Verify stack.json and persist it
        File stackFile = new File(location.toString() + "/" + relativePath + "/stack.json");
        if (!stackFile.isFile()) {
            throw new IllegalArgumentException(
                "No Stack Definition in given Directory " + stack.getVcsUrl() + "" + stack.getRelativePath());
        }
        try {
            Gson gson = new Gson();
            StackFile file = gson.fromJson(new FileReader(stackFile), StackFile.class);
            stack.setStackVars(file.getStackVariables());
            stack.setClusterVariablesMeta(file.getClusterVariablesMeta());
        } catch (Throwable e) {
            throw new IllegalArgumentException(
                "Invalid Stack Definition in given Directory " + stack.getVcsUrl() + "" + stack.getRelativePath(), e);
        }
        return stackRepository.save(stack);
    }

    public Stack reloadStack(String stackName) {
        Optional<Stack> optionalStack = stackRepository.findById(stackName);
        if (!optionalStack.isPresent()) {
            throw new NotFoundException("Stack with name " + stackName + " not found");
        }

        return createStack(optionalStack.get());
    }

    public List<Stack> getAllStacks() {
        return stackRepository.findAll();
    }

    public Stack getStackByName(String stackName) {
        return stackRepository.findById(stackName).get();
    }
}
