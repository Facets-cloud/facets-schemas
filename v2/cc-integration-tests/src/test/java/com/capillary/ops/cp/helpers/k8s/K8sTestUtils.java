package com.capillary.ops.cp.helpers.k8s;

import com.capillary.ops.cp.bo.CpuSize;
import com.capillary.ops.cp.bo.CpuUnits;
import com.capillary.ops.cp.bo.K8sConfig;
import com.capillary.ops.cp.bo.MemorySize;
import com.capillary.ops.cp.bo.MemoryUnits;
import com.capillary.ops.cp.bo.PodSize;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.HorizontalPodAutoscaler;
import io.fabric8.kubernetes.api.model.HorizontalPodAutoscalerSpec;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Probe;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceAccount;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.api.model.rbac.ClusterRole;
import io.fabric8.kubernetes.api.model.rbac.ClusterRoleBinding;
import io.fabric8.kubernetes.api.model.rbac.Role;
import io.fabric8.kubernetes.api.model.rbac.RoleBinding;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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

    private KubernetesClient getKubernetesClientHandle() {
        Optional<KubernetesClient> kubernetesClient;
        try {
            kubernetesClient = Optional.of(getKubernetesClient());
        } catch (Exception e) {
            kubernetesClient = Optional.empty();
        }

        return kubernetesClient.orElseThrow(() -> new RuntimeException("Failed to get kubernetes client"));
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

    public PodSize getK8sPodSizeWithUnits(String podName) throws Exception {
        ResourceRequirements resources = getK8sPod(podName)
                .getSpec()
                .getContainers()
                .get(0)
                .getResources();

        CpuSize cpu = getK8sCpuSize(resources.getLimits().get("cpu").getAmount());
        MemorySize memory = getK8sMemorySize(resources.getLimits().get("memory").getAmount());

        return new PodSize(cpu, memory);
    }

    public CpuSize getK8sCpuSize(String k8sCpuString) {
        if (k8sCpuString.contains("m")) {
            return new CpuSize(Double.parseDouble(k8sCpuString.replace("m", "")), CpuUnits.MilliCores);
        }
        return new CpuSize(Double.parseDouble(k8sCpuString), CpuUnits.Cores);
    }

    public MemorySize getK8sMemorySize(String k8sMemoryString) {
        if (k8sMemoryString.contains("Mi")) {
            return new MemorySize(Double.parseDouble(k8sMemoryString.replace("Mi", "")), MemoryUnits.MegaBytes);
        }
        if (k8sMemoryString.contains("Gi")) {
            return new MemorySize(Double.parseDouble(k8sMemoryString.replace("Gi", "")), MemoryUnits.GigaBytes);
        }
        return new MemorySize(Double.parseDouble(k8sMemoryString), MemoryUnits.GigaBytes);
    }

    public List<Ingress> getK8sIngressRules(String appName) throws Exception {
        return getKubernetesClient().extensions().ingresses()
                .inNamespace("default")
                .list().getItems()
                .stream()
                .filter(p -> p.getMetadata().getName().contains(appName))
                .filter(p -> p.getSpec().getRules().get(0).getHost().contains("cc.capillarytech.com"))
                .filter(p -> p.getSpec().getRules().get(0).getHttp().getPaths().get(0).getBackend().getServiceName().equals(appName))
                .collect(Collectors.toList());
    }

    public Optional<Service> getK8sService(String name) throws Exception{
        return getKubernetesClient().services()
                .list().getItems()
                .stream()
                .filter(p -> p.getMetadata().getName().contains(name))
                .findFirst();
    }

    public Map<String, StatefulSet> getK8sStatefulSets(Set<String> statefulSets, String namespace) {
        Map<String, StatefulSet> statefulSetInstances = new HashMap<>();

        statefulSets.forEach(name -> {
            try {
                statefulSetInstances.put(name, getKubernetesClient().apps().statefulSets().inNamespace(namespace).withName(name).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return statefulSetInstances;
    }

    public Map<String, CronJob> getK8sCronJobs(Set<String> cronJobs, String namespace) {
        Map<String, CronJob> cronJobInstances = new HashMap<>();

        cronJobs.forEach(name -> {
            try {
                cronJobInstances.put(name, getKubernetesClient().batch().cronjobs().inNamespace(namespace).withName(name).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return cronJobInstances;
    }

    public Probe getK8sLivenessProbe(String appName) throws Exception {
        return getKubernetesClient().apps().deployments().inNamespace(K8S_NAMESPACE).withName(appName).get().getSpec().getTemplate().getSpec().getContainers().get(0).getLivenessProbe();
    }

    public Probe getK8sReadinessProbe(String appName) throws Exception {
        return getKubernetesClient().apps().deployments().inNamespace(K8S_NAMESPACE).withName(appName).get().getSpec().getTemplate().getSpec().getContainers().get(0).getReadinessProbe();
    }

    public HorizontalPodAutoscalerSpec getK8sHPA(String appName) throws Exception {
        HorizontalPodAutoscaler horizontalPodAutoscaler = getKubernetesClient().autoscaling().horizontalPodAutoscalers().inNamespace(K8S_NAMESPACE).withName(appName).get();
        return horizontalPodAutoscaler.getSpec();
    }

    public Optional<Ingress> getK8sIngress(String ingressName) throws Exception {
        return Optional.ofNullable(getKubernetesClient().extensions().ingresses().inNamespace(K8S_NAMESPACE).withName(ingressName).get());
    }

    public String getK8sMajorVersion() throws Exception {
        return getKubernetesClient().getVersion().getMajor();
    }

    public String getK8sMinorVersion() throws Exception {
        return getKubernetesClient().getVersion().getMinor();
    }

    public Optional<Namespace> getClusterNameSpace(String namespace) throws Exception {
        return Optional.ofNullable(getKubernetesClient().namespaces().withName(namespace).get());
    }

    public Optional<Role> getRole(String namespace, String roleName) throws Exception {
        return Optional.ofNullable(getKubernetesClient().rbac().roles().inNamespace(namespace).withName(roleName).get());
    }

    public Optional<ServiceAccount> getServiceAccount(String namespace, String serviceAccountName) {
        return Optional.ofNullable(getKubernetesClientHandle().serviceAccounts().inNamespace(namespace).withName(serviceAccountName).get());
    }

    public Optional<RoleBinding> getRoleBinding(String namespace, String roleBindingName) throws Exception {
        return Optional.ofNullable(getKubernetesClient().rbac().roleBindings().inNamespace(namespace).withName(roleBindingName).get());
    }

    public Optional<ClusterRoleBinding> getClusterRoleBinding(String namespace, String clusterRoleBindingName) {
        return Optional.ofNullable(getKubernetesClientHandle().rbac().clusterRoleBindings().inNamespace(namespace).withName(clusterRoleBindingName).get());
    }

    public Optional<ClusterRole> getClusterRole(String namespace, String clusterRoleName) {
        return Optional.ofNullable(getKubernetesClientHandle().rbac().clusterRoles().inNamespace(namespace).withName(clusterRoleName).get());
    }

    public Optional<Secret> getSecret(String namespace, String secretName) {
        return Optional.ofNullable(getKubernetesClientHandle().secrets().inNamespace(namespace).withName(secretName).get());
    }

    public Optional<CronJob> getCronJob(String namespace, String cronJobName) {
        return Optional.ofNullable(getKubernetesClientHandle().batch().cronjobs().inNamespace(namespace).withName(cronJobName).get());
    }

    public Optional<Map<String, String>> getNodeSelector(String deploymentName, String namespace) {
        return Optional.ofNullable(getKubernetesClientHandle().apps().deployments().inNamespace(namespace).withName(deploymentName).get().getSpec().getTemplate().getSpec().getNodeSelector());
    }
}
