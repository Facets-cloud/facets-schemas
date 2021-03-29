package com.capillary.ops.cp.helpers.k8s;

import com.capillary.ops.cp.bo.K8sConfig;
import com.capillary.ops.cp.bo.PodSize;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

@Component
@TestPropertySource(locations = "classpath:test.properties")
public class K8sTestUtils {

    private String K8S_NAMESPACE = "default";

    @Value("${cloud.provider}")
    private String CLOUD_PROVIDER;

    @Autowired
    private K8sHelperFactory k8sHelperFactory;

    private KubernetesClient getKubernetesClient() throws Exception {

        K8sConfig k8sConfig = k8sHelperFactory.getK8sConfig(CLOUD_PROVIDER);
        return new DefaultKubernetesClient(
                new ConfigBuilder()
                        .withMasterUrl(k8sConfig.getKubernetesApiEndpoint())
                        .withOauthToken(k8sConfig.getKubernetesToken())
                        .withTrustCerts(true)
                        .build());
    }

    public Pod getK8sPod(String podName) throws Exception {
        Pod pod = getKubernetesClient().pods().inNamespace(K8S_NAMESPACE)
                .withName(podName)
                .get();
        if (pod == null) {
            return getKubernetesClient().pods().inNamespace(K8S_NAMESPACE)
                    .list().getItems()
                    .stream()
                    .filter(p -> p.getMetadata().getName().contains(podName))
                    .findFirst()
                    .get();
        }
        return pod;
    }

    public PodSize getK8sPodSize(String podName) throws Exception {
        ResourceRequirements resources = getK8sPod(podName)
                .getSpec()
                .getContainers()
                .get(0)
                .getResources();

        Double cpu = getK8sCpuInCores(resources.getLimits().get("cpu").getAmount());
        Double memory = getK8sMemoryInGi(resources.getLimits().get("memory").getAmount());
        return new PodSize(cpu, memory);
    }

    public Double getK8sCpuInCores(String k8sCpuString) {
        if (k8sCpuString.contains("m")) {
            String cpuTemp = k8sCpuString.replace("m", "");
            return Double.parseDouble(cpuTemp) / 1000;
        }
        return Double.parseDouble(k8sCpuString);
    }

    public Double getK8sMemoryInGi(String k8sMemoryString) {
        if (k8sMemoryString.contains("Mi")) {
            String memoryTemp = k8sMemoryString.replace("Mi", "");
            return Double.parseDouble(memoryTemp) / 1000;
        }
        if (k8sMemoryString.contains("Gi")) {
            String memoryTemp = k8sMemoryString.replace("Gi", "");
            return Double.parseDouble(memoryTemp);
        }
        return Double.parseDouble(k8sMemoryString);
    }

}
