package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.cp.service.GitService;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
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

        if (stackRepository.findById(stack.getName()).isPresent()) {
            throw new IllegalStateException("Stack already exists and cannot be created again");
        }
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
            JSONParser parser = new JSONParser(new FileReader(stackFile));
            LinkedHashMap<String, Object> vars = parser.object();
            Map<String, String> stringVars = new HashMap((Map) vars.get("variables"));
            stack.setStackVars(stringVars);
        } catch (Throwable e) {
            throw new IllegalArgumentException(
                "Invalid Stack Definition in given Directory " + stack.getVcsUrl() + "" + stack.getRelativePath(), e);
        }
        return stackRepository.save(stack);
    }

}
