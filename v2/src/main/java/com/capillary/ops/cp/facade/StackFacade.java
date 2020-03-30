package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.cp.service.GitService;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StackFacade {

    @Autowired
    StackRepository stackRepository;

    @Autowired
    GitService gitService;

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
            Map<String, String> stringVars = new HashMap(vars);
            stack.setStackVars(stringVars);
        } catch (FileNotFoundException | ParseException e) {
            throw new IllegalArgumentException(
                "Invalid Stack Definition in given Directory " + stack.getVcsUrl() + "" + stack.getRelativePath());
        }
        return stackRepository.save(stack);
    }

}
