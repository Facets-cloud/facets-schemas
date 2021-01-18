package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.ClusterTask;
import com.capillary.ops.cp.bo.TaskStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClusterTaskRepository extends MongoRepository<ClusterTask, String> {

    List<ClusterTask> findFirst30ByStackNameAndTaskStatus(String stackName,TaskStatus taskStatus);

    List<ClusterTask> findFirst15ByClusterIdAndTaskStatus(String clusterId, TaskStatus taskStatus);

    List<ClusterTask> findFirst15ByTaskStatus(TaskStatus taskStatus);

    List<ClusterTask> findFirst30ByStackName(String stackName);
}
