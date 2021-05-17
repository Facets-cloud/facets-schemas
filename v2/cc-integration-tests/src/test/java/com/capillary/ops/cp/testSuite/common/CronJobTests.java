package com.capillary.ops.cp.testSuite.common;

import com.capillary.ops.cp.App;
import com.capillary.ops.cp.bo.PodSize;
import com.capillary.ops.cp.helpers.StackTestUtils;
import com.capillary.ops.cp.helpers.k8s.K8sTestUtils;
import com.google.gson.JsonObject;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@TestPropertySource(locations = "classpath:test.properties")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {App.class})
@SpringBootTest
public class CronJobTests {

    private static final Logger logger = LoggerFactory.getLogger(CronJobTests.class);

    @Autowired
    private K8sTestUtils k8sTestUtils;

    @Autowired
    private StackTestUtils stackTestUtils;

    private static Map<String, JsonObject> stackCronJobInstances = new HashMap<>();

    private static Map<String, CronJob> k8sStackCronJobs = new HashMap<>();

    private static final String CRONJOB = "cronjob";

    private static final String CRONJOB_NAMESPACE = "default";

    private static final String cpuUnits = "";

    private static final String memoryUnits = "Gi";
    
    @Before
    public void populateInstances() {
        if (stackCronJobInstances.size() == 0) {
            stackCronJobInstances = stackTestUtils.sampleInstances(CRONJOB);
            k8sStackCronJobs = k8sTestUtils.getK8sCronJobs(stackCronJobInstances.keySet(), CRONJOB_NAMESPACE);
        }
    }

    @Test
    public void verifyCronSize() {
        k8sStackCronJobs.forEach((cronJob, cronJobInstance) -> {
            logger.info("sampled {} from cron jobs. ", cronJob);
            try {
                Map<String, Quantity> limits = cronJobInstance.getSpec().getJobTemplate().getSpec().getTemplate().getSpec().getContainers().get(0).getResources().getLimits();
                PodSize k8sCronJobSize = new PodSize(k8sTestUtils.getK8sCpuSize(limits.get("cpu").getAmount()), k8sTestUtils.getK8sMemorySize(limits.get("memory").getAmount()));
                PodSize instanceSizing = stackTestUtils.getInstanceSizingWithUnits(CRONJOB, stackCronJobInstances.get(cronJob), cpuUnits, memoryUnits);

                Assert.assertEquals("Cronjob size should match with the type mentioned in the instance in terms of the sizing defined for the cronjob instances. ", k8sCronJobSize, instanceSizing);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void verifyCronPattern() {
        k8sStackCronJobs.forEach((cronJob, cronJobInstance) -> {
            logger.info("sampled {} from cron jobs. ", cronJob);
            try {
                String cronJobPattern = cronJobInstance.getSpec().getSchedule();
                String instanceSchedule = stackCronJobInstances.get(cronJob).get("schedule").getAsString();

                Assert.assertEquals("CronJob Schedule mentioned in the instance should match with the cron pattern of kubernetes cronjob resource.", cronJobPattern, instanceSchedule);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void verifyCredentialRequests() {
        k8sStackCronJobs.forEach((cronJob, cronJobInstance) -> {
            logger.info("sampled {} from cron jobs. ", cronJob);
            try {
                Set<String> instanceCredRequests = stackTestUtils.getEnvsFromCredential(CRONJOB, cronJob);
                Set<String> cronJobEnvironmentVariables = cronJobInstance.getSpec().getJobTemplate().getSpec().getTemplate().getSpec().getContainers().get(0).getEnv().stream().map(EnvVar::getName).collect(Collectors.toSet());

                Assert.assertTrue("All MySQL, Mongo and RabbitMQ credentials in the cron job instance should be found in the kubernetes cron job object environment variables", cronJobEnvironmentVariables.containsAll(instanceCredRequests));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void verifyEnvVariables() {
        k8sStackCronJobs.forEach((cronJob, cronJobInstance) -> {
            logger.info("sampled {} from cron jobs. ", cronJob);
            try {
                HashMap<String, String> instanceEnvVariables = stackTestUtils.getInstanceEnvVariables(CRONJOB, cronJob);
                Set<String> cronJobEnvVariables = cronJobInstance.getSpec().getJobTemplate().getSpec().getTemplate().getSpec().getContainers().get(0).getEnv().stream().map(EnvVar::getName).collect(Collectors.toSet());

                Assert.assertTrue("All static and dynamic environment variables in the cron job instance should be found in the kubernetes cronjob object environment variables", cronJobEnvVariables.containsAll(instanceEnvVariables.keySet()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
