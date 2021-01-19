package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.*;
import com.capillary.ops.cp.bo.requests.ClusterTaskRequest;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.repository.AuditLogRepository;
import com.capillary.ops.cp.repository.ClusterTaskRepository;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.cp.repository.SubstackRepository;
import com.capillary.ops.cp.service.GitService;
import com.capillary.ops.cp.service.StackAutoCompleteService;
import com.capillary.ops.cp.service.StackService;
import com.capillary.ops.cp.service.notification.FlockNotifier;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.utils.DeployerUtil;
import com.google.common.collect.ImmutableMap;
import com.jcabi.aspects.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Loggable
public class StackFacade {

    @Autowired
    StackRepository stackRepository;

    @Autowired
    SubstackRepository substackRepository;

    @Autowired
    GitService gitService;

    @Autowired
    StackAutoCompleteService stackAutoCompleteService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private FlockNotifier flockNotifier;

    @Autowired
    private StackService stackService;

    @Autowired
    private ArtifactFacade artifactFacade;

    @Autowired
    private ClusterFacade clusterFacade;

    @Value("${flock.notification.pauseReleases.endpoint}")
    private String pauseReleaseNotifier;

    @Autowired
    private ClusterTaskRepository clusterTaskRepository;

    private static final Logger logger = LoggerFactory.getLogger(StackFacade.class);

    /**
     * Create a stack given details
     *
     * @param stack
     * @return
     */
    @CacheEvict(value = { "substacks", "substackNames" }, key = "#stack.name")
    public Stack createStack(Stack stack) {
        Path location = checkoutRepository(stack);
        String relativePath = (StringUtils.isEmpty(stack.getRelativePath())) ? "" : stack.getRelativePath();
        Stack updatedSack = stackService.syncWithStackFile(stack, location);
        stackAutoCompleteService.parseStack(stack.getName(), location.toString() + "/" + relativePath + "/");
        return stackRepository.save(updatedSack);
    }

    private Path checkoutRepository(Stack stack) {
        Path location;
        try {
            location = gitService.checkout(stack.getVcsUrl(), stack.getUser(), stack.getAppPassword());
        } catch (Throwable e) {
            throw new IllegalArgumentException("Unable to checkout the URL", e);
        }
        return location;
    }

    @CacheEvict(value = { "substacks", "substackNames" })
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

    public Substack createSubstack(Substack substack) throws IOException {
        Path location = checkoutRepository(substack);
        Substack updatedSubstack = (Substack) stackService.syncWithStackFile(substack, location);
        String s3Path = stackService.uploadSubstack(updatedSubstack, location);
        updatedSubstack.setArtifactPath(s3Path);
        String relativePath = (StringUtils.isEmpty(substack.getRelativePath())) ? "" : substack.getRelativePath();
        stackAutoCompleteService.parseStack(substack.getName(), location.toString() + "/" + relativePath + "/");
        return substackRepository.save(updatedSubstack);
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


    public List<Substack> getSubstacks(String stackName) {
        return stackService.getSubstacks(stackName);
    }

    public Substack getSubstacks(String stackName, String substackName) {
        return stackService.getSubstack(substackName);
    }
    public DeploymentContext getLocalDeploymentContext(String stackName) {
        Map<String, Map<String, Artifact>> allArtifacts = artifactFacade.getAllArtifacts(BuildStrategy.QA, ReleaseType.RELEASE);
        DeploymentContext deploymentContext = new DeploymentContext();
        deploymentContext.setArtifacts(allArtifacts);
        return deploymentContext;
    }

    public List<ClusterTask> createClusterTasks(ClusterTaskRequest taskRequest) throws Exception {
        List<ClusterTask> pendingClusterTasks = getPendingClusterTasks();
        if(pendingClusterTasks.size()>0){
            logger.error("Pending cluster tasks exist. Please execute them first");
            throw new Exception("Pending cluster tasks exist");
        }
        List<ClusterTask> clusterTasks = new ArrayList<>();
        if (taskRequest.getStackName() == null) {
            List<Stack> stackList = getAllStacks();
            for(Stack s: stackList){
                List<AbstractCluster> clusterList = clusterFacade.getClustersByStackName(s.getName());
                for(AbstractCluster c: clusterList){
                    ClusterTask task = new ClusterTask(s.getName(),c.getName(),taskRequest.getTasks());
                    clusterTasks.add(task);
                }
            }
        }else{
            if(taskRequest.getClusterId() == null){
                List<AbstractCluster> clusterList = clusterFacade.getClustersByStackName(taskRequest.getStackName());
                for(AbstractCluster c: clusterList){
                    ClusterTask task = new ClusterTask(taskRequest.getStackName(),c.getName(),taskRequest.getTasks());
                    clusterTasks.add(task);
                }
            }else {
                ClusterTask task = new ClusterTask(taskRequest.getStackName(), taskRequest.getClusterId(), taskRequest.getTasks());
                clusterTasks.add(task);
            }
        }
        return clusterTaskRepository.saveAll(clusterTasks);
    }

    public List<ClusterTask> getClusterTasks(String stackName,TaskStatus taskStatus) {
        return clusterTaskRepository.findFirst30ByStackNameAndTaskStatus(stackName, taskStatus);
    }

    public List<ClusterTask> getClusterTasks(String stackName) {
        return clusterTaskRepository.findFirst30ByStackName(stackName);
    }

    public List<ClusterTask> getPendingClusterTasks(){
        return clusterTaskRepository.findFirst15ByTaskStatus(TaskStatus.QUEUED);
    }
}
