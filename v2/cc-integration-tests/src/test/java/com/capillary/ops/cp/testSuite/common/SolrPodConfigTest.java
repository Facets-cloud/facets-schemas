package com.capillary.ops.cp.testSuite.common;

import com.capillary.ops.cp.helpers.StackTestUtils;
import com.capillary.ops.cp.helpers.k8s.K8sTestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class SolrPodConfigTest {

    @Autowired
    private StackTestUtils stackTestUtils;

    @Autowired
    private K8sTestUtils k8sTestUtils;

    @Test
    public void verifyPodSize() throws Exception {
        HashMap<String,String> stackSizing = stackTestUtils.getInstanceSizing("solr","intouch");
        HashMap<String,String> k8sPodSizing = k8sTestUtils.getK8sPodSize("solr-intouch-0");
    }
}
