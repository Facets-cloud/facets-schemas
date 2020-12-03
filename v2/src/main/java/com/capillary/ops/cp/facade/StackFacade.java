package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.AuditLog;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.StackFile;
import com.capillary.ops.cp.bo.ToggleRelease;
import com.capillary.ops.cp.repository.AuditLogRepository;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.cp.service.GitService;
import com.capillary.ops.cp.service.StackAutoCompleteService;
import com.capillary.ops.cp.service.notification.FlockNotifier;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.utils.DeployerUtil;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.jcabi.aspects.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Loggable
public class StackFacade {

    @Autowired
    StackRepository stackRepository;

    @Autowired
    GitService gitService;

    @Autowired
    StackAutoCompleteService stackAutoCompleteService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private FlockNotifier flockNotifier;

    @Value("${flock.notification.pauseReleases.endpoint}")
    private String pauseReleaseNotifier;

    private static final Logger logger = LoggerFactory.getLogger(StackFacade.class);
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
            stackAutoCompleteService.parseStack(stack.getName(), location.toString() + "/" + relativePath + "/");
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

    public ToggleRelease toggleRelease(ToggleRelease toggleRelease){
        Optional<Stack> stack =  stackRepository.findById(toggleRelease.getStackName());
        if(!stack.isPresent()){
            throw new NotFoundException("Stack with name " + toggleRelease.getStackName() + " not found");
        }

        Stack existingStack = stack.get();
        existingStack.setPauseReleases(toggleRelease.isPauseReleases());

        String action = existingStack.isPauseReleases() ? "paused release" : "enabled release";
        ImmutableMap<String, Object> map = ImmutableMap.of("stackName", toggleRelease.getStackName(), "pauseRelease", existingStack.isPauseReleases());
        String authUserName = DeployerUtil.getAuthUserName();
        AuditLog auditLog = new AuditLog(action, Instant.now(), authUserName, "changing the prod release", map);
        auditLogRepository.save(auditLog);

        String notificationMsg = String.format("pause releases value set to %s by user %s for the stack %s",existingStack.isPauseReleases(), authUserName, existingStack.getName());
        logger.info(notificationMsg);
        stackRepository.save(existingStack);
        sendPauseNotification(notificationMsg);
        return toggleRelease;
    }

    private void sendPauseNotification(String message){
        try{
            flockNotifier.notify(pauseReleaseNotifier, message);
        } catch (Exception e){
            logger.info("sending flock notification for pause release failed");
            logger.info("exception occured while sending is ", e);
        }
    }


}
