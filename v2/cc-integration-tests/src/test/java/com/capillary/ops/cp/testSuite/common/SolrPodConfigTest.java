package com.capillary.ops.cp.testSuite.common;

import com.capillary.ops.cp.App;
import com.capillary.ops.cp.bo.PodSize;
import com.capillary.ops.cp.helpers.StackTestUtils;
import com.capillary.ops.cp.helpers.k8s.K8sTestUtils;
import com.google.gson.JsonObject;
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

    @Value("${stack.name}")
    private String STACK_NAME;

    @Test
    public void verifyPodSize() throws Exception {
        PodSize k8sPodSize = k8sTestUtils.getK8sPodSize("solr-intouch-0");

        JsonObject stackSizing = stackTestUtils.getInstanceSizing("solr", "intouch");

        String stackSizingCpu = stackSizing.get("podCPULimit").getAsString();
        String stackSizingMemory = stackSizing.get("podCPULimit").getAsString();

        assert (k8sPodSize.getCpu().equals(Double.parseDouble(stackSizingCpu)));
        assert (k8sPodSize.getMemory().equals(Double.parseDouble(stackSizingMemory)));
    }
}
