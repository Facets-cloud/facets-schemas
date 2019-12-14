package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.actions.ActionExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ActionExecutionRepository extends MongoRepository<ActionExecution, String> {
    Page<ActionExecution> findAllByApplicationId(String applicationId, Pageable pageable);
}