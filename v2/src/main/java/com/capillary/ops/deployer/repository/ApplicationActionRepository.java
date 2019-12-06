package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.BuildType;
import com.capillary.ops.deployer.bo.actions.ActionType;
import com.capillary.ops.deployer.bo.actions.ApplicationAction;
import com.capillary.ops.deployer.bo.actions.CreationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApplicationActionRepository extends MongoRepository<ApplicationAction, String> {

    List<ApplicationAction> findByActionTypeAndBuildTypeAndCreationStatus(ActionType actionType, BuildType buildType, CreationStatus creationStatus);

    List<ApplicationAction> findByActionTypeAndApplicationIdAndCreationStatus(ActionType actionType, String applicationId, CreationStatus creationStatus);
}
