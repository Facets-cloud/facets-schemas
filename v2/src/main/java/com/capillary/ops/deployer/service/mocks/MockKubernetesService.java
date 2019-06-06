package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.ApplicationSecret;
import com.capillary.ops.deployer.bo.DeploymentStatusDetails;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.service.interfaces.IKubernetesService;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.extensions.DeploymentList;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;
import java.util.Map;

@Profile("dev")
@Service
public class MockKubernetesService implements IKubernetesService {
    @Override
    public DeploymentList getDeployments(Environment environment, String namespace) {
        return null;
    }

    @Override
    public DeploymentList getDeployments(Environment environment) {
        return null;
    }

    @Override
    public Secret getSecretWithName(Environment environment, String secretName, String namespace) {
        return null;
    }

    @Override
    public Secret getSecretWithName(Environment environment, String secretName) {
        return null;
    }

    @Override
    public void createOrUpdateApplicationSecrets(Environment environment, String secretName, List<ApplicationSecret> applicationSecrets) {

    }

    @Override
    public void createOrUpdateSecret(Environment environment, String secretName, Map<String, String> secretMap) {

    }

    @Override
    public DeploymentStatusDetails getDeploymentStatus(Application application, Environment environment, String deploymentName) {
        PodamFactory podamFactory = new PodamFactoryImpl();
        return podamFactory.manufacturePojo(DeploymentStatusDetails.class);
    }
}
