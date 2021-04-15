package com.capillary.ops.cp.helpers.k8s;

import com.capillary.ops.cp.bo.K8sConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class K8sHelperFactory {

    @Autowired
    private EksHelper eksHelper;

    @Autowired
    private LocalK8sHelper localK8sHelper;

    public K8sConfig getK8sConfig(String cloudProvider) throws Exception {
        switch (cloudProvider) {
            case "tfaws":
                return eksHelper.getK8sConfig();
            case "localcluster":
                return localK8sHelper.getK8sConfig();
            default:
                return localK8sHelper.getK8sConfig();
        }
    }

}
