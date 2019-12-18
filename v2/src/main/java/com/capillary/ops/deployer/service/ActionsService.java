package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.BuildType;
import com.capillary.ops.deployer.bo.actions.ActionExecution;
import com.capillary.ops.deployer.bo.actions.ActionType;
import com.capillary.ops.deployer.bo.actions.ApplicationAction;
import com.capillary.ops.deployer.bo.actions.CreationStatus;
import com.capillary.ops.deployer.repository.ActionExecutionRepository;
import com.capillary.ops.deployer.repository.ApplicationActionRepository;
import com.capillary.ops.deployer.service.interfaces.IActionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Profile("!dev")
@Service
public class ActionsService implements IActionsService {

    @Autowired
    private ApplicationActionRepository applicationActionRepository;

    @Autowired
    private ActionExecutionRepository actionExecutionRepository;

    private static final Logger logger = LoggerFactory.getLogger(ActionsService.class);

    @Override
    public List<ApplicationAction> getGenericActions(BuildType buildType) {
        return applicationActionRepository.findByActionTypeAndBuildTypeAndCreationStatus(
                ActionType.GENERIC, buildType, CreationStatus.FULFILLED);
    }

    @Override
    public List<ApplicationAction> getApplicationActions(String applicationId) {
        logger.info("getting actions for applicationId: {}", applicationId);
        return applicationActionRepository.findByActionTypeAndApplicationIdAndCreationStatus(
                ActionType.CUSTOM, applicationId, CreationStatus.FULFILLED);
    }

    @Override
    public ApplicationAction saveGenericAction(BuildType buildType, ApplicationAction applicationAction) {
        applicationAction.setApplicationId(null);
        applicationAction.setBuildType(buildType);
        applicationAction.setActionType(ActionType.GENERIC);
        applicationAction.setCreationStatus(CreationStatus.FULFILLED);
        return applicationActionRepository.save(applicationAction);
    }

    @Override
    public List<ActionExecution> getLastNExecutions(String applicationId, int n, String property) {
        Page<ActionExecution> executions = actionExecutionRepository.findAllByApplicationId(
                applicationId, PageRequest.of(0, n, Sort.by(property).descending()));
        return executions.getContent();
    }
}
