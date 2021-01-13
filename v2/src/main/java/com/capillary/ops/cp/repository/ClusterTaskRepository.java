package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.ClusterTask;
import com.capillary.ops.cp.bo.TaskStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClusterTaskRepository extends MongoRepository<ClusterTask, String> {

    List<ClusterTask> findAllByStackName(String stackName);

    ClusterTask findOneById(String Id);

    ClusterTask findOneByStackNameAndClusterIdAndTaskStatus(String stackName, String ClusterId, TaskStatus taskStatus);

    void deleteById(String Id);

    void deleteByClusterId(String clusterId);

    void deleteByStack(String stackName);
}
