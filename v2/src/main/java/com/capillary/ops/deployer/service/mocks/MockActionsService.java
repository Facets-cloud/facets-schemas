package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.BuildType;
import com.capillary.ops.deployer.bo.actions.ActionType;
import com.capillary.ops.deployer.bo.actions.ApplicationAction;
import com.capillary.ops.deployer.bo.actions.CreationStatus;
import com.capillary.ops.deployer.service.interfaces.IActionsService;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Profile("dev")
@Service
public class MockActionsService implements IActionsService {

    @Override
    public List<ApplicationAction> getGenericActions(BuildType buildType) {
        ApplicationAction ps = new ApplicationAction(UUID.randomUUID().toString(), "ps", null,
                ActionType.GENERIC, "/commands/generic/ps", null, null,
                BuildType.MVN, CreationStatus.FULFILLED, null);

        ApplicationAction ls = new ApplicationAction(UUID.randomUUID().toString(), "ls", null,
                ActionType.GENERIC, "/commands/generic/ls", null, null,
                BuildType.MVN, CreationStatus.FULFILLED, null);

        ApplicationAction lsal = new ApplicationAction(UUID.randomUUID().toString(), "ls -al", null,
                ActionType.GENERIC, "/commands/generic/lsal", null, null,
                BuildType.MVN, CreationStatus.FULFILLED, null);

        return Lists.newArrayList(ps, ls, lsal);
    }

    @Override
    public List<ApplicationAction> getApplicationActions(String applicationId) {
        ApplicationAction jmap = new ApplicationAction(UUID.randomUUID().toString(), "jmap", null,
                ActionType.CUSTOM, "/commands/custom/jmap", null, null,
                BuildType.MVN, CreationStatus.FULFILLED, null);

        ApplicationAction custom = new ApplicationAction(UUID.randomUUID().toString(), "custom", null,
                ActionType.CUSTOM, "/commands/custom/custom", null, null,
                BuildType.MVN, CreationStatus.FULFILLED, null);

        return Lists.newArrayList(jmap, custom);
    }

    @Override
    public ApplicationAction saveGenericAction(BuildType buildType, ApplicationAction applicationAction) {
        return null;
    }
}
