package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.*;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DeploymentList;
import io.fabric8.kubernetes.api.model.extensions.DeploymentStatus;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
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

    public DeploymentList getDeployments(Environment environment, String namespace) {
        if (namespace == null) {
            namespace = "default";
        }

        KubernetesClient kubernetesClient = getKubernetesClient(environment);
        return kubernetesClient.extensions().deployments().inNamespace(namespace).list();
    }

    public DeploymentList getDeployments(Environment environment) {
        return getDeployments(environment, "default");
    }

    public Secret getSecretWithName(Environment environment, String secretName, String namespace) {
        return getKubernetesClient(environment).secrets().inNamespace(namespace).withName(secretName).get();
    }

    public Secret getSecretWithName(Environment environment, String secretName) {
        return getSecretWithName(environment, secretName, "default");
    }

    public void createOrUpdateApplicationSecrets(Environment environment, String secretName, List<ApplicationSecret> applicationSecrets) {
        logger.info("creating kubernetes secret with name: {}", secretName);

        Map<String, String> secretMap = applicationSecrets.parallelStream()
                .collect(Collectors.toMap(ApplicationSecret::getSecretName, ApplicationSecret::getSecretValue));

        createOrUpdateSecret(environment, secretName, secretMap);
    }

    public void createOrUpdateSecret(Environment environment, String secretName, Map<String, String> secretMap) {
        Secret existingSecret = getSecretWithName(environment, secretName);
        if (existingSecret != null) {
            secretMap.putAll(existingSecret.getData());
        }

        Secret secret = new SecretBuilder()
                .withNewMetadata()
                .withName(secretName)
                .addToLabels("name", secretName)
                .endMetadata()
                .addToData(secretMap)
                .withType("Opaque")
                .build();

        getKubernetesClient(environment).secrets().inNamespace("default").createOrReplace(secret);
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
        logger.info("getting service details for service: {}", deploymentName);
        io.fabric8.kubernetes.api.model.Service service = kubernetesClient.services()
                .inNamespace(NAMESPACE)
                .withName(deploymentName)
                .get();

        if (service != null) {
            String serviceName = service.getMetadata().getName();

            ObjectMeta serviceMetadata = service.getMetadata();
            ServiceSpec serviceSpec = service.getSpec();
            ApplicationServiceDetails.ServiceType serviceType = ApplicationServiceDetails.ServiceType.valueOf(serviceSpec.getType());

            List<ApplicationServiceDetails.Endpoint> internalPortList = new ArrayList<>();
            logger.info("fetching port mapping from service spec");
            serviceSpec.getPorts().forEach(x -> internalPortList.add(
                    new ApplicationServiceDetails.Endpoint(
                            x.getProtocol(), deploymentName + "." + NAMESPACE + ":" + x.getPort())));
            logger.info("found internal port mapping: {}", internalPortList);

            List<ApplicationServiceDetails.Endpoint> externalPortList = new ArrayList<>();
            logger.info("fetching external port mapping for service: {}", serviceName);
            List<LoadBalancerIngress> ingresses = service.getStatus().getLoadBalancer().getIngress();
            ingresses.forEach(x -> internalPortList.forEach(p -> externalPortList.add(
                    new ApplicationServiceDetails.Endpoint(p.getProtocol(), x.getHostname() + ":" + p.getEndpoint().split(":")[1]))));
            logger.info("found external port mapping: {}", externalPortList);

            return new ApplicationServiceDetails(serviceMetadata.getName(), serviceType, internalPortList, externalPortList,
                    serviceMetadata.getLabels(), serviceSpec.getSelector(), serviceMetadata.getCreationTimestamp());
        }

        return null;
    }

    private ApplicationDeploymentDetails getApplicationDeploymentDetails(String deploymentName, KubernetesClient kubernetesClient) {
        logger.info("getting deployment with name: {}", deploymentName);
        Deployment deployment = kubernetesClient.extensions()
                .deployments().
                inNamespace(NAMESPACE)
                .withName(deploymentName)
                .get();

        if (deployment != null) {
            ObjectMeta deploymentMetadata = deployment.getMetadata();
            PodReplicationDetails replicationDetails = getReplicationDetails(deployment);
            logger.info("found pod replication details for deployment: {}", deploymentName);

            Map<String, String> environmentConfigs = getEnvironmentConfigs(kubernetesClient, deploymentName);
            logger.info("found environment configs for deployment: {}", deploymentName);

            return new ApplicationDeploymentDetails(
                    deploymentMetadata.getName(),
                    environmentConfigs,
                    replicationDetails,
                    deploymentMetadata.getLabels(),
                    deploymentMetadata.getCreationTimestamp());
        }

        return null;
    }

    private Map<String, String> getEnvironmentConfigs(KubernetesClient kubernetesClient, String deploymentName) {
        Secret secret = kubernetesClient.secrets().inNamespace("default").withName(deploymentName + "-configs").get();
        return secret == null ? null : decodeConfigValues(secret);
    }

    private static Map<String, String> decodeConfigValues(Secret secret) {
        Map<String, String> secretData = secret.getData();
        Map<String, String> envConfigs = Maps.newHashMapWithExpectedSize(secretData.size());
        secretData.forEach((key, value) -> {
            String configValue = StringUtils.newStringUtf8(Base64.decodeBase64(value));
            envConfigs.put(key, configValue);
        });

        return envConfigs;
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
