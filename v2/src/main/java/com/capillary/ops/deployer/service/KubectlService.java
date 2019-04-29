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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KubectlService {

    private static final String NAMESPACE = "default";

    public DeploymentStatusDetails getDeploymentStatus(Application application, Environment environment, String deploymentName) {
        KubernetesClient kubernetesClient = getKubernetesClient(environment);

        ApplicationServiceDetails applicationServiceDetails = getServiceCheckDetails(deploymentName, kubernetesClient);
        ApplicationDeploymentDetails applicationDeploymentDetails = getDeploymentCheckDetails(deploymentName, kubernetesClient);
        List<ApplicationPodDetails> applicationPodDetails = getPodCheckDetails(deploymentName,
                applicationServiceDetails.getSelectors(), kubernetesClient);

        return new DeploymentStatusDetails(applicationServiceDetails, applicationDeploymentDetails, applicationPodDetails);
    }

    private ApplicationServiceDetails getServiceCheckDetails(String deploymentName, KubernetesClient kubernetesClient) {
        io.fabric8.kubernetes.api.model.Service service = kubernetesClient.services()
                .inNamespace(NAMESPACE)
                .withName(deploymentName)
                .get();

        ObjectMeta serviceMetadata = service.getMetadata();
        ServiceSpec serviceSpec = service.getSpec();
        ApplicationServiceDetails.ServiceType serviceType = ApplicationServiceDetails.ServiceType.valueOf(serviceSpec.getType());

        List<ApplicationServiceDetails.PortMapping> internalPortList = new ArrayList<>();
        serviceSpec.getPorts().forEach(x -> internalPortList.add(
                new ApplicationServiceDetails.PortMapping(
                        x.getProtocol(), deploymentName + "." + NAMESPACE + x.getPort())));

        List<ApplicationServiceDetails.PortMapping> externalPortList = new ArrayList<>();
        List<LoadBalancerIngress> ingresses = service.getStatus().getLoadBalancer().getIngress();
        ingresses.forEach(x -> internalPortList.forEach(p -> externalPortList.add(
                new ApplicationServiceDetails.PortMapping(p.getProtocol(), x.getHostname() + ":" + p))));

        return new ApplicationServiceDetails(serviceMetadata.getName(), serviceType, internalPortList, externalPortList,
                serviceMetadata.getLabels(), serviceSpec.getSelector(), serviceMetadata.getCreationTimestamp());
    }

    private ApplicationDeploymentDetails getDeploymentCheckDetails(String deploymentName, KubernetesClient kubernetesClient) {
        Deployment deployment = kubernetesClient.extensions()
                .deployments().
                inNamespace(NAMESPACE)
                .withName(deploymentName)
                .get();

        ObjectMeta deploymentMetadata = deployment.getMetadata();
        PodReplicationDetails replicationDetails = getReplicationDetails(deployment);

        return new ApplicationDeploymentDetails(deploymentMetadata.getName(), replicationDetails, deploymentMetadata.getLabels(), deploymentMetadata.getCreationTimestamp());
    }

    private PodReplicationDetails getReplicationDetails(Deployment deployment) {
        DeploymentStatus deploymentStatus = deployment.getStatus();

        return new PodReplicationDetails(deploymentStatus.getReplicas(), deploymentStatus.getReadyReplicas(),
                deploymentStatus.getUnavailableReplicas(), deploymentStatus.getAvailableReplicas(),
                deploymentStatus.getUpdatedReplicas());
    }

    private List<ApplicationPodDetails> getPodCheckDetails(String deploymentName, Map<String, String> selectors, KubernetesClient kubernetesClient) {
        PodList podList = kubernetesClient.pods().inNamespace(NAMESPACE).list();
        List<Pod> filteredPodList = podList.getItems().stream()
                .filter(x -> x.getMetadata().getName().startsWith(deploymentName))
                .filter(x -> x.getMetadata().getLabels()
                        .entrySet().containsAll(selectors.entrySet())).collect(Collectors.toList());

        List<ApplicationPodDetails> applicationPodDetailsList = new ArrayList<>(filteredPodList.size());
        filteredPodList.parallelStream().forEach(x -> applicationPodDetailsList.add(getIndividualPodDetails(x)));

        return applicationPodDetailsList;
    }

    private ApplicationPodDetails getIndividualPodDetails(Pod pod) {
        ObjectMeta podMetadata = pod.getMetadata();
        PodStatus podStatus = pod.getStatus();
        ContainerStatus containerStatus = podStatus.getContainerStatuses().get(0);

        return new ApplicationPodDetails(podMetadata.getName(), podMetadata.getLabels(), podStatus.getPhase(),
                containerStatus.getImage(), containerStatus.getImageID(), podMetadata.getCreationTimestamp());
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
