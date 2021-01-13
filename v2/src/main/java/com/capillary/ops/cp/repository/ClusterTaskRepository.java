package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.ClusterTask;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClusterTaskRepository extends MongoRepository<ClusterTask, String> {

    List<ClusterTask> findAllByStackName(String stackName);

    ClusterTask findOneById(String Id);

    ClusterTask findOneByStackNameAndClusterId(String stackName, String ClusterId);

    void deleteById(String Id);

    void deleteByClusterId(String clusterId);

    void deleteByStack(String stackName);
}
