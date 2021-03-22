package com.capillary.ops.cp.testSuite.common;

import com.capillary.ops.cp.App;
import com.capillary.ops.cp.bo.K8sConfig;
import com.capillary.ops.cp.helpers.StackTestUtils;
import com.capillary.ops.cp.helpers.k8s.K8sHelperFactory;
import com.capillary.ops.cp.helpers.k8s.K8sTestUtils;
import io.fabric8.kubernetes.api.model.Quantity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ObjLongConsumer;

@TestPropertySource(locations="classpath:test.properties")
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

    @Value("${cluster.id}")
    private String CLUSTER_ID;

    @Test
    public void verifyPodSize() throws Exception {
        HashMap<String,String> stackSizing = stackTestUtils.getInstanceSizing("solr","intouch");
        Map<String, Quantity> k8sPodSizing = k8sTestUtils.getK8sPodSize("solr-intouch-0");

        Object num = stackSizing.get("podCPULimit");
        Integer stackSizingValue = ((Double) num).intValue();
        Integer k8sPodSizingValue = Integer.parseInt(k8sPodSizing.get("cpu").getAmount());

        assert(stackSizingValue.equals(k8sPodSizingValue));
    }
}
