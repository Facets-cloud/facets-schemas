package com.capillary.ops.cp.helpers.k8s;

import com.capillary.ops.cp.bo.K8sConfig;
import org.springframework.stereotype.Service;

@Service
public class LocalK8sHelper implements K8sHelper {

    @Override
    public K8sConfig getK8sConfig() {

        //TODO: fabric8 client from kubeconfig
        return null;
    }
}