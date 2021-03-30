package com.capillary.ops.cp.testSuite.common;

import com.capillary.ops.cp.App;
import com.capillary.ops.cp.bo.PodSize;
import com.capillary.ops.cp.helpers.CommonUtils;
import com.capillary.ops.cp.helpers.ConfigManager;
import com.capillary.ops.cp.helpers.StackTestUtils;
import com.capillary.ops.cp.helpers.k8s.K8sTestUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;
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

import javax.validation.constraints.AssertTrue;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public void environmentVarsTests() throws Exception {
        Integer totalEnvsByDef = stackTestUtils.getStackVars().size() +
                stackTestUtils.getClusterVars().size() +
                stackTestUtils.getInstanceEnvVariables(moduleName, appName).size();

        Integer totalEnvsFromK8sPod = applicationPod
                .getSpec()
                .getContainers()
                .get(0)
                .getEnv()
                .size();
        assert (totalEnvsByDef.equals(totalEnvsFromK8sPod));
    }

    @Test
    public void sizingTest() throws Exception {
        PodSize k8sPodSize = k8sTestUtils.getK8sPodSize(appName);
        JsonObject stackPodSize = stackTestUtils.getInstanceSizing(moduleName, appName);
        assert (k8sPodSize.getCpu().equals(stackPodSize.get("podCPULimit")));
        assert (k8sPodSize.getMemory().equals(stackPodSize.get("podMemoryLimit")));
    }

    @Test
    public void sidecarTest() throws Exception {
        JsonObject sidecarJson = stackTestUtils.getInstance(moduleName, appName).getAsJsonObject("sidecarContainers");
        int containerCount = 1;
        if (sidecarJson.size() > 1) {
            containerCount = 2;
        }
        Assert.assertEquals(applicationPod.getSpec().getContainers().size(), containerCount);
    }

    @Test
    public void ingresRuleTest() throws Exception {
        JsonObject stackIngressRules = stackTestUtils.getInstance(moduleName, appName).getAsJsonObject("ingress_rules");
        int k8sIngressRules = k8sTestUtils.getK8sIngressRules(appName).size();
        Assert.assertEquals(stackIngressRules.size(),k8sIngressRules);
    }

    @Test
    public void ondemandAppTest() throws Exception {
        JsonObject nodeLifecycle = stackTestUtils.getInstance(moduleName, appName).getAsJsonObject("nodeLifecycle");
        if (nodeLifecycle != null && nodeLifecycle.equals("ondemand")) {
            Assert.assertEquals("ondemand-applications",applicationPod.getSpec().getPriorityClassName());
            Assert.assertEquals("ondemand",applicationPod.getSpec().getNodeSelector().get("kubernetes.io/lifecycle"));
        }else {
            Assert.assertEquals("default",applicationPod.getSpec().getPriorityClassName());
            Assert.assertNull(applicationPod.getSpec().getNodeSelector());
        }
    }

    @Test
    public void annotations() throws Exception {
        Map<String,String> podAnnotations = applicationPod.getMetadata().getAnnotations();
        JsonObject instance = stackTestUtils.getInstance(moduleName, appName);

        if(instance.get("disablePodEviction") != null && instance.get("disablePodEviction").getAsBoolean()){
            Assert.assertEquals(podAnnotations.get("cluster-autoscaler.kubernetes.io/safe-to-evict"),"never");
        }else {
            Assert.assertEquals(podAnnotations.get("cluster-autoscaler.kubernetes.io/safe-to-evict"),"false");
        }

        Assert.assertTrue(podAnnotations.get("iam.amazonaws.com/role").contains(appName));
        Assert.assertEquals(podAnnotations.get("sidecar.istio.io/inject"),"false");
    }

    @Ignore
    @Test
    public void k8sServiceTest() throws Exception {
        JsonArray k8s_service_names = stackTestUtils.getInstance(moduleName,appName).getAsJsonArray("k8s_service_names");
        if(k8s_service_names != null && k8s_service_names.size() > 0){ }
    }
}
