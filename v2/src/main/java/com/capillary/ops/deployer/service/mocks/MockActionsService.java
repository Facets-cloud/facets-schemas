package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.BuildType;
import com.capillary.ops.deployer.bo.actions.*;
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

    @Override
    public List<ActionExecution> getLastNExecutions(String applicationId, int n, String property) {
        ApplicationAction jmap = new ApplicationAction(UUID.randomUUID().toString(), "jmap", null,
                ActionType.CUSTOM, "/commands/custom/jmap", null, null,
                BuildType.MVN, CreationStatus.FULFILLED, null);

        ActionExecution actionExecution = new ActionExecution(applicationId, jmap, TriggerStatus.SUCCESS, System.currentTimeMillis());
        actionExecution.setOutput("Dumping heap to /var/log/dumps/heapdump-1576338905.hprof ...\n" +
                "Heap dump file created");

        ApplicationAction psaux = new ApplicationAction(UUID.randomUUID().toString(), "psaux", null,
                ActionType.CUSTOM, "/commands/custom/psaux", null, null,
                BuildType.MVN, CreationStatus.FULFILLED, null);

        ActionExecution actionExecution2 = new ActionExecution(applicationId, psaux, TriggerStatus.SUCCESS, System.currentTimeMillis());
        actionExecution2.setOutput("USER       PID %CPU %MEM    VSZ   RSS TTY      STAT START   TIME COMMAND\n" +
                                    "root         1  0.0  0.0   4276   604 pts/0    Ss+  15:36   0:00 sh -c java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /hello-docker-app.jar /bin/bash\n" +
                                    "root         6  1.3 15.3 4045292 313220 pts/0  Sl+  15:36   0:34 java -Djava.security.egd=file:/dev/./urandom -jar /hello-docker-app.jar\n" +
                                    "root        42  0.0  0.1  20028  3740 pts/1    Ss   15:37   0:00 /bin/bash\n" +
                                    "root       697  0.0  0.1  38380  3072 pts/1    R+   16:20   0:00 ps aux");

        return Lists.newArrayList(actionExecution, actionExecution2);
    }
}
