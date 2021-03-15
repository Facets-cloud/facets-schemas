package com.capillary.ops.cp.helpers.k8s;

import com.capillary.ops.cp.bo.K8sConfig;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class K8sTestUtils {

    private String CLOUD_PROVIDER;
    @Autowired
    private K8sHelperFactory k8sHelperFactory;

    private KubernetesClient getKubernetesClient() {
        CLOUD_PROVIDER = System.getenv("cloud_provider");
        K8sConfig k8sConfig = k8sHelperFactory.getK8sConfig("CLOUD_PROVIDER");
        return new DefaultKubernetesClient(
                new ConfigBuilder()
                        .withMasterUrl(k8sConfig.getKubernetesApiEndpoint())
                        .withOauthToken(k8sConfig.getKubernetesToken())
                        .withTrustCerts(true)
                        .build());
    }

    public HashMap<String, String> getK8sPodSize(String podName) {
        return null;
    }

}
