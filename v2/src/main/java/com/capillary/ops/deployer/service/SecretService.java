package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.ApplicationSecret;
import com.capillary.ops.deployer.bo.ApplicationSecretRequest;
import com.capillary.ops.deployer.repository.ApplicationSecretRequestsRepository;
import com.capillary.ops.deployer.repository.ApplicationSecretsRepository;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SecretService {

    @Autowired
    private ApplicationSecretsRepository applicationSecretsRepository;

    @Autowired
    private ApplicationSecretRequestsRepository applicationSecretRequestsRepository;

    public List<ApplicationSecretRequest> initializeApplicationSecrets(List<ApplicationSecretRequest> applicationSecretRequests) {
        return applicationSecretRequestsRepository.saveAll(applicationSecretRequests);
    }

    public List<ApplicationSecret> getApplicationSecrets(String environmentName, ApplicationFamily applicationFamily, String applicationId) {
        return applicationSecretsRepository.findByEnvironmentNameAndApplicationFamilyAndApplicationId(environmentName, applicationFamily, applicationId);
    }

    public List<ApplicationSecretRequest> getApplicationSecretRequests(ApplicationFamily applicationFamily, String applicationId) {
        return applicationSecretRequestsRepository.findByApplicationFamilyAndApplicationId(applicationFamily, applicationId);
    }

    public List<ApplicationSecret> updateApplicationSecrets(String environmentName, ApplicationFamily applicationFamily, String applicationId, List<ApplicationSecret> applicationSecrets) {
        List<ApplicationSecretRequest> savedSecrets = getApplicationSecretRequests(applicationFamily, applicationId);
        Map<String, ApplicationSecretRequest> secretMap = Maps.newHashMapWithExpectedSize(savedSecrets.size());
        savedSecrets.forEach(x -> secretMap.put(x.getSecretName(), x));
        List<ApplicationSecret> secrets = new ArrayList<>();
        applicationSecrets.stream().forEach(x -> {
            ApplicationSecretRequest request = secretMap.get(x.getSecretName());
            Optional<ApplicationSecret> savedSecret = applicationSecretsRepository.findOneByEnvironmentNameAndApplicationFamilyAndApplicationIdAndSecretName(environmentName, applicationFamily, applicationId, x.getSecretName());
            ApplicationSecret secret = new ApplicationSecret(environmentName, applicationFamily, applicationId, request.getSecretName(), x.getSecretValue(), ApplicationSecret.SecretStatus.FULFILLED);
            secret.setSecretStatus(ApplicationSecret.SecretStatus.FULFILLED);
            if(savedSecret.isPresent()) {
                secret.setId(savedSecret.get().getId());
            }
            secret.setSecretValue(x.getSecretValue());
            secrets.add(secret);
        });

        return applicationSecretsRepository.saveAll(secrets);
    }
}
