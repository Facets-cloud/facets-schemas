package com.capillary.ops.cp.testSuite.common;

import com.capillary.ops.cp.App;
import com.capillary.ops.cp.bo.PodSize;
import com.capillary.ops.cp.helpers.StackTestUtils;
import com.capillary.ops.cp.helpers.k8s.K8sTestUtils;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@TestPropertySource(locations = "classpath:test.properties")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {App.class})
@SpringBootTest
public class SolrPodConfigTest {

    @Autowired
    private StackTestUtils stackTestUtils;

    @Autowired
    private K8sTestUtils k8sTestUtils;

    private static final String cpuUnits = "";

    private static final String memoryUnits = "Mi";

    @Value("${stack.name}")
    private String STACK_NAME;

    @Test
    public void verifyPodSize() throws Exception {
        Assume.assumeTrue(!STACK_NAME.equals("cc-infra-testing"));
        PodSize k8sPodSize = k8sTestUtils.getK8sPodSize("solr-intouch-0");

        PodSize stackSizing = stackTestUtils.getInstanceSizing("solr", "intouch", cpuUnits, memoryUnits);

        Assert.assertEquals(k8sPodSize.getCpu(), stackSizing.getCpu());
        Assert.assertEquals(k8sPodSize.getMemory(), stackSizing.getMemory());
    }
}
