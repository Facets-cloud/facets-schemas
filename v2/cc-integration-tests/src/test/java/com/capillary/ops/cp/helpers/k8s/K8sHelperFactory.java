package com.capillary.ops.cp.helpers.k8s;

import com.capillary.ops.cp.bo.K8sConfig;
import org.springframework.beans.factory.annotation.Autowired;

public class K8sHelperFactory {

    @Autowired
    private EksHelper eksHelper;

    @Autowired
    private localK8sHelper localK8sHelper;

    public K8sConfig getK8sConfig(String cloudProvider) {
        switch (cloudProvider) {
            case "AWS":
                return eksHelper.getK8sConfig();
            case "localcluster":
                return localK8sHelper.getK8sConfig();
            default:
                return localK8sHelper.getK8sConfig();
        }
    }

}
