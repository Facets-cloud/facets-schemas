package com.capillary.ops.cp.helpers.k8s;

import com.capillary.ops.cp.bo.K8sConfig;

public class localK8sHelper implements K8sHelper {

    @Override
    public K8sConfig getK8sConfig() {

        //SET KUBERNETES_KUBECONFIG_FILE
        return null;
    }
}