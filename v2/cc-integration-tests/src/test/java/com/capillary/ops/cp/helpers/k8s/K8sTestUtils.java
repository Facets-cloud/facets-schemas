package com.capillary.ops.cp.helpers.k8s;

import com.capillary.ops.cp.bo.K8sConfig;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class K8sTestUtils {

    @Value("${cloud.provider}")
    private String CLOUD_PROVIDER;

    @Autowired
    private K8sHelperFactory k8sHelperFactory;

    private KubernetesClient getKubernetesClient() throws Exception {
        CLOUD_PROVIDER = System.getenv("CLOUD_PROVIDER");
        K8sConfig k8sConfig = k8sHelperFactory.getK8sConfig("CLOUD_PROVIDER");
        return new DefaultKubernetesClient(
                new ConfigBuilder()
                        .withMasterUrl(k8sConfig.getKubernetesApiEndpoint())
                        .withOauthToken(k8sConfig.getKubernetesToken())
                        .withTrustCerts(true)
                        .build());
    }

    public Map<String, Quantity> getK8sPodSize(String podName) throws Exception {
        Pod pod = getKubernetesClient().pods().withName(podName).get();
        return pod.getSpec().getContainers().get(0).getResources().getLimits();
    }

}
