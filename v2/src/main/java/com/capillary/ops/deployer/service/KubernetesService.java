package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.bo.actions.ActionExecution;
import com.capillary.ops.deployer.bo.actions.ApplicationAction;
import com.capillary.ops.deployer.bo.actions.TriggerStatus;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.service.interfaces.IKubernetesService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jcabi.aspects.Loggable;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.capillary.ops.deployer.bo.Application.ApplicationType.SCHEDULED_JOB;

@Profile("!devee")
@Service
@Loggable
public class KubernetesService implements IKubernetesService {

    private static final Logger logger = LoggerFactory.getLogger(KubernetesService.class);

    private static final String NAMESPACE = "default";

    private static class KubeExecListener implements ExecListener {

        @Override
        public void onOpen(Response response) {
            logger.info("inside onOpen");
            logger.info("response: {}", response);
            logger.info("The shell will remain open for 10 seconds.");
        }

        @Override
        public void onFailure(Throwable t, Response response) {
            logger.info("inside onFailure");
            logger.info("response: {}", response);
            logger.info("shell barfed");
        }

        @Override
        public void onClose(int code, String reason) {
            logger.info("inside onClose");
            logger.info("The shell will now close.");
        }
    }

    @Override
    public DeploymentList getDeployments(Environment environment, String namespace) {
        if (namespace == null) {
            namespace = "default";
        }

        KubernetesClient kubernetesClient = getKubernetesClient(environment);
        return kubernetesClient.extensions().deployments().inNamespace(namespace).list();
    }

    @Override
    public DeploymentList getDeployments(Environment environment) {
        return getDeployments(environment, "default");
    }

    @Override
    public Secret getSecretWithName(Environment environment, String secretName, String namespace) {
        return getKubernetesClient(environment).secrets().inNamespace(namespace).withName(secretName).get();
    }

    @Override
    public Secret getSecretWithName(Environment environment, String secretName) {
        return getSecretWithName(environment, secretName, "default");
    }

    @Override
    public void createOrUpdateApplicationSecrets(Environment environment, String secretName, List<ApplicationSecret> applicationSecrets) {
        logger.info("creating kubernetes secret with name: {}", secretName);

        Map<String, String> secretMap = applicationSecrets.parallelStream()
                .collect(Collectors.toMap(ApplicationSecret::getSecretName, ApplicationSecret::getSecretValue));

        createOrUpdateSecret(environment, secretName, secretMap);
    }

    @Override
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

    @Override
    public DeploymentStatusDetails getDeploymentStatus(Application application, Environment environment,
        String deploymentName, boolean isCC) {

        KubernetesClient kubernetesClient = getKubernetesClient(environment);
        if(isCC){

            DeploymentList deploymentList =
                kubernetesClient.extensions().deployments().inNamespace("").withLabel("deployerid", deploymentName)
                    .list();
            if(deploymentList.getItems().size()>0){
                deploymentName = deploymentList.getItems().get(0).getMetadata().getName();
            }else{
                throw new NotFoundException("No CC Service found for this appid");
            }
        }
        ApplicationServiceDetails applicationServiceDetails = null;
        ApplicationDeploymentDetails applicationDeploymentDetails;
        Map<String, String> selectors = new HashMap<>();
        if (application.getApplicationType().equals(SCHEDULED_JOB)) {
            applicationDeploymentDetails = getCronjobDeploymentDetails(deploymentName, kubernetesClient);
            selectors = ImmutableMap.of("app", deploymentName);
        } else {
            applicationServiceDetails = getApplicationServiceDetails(deploymentName, kubernetesClient);
            applicationDeploymentDetails= getApplicationDeploymentDetails(deploymentName, kubernetesClient);
            if(application.getApplicationFamily().equals(ApplicationFamily.CRM) && applicationServiceDetails == null) {
                applicationServiceDetails = getApplicationServiceDetails(deploymentName + "-test", kubernetesClient);
            }
            selectors = applicationServiceDetails.getSelectors(); 
        }

        List<ApplicationPodDetails> applicationPodDetails = getApplicationPodDetails(
                deploymentName, selectors, kubernetesClient);
        return new DeploymentStatusDetails(applicationServiceDetails, applicationDeploymentDetails, applicationPodDetails);
    }

    @Override
    public List<ApplicationPodDetails> getApplicationPodDetails(Application application, Environment environment, String deploymentName) {
        KubernetesClient kubernetesClient = getKubernetesClient(environment);
        ImmutableMap<String, String> selectors = ImmutableMap.of("app", deploymentName);
        return getApplicationPodDetails(deploymentName, selectors, kubernetesClient);
    }

    private String[] getActionCommand(ApplicationAction applicationAction) {
        String actionPath = applicationAction.getPath();
        String arguments = applicationAction.getArguments();
        if (org.springframework.util.StringUtils.isEmpty(arguments)) {
            return new String[]{actionPath};
        }

        return new String[]{actionPath, arguments};
    }

    private ByteArrayOutputStream executeKubeCommand(Environment environment, String podName, String[] command) throws Exception {
        KubernetesClient kubernetesClient = getKubernetesClient(environment);
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        logger.info("executing command: {} on pod: {}", command, podName);
        try (ExecWatch watch =
                     kubernetesClient
                             .pods()
                             .inNamespace(NAMESPACE)
                             .withName(podName)
                             .readingInput(System.in)
                             .writingOutput(os)
                             .writingError(System.err)
                             .withTTY()
                             .usingListener(new KubeExecListener())
                             .exec(command)) {
            Thread.sleep(5 * 1000);
            return os;
        } catch (Exception ex) {
            logger.error("error happened while trying to execute command: {} on pod: {}", command, podName);
            throw ex;
        }
    }

    @Override
    public ActionExecution executeAction(ApplicationAction applicationAction, Environment environment, String podName) {
        ActionExecution actionExecution = new ActionExecution(
                applicationAction,
                TriggerStatus.SUCCESS,
                System.currentTimeMillis());
        String[] command = getActionCommand(applicationAction);
        logger.info("executing commmand: {} on pod: {}", command, podName);
        try {
            ByteArrayOutputStream outputStream = executeKubeCommand(environment, podName, command);
            String output = new String(outputStream.toByteArray());
            actionExecution.setOutput(output);
            outputStream.close();
        } catch (Exception ex) {
            logger.error("error happened while trying to execute application action: {}", applicationAction, ex);
            actionExecution.setTriggerStatus(TriggerStatus.FAILURE);
            actionExecution.setTriggerException(ex.getMessage());
        }

        return actionExecution;
    }

    @Override
    public void haltApplication(String deploymentName, Environment environment) {
        KubernetesClient kubernetesClient = getKubernetesClient(environment);
        Deployment deployment = kubernetesClient.apps().deployments().inNamespace("default").withName(deploymentName).get();
        if(deployment.getSpec().getReplicas() > 0) {
            deployment.getSpec().setReplicas(0);
        }
        kubernetesClient.apps().deployments().inNamespace("default").withName(deploymentName).patch(deployment);
    }

    @Override
    public void resumeApplication(String deploymentName, Environment environment) {
        KubernetesClient kubernetesClient = getKubernetesClient(environment);
        Deployment deployment = kubernetesClient.apps().deployments().inNamespace("default").withName(deploymentName).get();
        HorizontalPodAutoscaler horizontalPodAutoscaler =
                kubernetesClient.autoscaling().horizontalPodAutoscalers().inNamespace("default").withName(deploymentName).get();
        Integer minReplicas = horizontalPodAutoscaler.getSpec().getMinReplicas();
        if(deployment.getSpec().getReplicas() == 0) {
            deployment.getSpec().setReplicas(minReplicas);
        }
        kubernetesClient.apps().deployments().inNamespace("default").withName(deploymentName).patch(deployment);
    }

    @Override
    public void deleteServiceCreatedByKubeCompassIfExists(String appName, Environment environment) {
        KubernetesClient kubernetesClient = getKubernetesClient(environment);
        io.fabric8.kubernetes.api.model.Service service = kubernetesClient.services().inNamespace("default").withName(appName).get();
        if(service.getMetadata().getLabels().get("origin").equals("cap-zk")){
            logger.info("Deleting {} service created by kube-compass on {}",appName, environment.getEnvironmentMetaData().getName());
            kubernetesClient.services().inNamespace("default").withName(appName).delete();
        }
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
            String externalDns = serviceMetadata.getAnnotations().getOrDefault("external-dns.alpha.kubernetes.io/hostname","NA");

            return new ApplicationServiceDetails(serviceMetadata.getName(), serviceType, internalPortList, externalPortList,
                    serviceMetadata.getLabels(), serviceSpec.getSelector(), serviceMetadata.getCreationTimestamp(),externalDns);
        }

        return null;
    }

    private ApplicationDeploymentDetails getApplicationDeploymentDetails(String deploymentName,
                                                                         KubernetesClient kubernetesClient) {
        logger.info("getting deployment with name: {}", deploymentName);
        io.fabric8.kubernetes.api.model.apps.Deployment deployment = kubernetesClient.extensions()
                .deployments().
                        inNamespace(NAMESPACE)
                .withName(deploymentName)
                .get();

        logger.info("getting HPA with name: {}", deploymentName);
        HorizontalPodAutoscaler hpa = kubernetesClient.autoscaling()
                .horizontalPodAutoscalers()
                .inNamespace(NAMESPACE)
                .withName(deploymentName)
                .get();

        if (deployment != null) {
            ObjectMeta deploymentMetadata = deployment.getMetadata();
            PodReplicationDetails replicationDetails = getReplicationDetails(deployment);
            HPADetails hpaDetails = null;
            logger.info("found pod replication details for deployment: {}", deploymentName);

            Map<String, String> environmentConfigs = getEnvironmentConfigs(kubernetesClient, deploymentName);
            logger.info("found environment configs for deployment: {}", deploymentName);

            List<String> credentialsList = getCredentialsList(kubernetesClient, deploymentName);
            logger.info("found credentials list for deployment: {}", deploymentName);

            if(hpa != null){
                hpaDetails = getHPADetails(hpa);
            }

            return new ApplicationDeploymentDetails(
                    deploymentMetadata.getName(),
                    environmentConfigs,
                    credentialsList,
                    replicationDetails,
                    deploymentMetadata.getLabels(),
                    hpaDetails,
                    deploymentMetadata.getCreationTimestamp());
        }

        return null;
    }

    private ApplicationDeploymentDetails getCronjobDeploymentDetails(String deploymentName,
                                                                         KubernetesClient kubernetesClient) {
        logger.info("getting cronjob with name: {}", deploymentName);
        CronJob cronjob = kubernetesClient.batch()
                .cronjobs()
                .inNamespace(NAMESPACE)
                .withName(deploymentName)
                .get();

        if (cronjob != null) {
            ObjectMeta cronjobMetadata = cronjob.getMetadata();

            Map<String, String> environmentConfigs = getEnvironmentConfigs(kubernetesClient, deploymentName);
            logger.info("found environment configs for cronjob: {}", deploymentName);

            List<String> credentialsList = getCredentialsList(kubernetesClient, deploymentName);
            logger.info("found credentials list for cronjob: {}", deploymentName);

            return new ApplicationDeploymentDetails(
                    cronjobMetadata.getName(),
                    environmentConfigs,
                    credentialsList,
                    null,
                    cronjobMetadata.getLabels(),
                    null,
                    cronjobMetadata.getCreationTimestamp());
        }

        return null;
    }

    private HPADetails getHPADetails(HorizontalPodAutoscaler hpa) {
        return new HPADetails(hpa.getSpec().getMinReplicas(),
                hpa.getSpec().getMaxReplicas(),
                hpa.getStatus().getCurrentReplicas(),
                hpa.getStatus().getDesiredReplicas(),
                hpa.getSpec().getTargetCPUUtilizationPercentage(),
                hpa.getStatus().getCurrentCPUUtilizationPercentage());
    }

    private Map<String, String> getEnvironmentConfigs(KubernetesClient kubernetesClient, String deploymentName) {
        Secret secret = kubernetesClient.secrets().inNamespace(NAMESPACE).withName(deploymentName + "-configs").get();
        return secret == null ? null : decodeConfigValues(secret);
    }

    private List<String> getCredentialsList(KubernetesClient kubernetesClient, String deploymentName) {
        Secret secret = kubernetesClient.secrets().inNamespace("default").withName(deploymentName + "-credentials").get();
        return secret == null ? null : getSecretKeys(secret);
    }

    private List<String> getSecretKeys(Secret secret) {
        if(secret==null) {
            return new ArrayList<>();
        }
        Map<String, String> secretData = secret.getData();
        if(secretData == null) {
            return new ArrayList<>();
        }
        List<String> credentialsList = Lists.newArrayListWithExpectedSize(secretData.size());
        secretData.forEach((k,v) -> {
            credentialsList.add(k);
        });

        return credentialsList;
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

    private PodReplicationDetails getReplicationDetails(io.fabric8.kubernetes.api.model.apps.Deployment deployment) {
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

        applicationPodDetailsList.replaceAll(p -> p.setResourceUsage(getResourceUsage(p.getName(),kubernetesClient)));

        return applicationPodDetailsList;
    }

    private ApplicationPodDetails getIndividualPodDetails(Pod pod) {
        ObjectMeta podMetadata = pod.getMetadata();
        PodStatus podStatus = pod.getStatus();
        String image = pod.getSpec().getContainers().get(0).getImage();
        String podLifecycleState= "Unknown";
        boolean ready = false;
        int podRestarts = 0;
        String restartReason = "NA";

        try {
            if (podStatus.getContainerStatuses().size() > 0) {
                ContainerStatus containerStatus = podStatus.getContainerStatuses().get(0);
                ContainerStateTerminated terminatedState = containerStatus.getLastState().getTerminated();
                ready = containerStatus.getReady();
                podRestarts = containerStatus.getRestartCount();
                if (terminatedState != null) {
                    StringBuilder returnStr = new StringBuilder("Terminated");
                    if (terminatedState.getReason() != null) {
                        returnStr.append(": ");
                        returnStr.append(terminatedState.getReason());
                    }
                    if (terminatedState.getMessage() != null) {
                        returnStr.append(": ");
                        returnStr.append(terminatedState.getMessage());
                    }
                    restartReason = returnStr.toString();
                }
            }
            podLifecycleState = getPodLifecycleState(podStatus);
        } catch (Exception e){
            logger.error("Error fetching pod status:", e.getMessage() + e.getStackTrace());
        }

        return new ApplicationPodDetails(podMetadata.getName(), podMetadata.getLabels(), podLifecycleState,
                image, image, podMetadata.getCreationTimestamp(), ready, podRestarts, restartReason);
    }

    private String getPodLifecycleState(PodStatus podStatus) throws Exception {
        switch (podStatus.getPhase()) {
            case "Running": {
                if (podStatus.getContainerStatuses().isEmpty()) {
                    return "Unknown";
                }
                ContainerState state = podStatus.getContainerStatuses().get(0).getState();
                if (state != null) {
                    ContainerStateRunning running = state.getRunning();
                    ContainerStateTerminated terminated = state.getTerminated();
                    ContainerStateWaiting waiting = state.getWaiting();
                    if (running != null) {
                        return podStatus.getPhase();
                    } else if (terminated != null) {
                        StringBuilder returnStr = new StringBuilder("Terminated");
                        if (terminated.getReason() != null) {
                            returnStr.append(": ");
                            returnStr.append(terminated.getReason());
                        }
                        if (terminated.getMessage() != null) {
                            returnStr.append(": ");
                            returnStr.append(terminated.getMessage());
                        }
                        return returnStr.toString();
                    } else if (waiting != null) {
                        StringBuilder returnStr = new StringBuilder("Waiting");
                        if (waiting.getReason() != null) {
                            returnStr.append(": ");
                            returnStr.append(waiting.getReason());
                        }
                        if (waiting.getMessage() != null) {
                            returnStr.append(": ");
                            returnStr.append(waiting.getMessage());
                        }
                        return returnStr.toString();
                    }
                } else {
                    return podStatus.getPhase();
                }
            }
            case "Pending": {
                //Check failed conditions
                List<PodCondition> podConditionList = podStatus.getConditions().stream()
                        .filter(c -> (c.getStatus().equals("False") || c.getStatus().equals("Unknown")))
                        .collect(Collectors.toList());
                if (!podConditionList.isEmpty()) {
                    PodCondition podScheduledCondition = podConditionList.get(0);
                    //If container is not ready, check container status
                    if (podScheduledCondition.getType().equals("Ready")) {
                        if (podStatus.getContainerStatuses().isEmpty()) {
                            return "Unknown";
                        }
                        ContainerState state = podStatus.getContainerStatuses().get(0).getState();
                        if (state != null) {
                            ContainerStateWaiting waiting = state.getWaiting();
                            if (waiting != null) {
                                StringBuilder returnStr = new StringBuilder("Pending");
                                if (waiting.getReason() != null) {
                                    returnStr.append(": ");
                                    returnStr.append(waiting.getReason());
                                }
                                if (waiting.getMessage() != null) {
                                    returnStr.append(": ");
                                    returnStr.append(waiting.getMessage());
                                }
                                return returnStr.toString();
                            }
                        } else {
                            return podStatus.getPhase();
                        }
                    } else {
                        StringBuilder returnStr = new StringBuilder("Pending");
                        if (podScheduledCondition.getReason() != null) {
                            returnStr.append(": ");
                            returnStr.append(podScheduledCondition.getReason());
                        }
                        if (podScheduledCondition.getMessage() != null) {
                            returnStr.append(": ");
                            returnStr.append(podScheduledCondition.getMessage());
                        }
                        return returnStr.toString();
                    }
                } else {
                    return podStatus.getPhase();
                }
            }
            default:
                return podStatus.getPhase();
        }
    }

    private KubernetesClient getKubernetesClient(Environment environment) {
        return new DefaultKubernetesClient(
                new ConfigBuilder()
                        .withMasterUrl(environment.getEnvironmentConfiguration().getKubernetesApiEndpoint())
                        .withOauthToken(environment.getEnvironmentConfiguration().getKubernetesToken())
                        .withTrustCerts(true)
                        .build());
    }

    private OkHttpClient getHttpK8sClient(KubernetesClient client){
        return ((DefaultKubernetesClient) client).getHttpClient();
    }

    private Request buildPodMetricsHttpGETRequest(String podName, String masterUrl){
        return new Request
                .Builder()
                .get()
                .url(masterUrl + "apis/metrics.k8s.io/v1beta1/namespaces/default/pods/" + podName)
                .build();
    }

    private PodResource getResourceUsage(String podName, KubernetesClient kubernetesClient) {
        OkHttpClient httpClient = getHttpK8sClient(kubernetesClient);
        JSONObject jsonObject = new JSONObject();
        Response response = null;
        try {
            response = httpClient
                    .newCall(buildPodMetricsHttpGETRequest(podName, kubernetesClient.getMasterUrl().toString()))
                    .execute();
            jsonObject = new JSONObject(response.body().bytes());
            String cpuUsage = jsonObject.getJSONArray("containers").getJSONObject(0).getJSONObject("usage").getString("cpu");
            String memoryUsage = jsonObject.getJSONArray("containers").getJSONObject(0).getJSONObject("usage").getString("memory");
            return new PodResource(cpuUsage, memoryUsage);
        } catch (Throwable e) {
            logger.error("Exception getting pod resource usage", e.getStackTrace());
            return null;
        } finally {
            if(response != null) {
                response.close();
            }
        }
    }
}
