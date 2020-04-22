package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.AbstractCluster;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface CpClusterRepository extends MongoRepository<AbstractCluster, String> {

    List<AbstractCluster> findAllByStackName(String stackName);

    Optional<AbstractCluster> findByNameAndStackName(String name, String stackName);
}
