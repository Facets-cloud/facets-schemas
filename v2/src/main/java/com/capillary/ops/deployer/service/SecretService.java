package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.ApplicationSecret;
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

    public List<ApplicationSecret> initializeApplicationSecrets(ApplicationFamily applicationFamily, String applicationId, List<ApplicationSecret> applicationSecrets) {
        List<ApplicationSecret> secretList = applicationSecrets.parallelStream()
                .map(x -> new ApplicationSecret(
                        applicationFamily,
                        applicationId,
                        x.getSecretName(),
                        ApplicationSecret.SecretStatus.PENDING))
                .collect(Collectors.toList());
        return applicationSecretsRepository.saveAll(secretList);
    }

    public List<ApplicationSecret> getApplicationSecrets(ApplicationFamily applicationFamily, String applicationId) {
        return applicationSecretsRepository.findByApplicationFamilyAndApplicationId(applicationFamily, applicationId);
    }

    public boolean doSecretsExist(ApplicationFamily applicationFamily, String applicationId, List<ApplicationSecret> applicationSecrets) {
        List<ApplicationSecret> savedSecrets = getApplicationSecrets(applicationFamily, applicationId);
        return savedSecrets.parallelStream().map(ApplicationSecret::getSecretName).collect(Collectors.toList())
                .containsAll(applicationSecrets.parallelStream().map(ApplicationSecret::getSecretName).collect(Collectors.toList()));
    }

    public List<ApplicationSecret> authorizeApplicationSecrets(ApplicationFamily applicationFamily, String applicationId, List<ApplicationSecret> applicationSecrets) {
        List<ApplicationSecret> savedSecrets = getApplicationSecrets(applicationFamily, applicationId);
        Map<String, ApplicationSecret> secretMap = Maps.newHashMapWithExpectedSize(savedSecrets.size());
        savedSecrets.forEach(x -> secretMap.put(x.getSecretName(), x));

        applicationSecrets.parallelStream().forEach(x -> {
            ApplicationSecret secret = secretMap.get(x.getSecretName());
            secret.setSecretStatus(ApplicationSecret.SecretStatus.FULFILLED);
        });

        return applicationSecretsRepository.saveAll(savedSecrets);
    }
}
