package com.capillary.ops.deployer.service.interfaces;

import com.capillary.ops.deployer.bo.BuildType;
import com.capillary.ops.deployer.bo.actions.ActionExecution;
import com.capillary.ops.deployer.bo.actions.ApplicationAction;

import java.util.List;

public interface IActionsService {
    List<ApplicationAction> getGenericActions(BuildType buildType);

    List<ApplicationAction> getApplicationActions(String applicationId);

    ApplicationAction saveGenericAction(BuildType buildType, ApplicationAction applicationAction);

    List<ActionExecution> getLastNExecutions(String applicationId, int n, String property);
}
