package com.capillary.ops.cp.testSuite.common;

import com.capillary.ops.cp.App;
import com.capillary.ops.cp.bo.PodSize;
import com.capillary.ops.cp.helpers.CommonUtils;
import com.capillary.ops.cp.helpers.ConfigManager;
import com.capillary.ops.cp.helpers.StackTestUtils;
import com.capillary.ops.cp.helpers.k8s.K8sTestUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.fabric8.kubernetes.api.model.Pod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Ignore
@TestPropertySource(locations = "classpath:test.properties")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {App.class})
@SpringBootTest
public class ApplicationModuleTests {


    @Autowired
    ConfigManager configManager;

    @Autowired
    CommonUtils commonUtils;

    @Autowired
    StackTestUtils stackTestUtils;

    @Autowired
    K8sTestUtils k8sTestUtils;

    private String moduleName = "application";

    private String appName = "";

    private Pod applicationPod = null;

    @Before
    public void init() throws Exception {
        appName = configManager
                .getTestConfiguration()
                .get("primaryAppName")
                .getAsString();
        applicationPod = k8sTestUtils.getK8sPod(appName);
    }

    @Test
    public void checkStackVarsExistsInPod() throws Exception {
        HashMap<String, String> stackEnvs = stackTestUtils.getStackVars();
        List<String> podEnvs = applicationPod
                .getSpec()
                .getContainers()
                .get(0)
                .getEnv()
                .stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());

        Assert.assertTrue(podEnvs.containsAll(stackEnvs.keySet()));
    }

    @Test
    public void checkClusterVarsInPod() throws Exception {
        Set<String> clusterEnvs = stackTestUtils.getClusterVars();
        List<String> podEnvs = applicationPod
                .getSpec()
                .getContainers()
                .get(0)
                .getEnv()
                .stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());

        Assert.assertTrue(podEnvs.containsAll(clusterEnvs));
    }

    @Test
    public void checkCredentialRequestVarsInPod() throws Exception {
        Set<String> envVars = stackTestUtils.getEnvsFromCredential(moduleName, appName);
        List<String> podEnvs = applicationPod
                .getSpec()
                .getContainers()
                .get(0)
                .getEnv()
                .stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());
        Assert.assertTrue(podEnvs.containsAll(envVars));
    }

    @Test
    public void staticAndDynamicEnvVarsInPod() throws Exception {
        HashMap<String, String> instanceEnvs = stackTestUtils.getInstanceEnvVariables(moduleName, appName);
        List<String> podEnvs = applicationPod
                .getSpec()
                .getContainers()
                .get(0)
                .getEnv()
                .stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());

        for (Map.Entry env : instanceEnvs.entrySet()) {
            Assert.assertTrue("Checking for env variable " + env.getKey(), podEnvs.contains(env.getKey()));
        }
    }

    @Test
    public void sizingTest() throws Exception {
        PodSize k8sPodSize = k8sTestUtils.getK8sPodSize(appName);
        PodSize stackPodSize = stackTestUtils.getInstanceSizing(moduleName, appName);
        assert (k8sPodSize.getCpu().equals(stackPodSize.getCpu()));
        assert (k8sPodSize.getMemory().equals(stackPodSize.getMemory()));
    }

    @Test
    public void sidecarTest() throws Exception {
        JsonArray sidecarJson = stackTestUtils.getInstance(moduleName, appName).getAsJsonArray("sidecarContainers");
        int containerCount = 1;
        if (sidecarJson != null && sidecarJson.size() > 1) {
            containerCount = 2;
        }
        Assert.assertEquals(applicationPod.getSpec().getContainers().size(), containerCount);
    }

    @Test
    public void ingresRuleTest() throws Exception {
        JsonArray stackIngressRules = stackTestUtils.getInstance(moduleName, appName).getAsJsonArray("ingress_rules");
        int k8sIngressRules = k8sTestUtils.getK8sIngressRules(appName).size();
        Assert.assertEquals(stackIngressRules.size(), k8sIngressRules);
    }

    @Test
    public void ondemandAppTest() throws Exception {
        JsonObject nodeLifecycle = stackTestUtils.getInstance(moduleName, appName).getAsJsonObject("nodeLifecycle");
        if (nodeLifecycle != null && nodeLifecycle.equals("ondemand")) {
            Assert.assertEquals("ondemand-applications", applicationPod.getSpec().getPriorityClassName());
            Assert.assertEquals("ondemand", applicationPod.getSpec().getNodeSelector().get("kubernetes.io/lifecycle"));
        } else {
            Assert.assertEquals("default", applicationPod.getSpec().getPriorityClassName());
            Assert.assertNull(applicationPod.getSpec().getNodeSelector());
        }
    }

    @Test
    public void checkPodAnnotations() throws Exception {
        Map<String, String> podAnnotations = applicationPod.getMetadata().getAnnotations();
        JsonObject instance = stackTestUtils.getInstance(moduleName, appName);

        if (instance.get("disablePodEviction") != null && instance.get("disablePodEviction").getAsBoolean()) {
            Assert.assertEquals(podAnnotations.get("cluster-autoscaler.kubernetes.io/safe-to-evict"), "never");
        } else {
            Assert.assertEquals(podAnnotations.get("cluster-autoscaler.kubernetes.io/safe-to-evict"), "false");
        }
        Assert.assertTrue(podAnnotations.get("iam.amazonaws.com/role").contains(appName));
        Assert.assertEquals(podAnnotations.get("sidecar.istio.io/inject"), "false");
    }

}
