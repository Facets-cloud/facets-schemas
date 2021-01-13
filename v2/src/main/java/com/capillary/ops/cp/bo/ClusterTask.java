package com.capillary.ops.cp.bo;

import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class ClusterTask {

    public ClusterTask() {}

    public ClusterTask(String stackName,String clusterId,List<String> tasks){
        this.stackName = stackName;
        this.clusterId = clusterId;
        this.tasks = tasks;
    }

    @Id
    @JsonIgnore
    String id;

    String stackName;

    String clusterId;

    List<String> tasks;

    TaskStatus taskStatus = TaskStatus.QUEUED;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStackName() {
        return stackName;
    }

    public void setStackName(String stackName) {
        this.stackName = stackName;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
}
