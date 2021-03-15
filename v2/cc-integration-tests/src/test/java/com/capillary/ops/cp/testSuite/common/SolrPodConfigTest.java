package com.capillary.ops.cp.testSuite.common;

import com.capillary.ops.cp.helpers.StackTestUtils;
import com.capillary.ops.cp.helpers.k8s.K8sTestUtils;
import io.fabric8.kubernetes.api.model.Quantity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

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
        assert(k8sPodSizing.get("cpu").equals(stackSizing.get("podCPULimit")));
    }
}
