package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.*;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DeploymentStatus;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KubectlService {

    private static final Logger logger = LoggerFactory.getLogger(KubectlService.class);

    private static final String NAMESPACE = "default";

    public void createOrUpdateSecrets(Environment environment, String secretName, List<ApplicationSecret> applicationSecrets) {
        logger.info("creating kubernetes secret with name: {}", secretName);
        KubernetesClient kubernetesClient = getKubernetesClient(environment);

        Map<String, String> secretMap = applicationSecrets.parallelStream()
                .collect(Collectors.toMap(ApplicationSecret::getSecretName, ApplicationSecret::getSecretValue));

        Secret existingSecrets = kubernetesClient.secrets().inNamespace("default").withName(secretName).get();
        if (existingSecrets != null) {
            secretMap.putAll(existingSecrets.getData());
        }

        Secret secret = new SecretBuilder()
                .withNewMetadata()
                .withName(secretName)
                .addToLabels("name", secretName)
                .endMetadata()
                .addToData(secretMap)
                .withType("Opaque")
                .build();

        kubernetesClient.secrets().inNamespace("default").createOrReplace(secret);
    }

    public DeploymentStatusDetails getDeploymentStatus(Application application, Environment environment, String deploymentName) {
        KubernetesClient kubernetesClient = getKubernetesClient(environment);

        ApplicationServiceDetails applicationServiceDetails = getApplicationServiceDetails(deploymentName, kubernetesClient);
        ApplicationDeploymentDetails applicationDeploymentDetails = getApplicationDeploymentDetails(deploymentName, kubernetesClient);
        List<ApplicationPodDetails> applicationPodDetails = getApplicationPodDetails(deploymentName,
                applicationServiceDetails.getSelectors(), kubernetesClient);

        return new DeploymentStatusDetails(applicationServiceDetails, applicationDeploymentDetails, applicationPodDetails);
    }

    private ApplicationServiceDetails getApplicationServiceDetails(String deploymentName, KubernetesClient kubernetesClient) {
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

    private ApplicationDeploymentDetails getApplicationDeploymentDetails(String deploymentName, KubernetesClient kubernetesClient) {
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

    private List<ApplicationPodDetails> getApplicationPodDetails(String deploymentName, Map<String, String> selectors, KubernetesClient kubernetesClient) {
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
