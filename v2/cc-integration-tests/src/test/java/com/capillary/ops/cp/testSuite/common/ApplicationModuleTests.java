package com.capillary.ops.cp.testSuite.common;

import com.capillary.ops.cp.App;
import com.capillary.ops.cp.bo.PodSize;
import com.capillary.ops.cp.helpers.CommonUtils;
import com.capillary.ops.cp.helpers.StackTestUtils;
import com.capillary.ops.cp.helpers.k8s.K8sTestUtils;
import com.google.gson.JsonObject;
import com.mongodb.util.JSON;
import io.fabric8.kubernetes.api.model.Pod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@TestPropertySource(locations="classpath:test.properties")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {App.class})
@SpringBootTest
public class ApplicationModuleTests {

    private String appName = "intouch-api";
    private String moduleName = "application";

    @Autowired
    CommonUtils commonUtils;

    @Autowired
    StackTestUtils stackTestUtils;

    @Autowired
    K8sTestUtils k8sTestUtils;

    @Test
    public void environmentVarsTests() throws Exception{
        Integer totalEnvsByDef = stackTestUtils.getStackVars().size() +
        stackTestUtils.getClusterVars().size() +
        stackTestUtils.getInstanceEnvVariables(moduleName,appName).size();

        Integer totalEnvsFromK8sPod = k8sTestUtils
                .getK8sPod(appName)
                .getSpec()
                .getContainers()
                .get(0)
                .getEnv()
                .size();
        assert(totalEnvsByDef.equals(totalEnvsFromK8sPod));
    }

    @Test
    public void sizingTest() throws Exception{
        PodSize k8sPodSize =  k8sTestUtils.getK8sPodSize(appName);
        JsonObject stackPodSize = stackTestUtils.getInstanceSizing(moduleName,appName);
        assert(k8sPodSize.getCpu().equals(stackPodSize.get("podCPULimit")));
        assert(k8sPodSize.getMemory().equals(stackPodSize.get("podMemoryLimit")));
    }

    @Test
    public void sidecarTest() throws Exception{
        JsonObject sidecarJson = stackTestUtils.getInstance(moduleName,appName).getAsJsonObject("sidecarContainers");
        assert(k8sTestUtils.getK8sPod(appName));

    }

    @Test
    public void cloudflareTest() throws Exception{}

    @Test
    public void ingresRuleTest() throws Exception{}

    @Test
    public void prodQaTest() throws Exception{}

    @Test
    public void annotations() throws Exception{}

    @Test
    public void k8sServiceTest() throws Exception{}

    @Test
    public void mountsTest() throws Exception{}
}
