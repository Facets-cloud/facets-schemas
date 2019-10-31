package com.capillary.ops.deployer.service.interfaces;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.ApplicationSecret;
import com.capillary.ops.deployer.bo.DeploymentStatusDetails;
import com.capillary.ops.deployer.bo.Environment;
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

    DeploymentStatusDetails getDeploymentStatus(Application application, Environment environment, String deploymentName);
}
