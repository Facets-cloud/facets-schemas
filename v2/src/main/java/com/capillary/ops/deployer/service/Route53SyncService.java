package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Environment;
import com.google.common.collect.ImmutableMap;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DeploymentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class Route53SyncService {

    private static final Logger logger = LoggerFactory.getLogger(Route53SyncService.class);

    @Autowired
    private KubectlService kubectlService;

    @Autowired
    private HelmService helmService;

    @Scheduled(fixedRate = 900)
    public void syncDNS() {
        for (ApplicationFamily applicationFamily : ApplicationFamily.values()) {
            List<Environment> environments;
            try {
                environments = applicationFamily.getEnvironments();
            } catch (FileNotFoundException e) {
                logger.info("no cluster details file found for application family: {}", applicationFamily.name());
                continue;
            }

            environments.parallelStream().forEach(environment -> {
                if (environment.getPrivateZoneId() != null) {
                    createSyncIfAbsent(environment, "private");
                }

                if (environment.getPublicZoneId() != null) {
                    createSyncIfAbsent(environment, "public");
                }
            });
        }
    }

    private void createSyncIfAbsent(Environment environment, String zoneType) {
        String releaseName = environment.getName() + "-" + zoneType + "-route53-dns";
        if (!isDnsSyncPresent(environment, zoneType, releaseName) && createAwsSecret(environment)) {
            Map<String, Object> valueMap = getValuesMap(environment, zoneType);
            helmService.deploy(environment, releaseName, "route53-dns", valueMap);
        }
    }

    private Map<String, Object> getValuesMap(Environment environment, String zoneType) {
        Map<String, Object> app = new LinkedHashMap<>();
        app.put("environment", environment.getName());

        Map<String, Object> aws = new LinkedHashMap<>();
        aws.put("zoneId", environment.getPrivateZoneId());
        aws.put("zoneType", zoneType);

        Map<String, Object> valueMap = new LinkedHashMap<>();
        valueMap.put("app", app);
        valueMap.put("aws", aws);

        return valueMap;
    }

    private boolean createAwsSecret(Environment x) {
        if (x.getAwsAccessKey() == null || x.getAwsSecretKey() == null) {
            logger.error("cannot read aws secrets, please update the access and secret keys in cluster details");
            return false;
        }

        kubectlService.createOrUpdateSecret(x, "awsCredentials", ImmutableMap.of(
                "AWS_ACCESS_KEY", x.getAwsAccessKey(),
                "AWS_SECRET_KEY", x.getAwsSecretKey()));

        return true;
    }

    private boolean isDnsSyncPresent(Environment environment, String zoneType, String releaseName) {
        DeploymentList deployments = kubectlService.getDeployments(environment);
        for (Deployment deployment : deployments.getItems()) {
            if (deployment.getMetadata().getName().equals(releaseName)) {
                return true;
            }
        }

        return false;
    }
}
