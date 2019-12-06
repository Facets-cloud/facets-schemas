package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.actions.ActionExecution;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActionExecutionRepository  extends MongoRepository<ActionExecution, String> {
}
