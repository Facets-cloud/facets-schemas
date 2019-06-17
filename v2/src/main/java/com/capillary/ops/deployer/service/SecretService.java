package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.ApplicationSecret;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.repository.ApplicationSecretsRepository;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SecretService {

    @Autowired
    private ApplicationSecretsRepository applicationSecretsRepository;

    public List<ApplicationSecret> initializeApplicationSecrets(List<ApplicationSecret> applicationSecrets) {
        applicationSecrets.parallelStream().forEach(x -> {
            x.setSecretStatus(ApplicationSecret.SecretStatus.PENDING);
            x.setSecretValue("");
        });

        return applicationSecretsRepository.saveAll(applicationSecrets);
    }

    public List<ApplicationSecret> getApplicationSecrets(String environmentName, ApplicationFamily applicationFamily, String applicationId) {
        return applicationSecretsRepository.findByEnvironmentNameAndApplicationFamilyAndApplicationId(environmentName, applicationFamily, applicationId);
    }

    public List<ApplicationSecret> updateApplicationSecrets(String environmentName, ApplicationFamily applicationFamily, String applicationId, List<ApplicationSecret> applicationSecrets) {
        List<ApplicationSecret> savedSecrets = getApplicationSecrets(environmentName, applicationFamily, applicationId);
        Map<String, ApplicationSecret> secretMap = Maps.newHashMapWithExpectedSize(savedSecrets.size());
        savedSecrets.forEach(x -> secretMap.put(x.getSecretName(), x));

        applicationSecrets.parallelStream().forEach(x -> {
            ApplicationSecret secret = secretMap.get(x.getSecretName());
            secret.setSecretStatus(ApplicationSecret.SecretStatus.FULFILLED);
            secret.setSecretValue(x.getSecretValue());
        });

        return applicationSecretsRepository.saveAll(savedSecrets);
    }
}
