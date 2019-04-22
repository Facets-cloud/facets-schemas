package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.*;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DeploymentStatus;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KubectlService {

    private static final String NAMESPACE = "default";

    public DeploymentStatusDetails getDeploymentStatus(Application application, String environmentName, String deploymentName) {
        Environment environment = application.getApplicationFamily().getEnvironment(environmentName);
        KubernetesClient kubernetesClient = getKubernetesClient(environment);

        ServiceCheckDetails serviceCheckDetails = getServiceCheckDetails(deploymentName, kubernetesClient);
        DeploymentCheckDetails deploymentCheckDetails = getDeploymentCheckDetails(deploymentName, kubernetesClient);
        List<PodCheckDetails> podCheckDetails = getPodCheckDetails(deploymentName,
                serviceCheckDetails.getSelectors(), kubernetesClient);

        return new DeploymentStatusDetails(serviceCheckDetails, deploymentCheckDetails, podCheckDetails);
    }

    private ServiceCheckDetails getServiceCheckDetails(String deploymentName, KubernetesClient kubernetesClient) {
        ServiceCheckDetails serviceCheckDetails = new ServiceCheckDetails();
        io.fabric8.kubernetes.api.model.Service service = kubernetesClient.services()
                .inNamespace(NAMESPACE)
                .withName(deploymentName)
                .get();

        ObjectMeta serviceMetadata = service.getMetadata();
        serviceCheckDetails.setCreationTimestamp(serviceMetadata.getCreationTimestamp());
        serviceCheckDetails.setLabels(serviceMetadata.getLabels());
        serviceCheckDetails.setName(serviceMetadata.getName());

        ServiceSpec serviceSpec = service.getSpec();
        serviceCheckDetails.setServiceType(ServiceCheckDetails.ServiceType.valueOf(serviceSpec.getType()));
        serviceCheckDetails.setSelectors(serviceSpec.getSelector());

        List<ServiceCheckDetails.PortMapping> internalPortList = new ArrayList<>();
        serviceSpec.getPorts().forEach(x -> internalPortList.add(
                new ServiceCheckDetails.PortMapping(
                        x.getProtocol(), deploymentName + "." + NAMESPACE + x.getPort())));
        serviceCheckDetails.setInternalEndpoints(internalPortList);

        List<ServiceCheckDetails.PortMapping> externalPortList = new ArrayList<>();
        List<LoadBalancerIngress> ingresses = service.getStatus().getLoadBalancer().getIngress();
        ingresses.forEach(x -> internalPortList.forEach(p -> externalPortList.add(new ServiceCheckDetails.PortMapping(null, x.getHostname() + ":" + p))));
        serviceCheckDetails.setExternalEndpoints(externalPortList);

        return serviceCheckDetails;
    }

    private DeploymentCheckDetails getDeploymentCheckDetails(String deploymentName, KubernetesClient kubernetesClient) {
        DeploymentCheckDetails deploymentCheckDetails = new DeploymentCheckDetails();

        Deployment deployment = kubernetesClient.extensions()
                .deployments().
                inNamespace(NAMESPACE)
                .withName(deploymentName)
                .get();

        ObjectMeta deploymentMetadata = deployment.getMetadata();
        deploymentCheckDetails.setName(deploymentMetadata.getName());
        deploymentCheckDetails.setCreationTimestamp(deploymentMetadata.getCreationTimestamp());
        deploymentCheckDetails.setLabels(deploymentMetadata.getLabels());
        deploymentCheckDetails.setReplicas(getReplicaDetailsToDeploymentCheck(deployment));

        return deploymentCheckDetails;
    }

    private Map<String, Integer> getReplicaDetailsToDeploymentCheck(Deployment deployment) {
        Map<String, Integer> replicas = new HashMap<>();
        DeploymentStatus deploymentStatus = deployment.getStatus();
        replicas.put("total", deploymentStatus.getReplicas());
        replicas.put("ready", deploymentStatus.getReadyReplicas());
        replicas.put("unavailable", deploymentStatus.getUnavailableReplicas());
        replicas.put("available", deploymentStatus.getAvailableReplicas());
        replicas.put("updated", deploymentStatus.getUpdatedReplicas());

        return replicas;
    }

    private List<PodCheckDetails> getPodCheckDetails(String deploymentName, Map<String, String> selectors, KubernetesClient kubernetesClient) {
        PodList podList = kubernetesClient.pods().inNamespace(NAMESPACE).list();
        List<Pod> filteredPodList = podList.getItems().stream()
                .filter(x -> x.getMetadata().getName().startsWith(deploymentName))
                .filter(x -> x.getMetadata().getLabels()
                        .entrySet().containsAll(selectors.entrySet())).collect(Collectors.toList());

        List<PodCheckDetails> podCheckDetailsList = new ArrayList<>(filteredPodList.size());
        filteredPodList.parallelStream().forEach(x -> podCheckDetailsList.add(getIndividualPodDetails(x)));

        return podCheckDetailsList;
    }

    private PodCheckDetails getIndividualPodDetails(Pod pod) {
        PodCheckDetails podCheckDetails = new PodCheckDetails();

        ObjectMeta podMetadata = pod.getMetadata();
        podCheckDetails.setName(podMetadata.getName());
        podCheckDetails.setCreationTimestamp(podMetadata.getCreationTimestamp());
        podCheckDetails.setLabels(podMetadata.getLabels());

        PodStatus podStatus = pod.getStatus();
        podCheckDetails.setPodStauts(podStatus.getPhase());

        ContainerStatus containerStatus = podStatus.getContainerStatuses().get(0);
        podCheckDetails.setImage(containerStatus.getImage());
        podCheckDetails.setImageID(containerStatus.getImageID());

        return podCheckDetails;
    }

    private KubernetesClient getKubernetesClient(Environment environment) {
        return new DefaultKubernetesClient(
                new ConfigBuilder()
                        .withMasterUrl(environment.getKubernetesApiEndpoint())
                        .withOauthToken(environment.getKubernetesToken())
                        .withTrustCerts(true)
                        .build());
    }
}
