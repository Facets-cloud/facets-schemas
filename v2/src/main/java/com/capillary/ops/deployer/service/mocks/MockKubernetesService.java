package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.service.interfaces.IKubernetesService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
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
                        new PodReplicationDetails(2, 2, 0, 0, 0), new HashMap<>(),
                        hpaDetails,
                        "2019-06-07T06:46:21Z"),
                Arrays.asList(pod1, pod2)
        );

        return deploymentStatusDetails;
    }

    @Override
    public List<ApplicationPodDetails> getApplicationPodDetails(Application application, Environment environment, String deploymentName) {
        ApplicationPodDetails podDetails = new ApplicationPodDetails();
        podDetails.setName("testpod");
        podDetails.setImage("image-uri");
        podDetails.setLabels(ImmutableMap.of("foo", "bar"));
        podDetails.setPodStatus("Ready");
        podDetails.setReady(false);
        podDetails.setRestartReason("Error");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = sdf.format(date);
        podDetails.setCreationTimestamp(time);

        try {
            Thread.sleep(1 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ApplicationPodDetails podDetails2 = new ApplicationPodDetails();
        podDetails2.setName("testpod2");
        podDetails2.setImage("image-uri");
        podDetails2.setLabels(ImmutableMap.of("foo", "bar"));
        podDetails2.setPodStatus("Ready");
        podDetails2.setReady(true);
        Date date2 = new Date();
        String time2 = sdf.format(date2);
        podDetails2.setCreationTimestamp(time2);

        return Lists.newArrayList(podDetails, podDetails2);
    }

    @Override
    public void haltApplication(String deploymentName, Environment environment) {

    }

    @Override
    public void resumeApplication(String deploymentName, Environment environment) {

    }
}
