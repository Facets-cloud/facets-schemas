package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.service.interfaces.IKubernetesService;
import com.google.common.collect.ImmutableMap;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

@Profile("dev")
@Service
public class MockKubernetesService implements IKubernetesService {
    @Override
    public DeploymentList getDeployments(Environment environment, String namespace) {
        return null;
    }

    @Override
    public DeploymentList getDeployments(Environment environment) {
        return null;
    }

    @Override
    public Secret getSecretWithName(Environment environment, String secretName, String namespace) {
        return null;
    }

    @Override
    public Secret getSecretWithName(Environment environment, String secretName) {
        return null;
    }

    @Override
    public void createOrUpdateApplicationSecrets(Environment environment, String secretName, List<ApplicationSecret> applicationSecrets) {

    }

    @Override
    public void createOrUpdateSecret(Environment environment, String secretName, Map<String, String> secretMap) {

    }

    @Override
    public DeploymentStatusDetails getDeploymentStatus(Application application, Environment environment, String deploymentName) {
        ApplicationPodDetails pod1 =
                new ApplicationPodDetails(application.getName() + "-old", new HashMap<>(),
                        "Running", "1111111122222.dkr.ecr.us-west-1.amazonaws.com/OPS/someappname:version1", "", "2019-06-07T06:46:21Z", true, 0, "NA");
        ApplicationPodDetails pod2 =
                new ApplicationPodDetails(application.getName() + "-new", new HashMap<>(),
                        "Pending", "1111111122222.dkr.ecr.us-west-1.amazonaws.com/OPS/someappname:version2", "", "2019-06-07T10:46:21Z", true, 0, "NA");
        pod1.setResourceUsage(new PodResource("1200m", "1024M"));
        pod1.setResourceUsage(new PodResource("1100m", "1096M"));

        HPADetails hpaDetails = new HPADetails(1, 2,
                1, 1, 60, 30);

        DeploymentStatusDetails deploymentStatusDetails = new DeploymentStatusDetails(
                new ApplicationServiceDetails(),
                new ApplicationDeploymentDetails(application.getName(),
                        ImmutableMap.of("key1", "value1",
                                "key2", "value2",
                                "key3", "value3"), new ArrayList<>(),
                        new PodReplicationDetails(), new HashMap<>(),
                        hpaDetails,
                        "2019-06-07T06:46:21Z"),
                Arrays.asList(pod1, pod2)
        );

        return deploymentStatusDetails;
    }
}
