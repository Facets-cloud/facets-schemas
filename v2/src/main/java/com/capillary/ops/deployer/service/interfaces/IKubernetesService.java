package com.capillary.ops.deployer.service.interfaces;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.bo.actions.ActionExecution;
import com.capillary.ops.deployer.bo.actions.ApplicationAction;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;

import java.util.List;
import java.util.Map;

public interface IKubernetesService {
    DeploymentList getDeployments(Environment environment, String namespace);

    DeploymentList getDeployments(Environment environment);

    Secret getSecretWithName(Environment environment, String secretName, String namespace);

    Secret getSecretWithName(Environment environment, String secretName);

    void createOrUpdateApplicationSecrets(Environment environment, String secretName, List<ApplicationSecret> applicationSecrets);

    void createOrUpdateSecret(Environment environment, String secretName, Map<String, String> secretMap);

    DeploymentStatusDetails getDeploymentStatus(Application application, Environment environment, String deploymentName,
        boolean isCC);

    List<ApplicationPodDetails> getApplicationPodDetails(Application application, Environment environment, String deploymentName);

    ActionExecution executeAction(ApplicationAction applicationAction, Environment environment, String podName);

    void haltApplication(Application application, String deploymentName, Environment environment);

    void resumeApplication(Application application, String deploymentName, Environment environment);

    void deleteServiceCreatedByKubeCompassIfExists(String appName, Environment environment);
}
