package com.capillary.ops.cp.helpers.k8s;

import com.capillary.ops.cp.bo.K8sConfig;
import com.capillary.ops.cp.helpers.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;

public class EksHelper implements K8sHelper {

    @Value("${stack.name}")
    private String STACK_NAME;

    @Value("${cluster.id}")
    private String CLUSTER_ID;

    @Autowired
    CommonUtils commonUtils;

    @Override
    public K8sConfig getK8sConfig() throws Exception {
        HashMap<String, String> deploymentContextJson =  commonUtils.getDeploymentContext();
        //deploymentContextJson
        return null;
    }
}
