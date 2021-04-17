package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.ClusterTask;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.TaskStatus;
import com.capillary.ops.cp.bo.requests.ClusterTaskRequest;
import com.capillary.ops.cp.repository.ClusterTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClusterTaskService {

    @Autowired
    private ClusterTaskRepository clusterTaskRepository;

    @Autowired
    private StackService stackService;

    @Autowired
    private CommonClusterService commonClusterService;

    private static final Logger logger = LoggerFactory.getLogger(ClusterTaskService.class);

    public List<ClusterTask> getPendingClusterTasks(){
        Optional<List<ClusterTask>> tasks = clusterTaskRepository.findFirst15ByTaskStatus(TaskStatus.QUEUED);
        if(tasks.isPresent() && !tasks.get().isEmpty()){
            return tasks.get();
        }
        return null;
    }

    public List<ClusterTask> createClusterTasks(ClusterTaskRequest taskRequest) throws RuntimeException {
        List<ClusterTask> pendingClusterTasks = getPendingClusterTasks();
        if(pendingClusterTasks != null && pendingClusterTasks.size()>0){
            logger.error("Pending cluster tasks exist. Please execute them first");
            throw new RuntimeException("Pending cluster tasks exist");
        }
        List<ClusterTask> clusterTasks = new ArrayList<>();
        if (taskRequest.getStackName() == null) {
            List<Stack> stackList = stackService.getAllStacks();
            for(Stack s: stackList){
                List<AbstractCluster> clusterList = commonClusterService.getClustersByStackName(s.getName());
                for(AbstractCluster c: clusterList){
                    ClusterTask task = new ClusterTask(s.getName(),c.getName(),taskRequest.getTasks());
                    clusterTasks.add(task);
                }
            }
        }else{
            if(taskRequest.getClusterId() == null){
                List<AbstractCluster> clusterList = commonClusterService.getClustersByStackName(taskRequest.getStackName());
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

    public ClusterTask getQueuedClusterTaskForClusterId(String clusterId){
        Optional<List<ClusterTask>> tasks = clusterTaskRepository.findFirst15ByClusterIdAndTaskStatus(clusterId,TaskStatus.QUEUED);
        if(tasks.isPresent() && !tasks.get().isEmpty()){
            return tasks.get().get(0);
        }
        return null;
    }

    public ClusterTask disableClusterTask(String taskId) throws Exception {
        Optional<ClusterTask> task = clusterTaskRepository.findById(taskId);
        ClusterTask modifiedTask = null;
        if(!task.isPresent()){
            throw new Exception("Task does not exist");
        }
        if(!task.get().getTaskStatus().equals(TaskStatus.QUEUED)){
            throw new Exception("Task not in QUEUED state");
        }
        modifiedTask = task.get();
        modifiedTask.setTaskStatus(TaskStatus.DISABLED);
        clusterTaskRepository.save(modifiedTask);
        return modifiedTask;
    }

    public ClusterTask enableClusterTask(String taskId) throws Exception {
        Optional<ClusterTask> task = clusterTaskRepository.findById(taskId);
        ClusterTask modifiedTask = null;
        if(!task.isPresent()){
            throw new Exception("Task does not exist");
        }
        if(!task.get().getTaskStatus().equals(TaskStatus.DISABLED)){
            throw new Exception("Task not in DISABLED state");
        }
        modifiedTask = task.get();
        modifiedTask.setTaskStatus(TaskStatus.QUEUED);
        clusterTaskRepository.save(modifiedTask);
        return modifiedTask;
    }
}
