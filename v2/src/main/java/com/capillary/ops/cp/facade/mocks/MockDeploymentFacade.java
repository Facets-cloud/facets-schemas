package com.capillary.ops.cp.facade.mocks;

import com.capillary.ops.cp.bo.CodeBuildStatusCallback;
import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.ClusterResourceDetails;
import com.capillary.ops.cp.repository.DeploymentLogRepository;
import com.capillary.ops.cp.repository.ClusterResourceDetailsRepository;
import com.capillary.ops.cp.service.TFBuildService;
import com.jcabi.aspects.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Loggable
@Profile("dev")
public class MockDeploymentFacade {
    @Autowired
    DeploymentLogRepository deploymentLogRepository;

    @Autowired
    TFBuildService tfBuildService;

    @Autowired
    ClusterResourceDetailsRepository clusterResourceDetailsRepository;

    private static final Logger logger = LoggerFactory.getLogger(MockDeploymentFacade.class);

    public void mockHandleCodeBuildCallback(CodeBuildStatusCallback callback) {
        Optional<DeploymentLog> deploymentLogOptional =
                deploymentLogRepository.findOneByCodebuildId(callback.getCodebuidId());
        if(!deploymentLogOptional.isPresent()) {
            return;
        }
        DeploymentLog deploymentLog = deploymentLogOptional.get();
        deploymentLog = tfBuildService.loadDeploymentStatus(deploymentLog, true);

        Optional<ClusterResourceDetails> refreshResourceJob = clusterResourceDetailsRepository.findByCodeBuildId(callback.getCodebuidId());
        StatusType refreshResourceStatus = deploymentLog.getStatus();

        refreshResourceJob.ifPresent(resourceJob -> {
            resourceJob.setStatus(refreshResourceStatus);
            if (StatusType.SUCCEEDED.equals(resourceJob.getStatus())){
                Map<String, String> map = new HashMap<>();
                map.put("key1", "value1");
                map.put("key2", "value2");
                resourceJob.setResourceDetails(map);
            }
            clusterResourceDetailsRepository.save(resourceJob);
            logger.info("resource details saved successfully");
        });
    }
}
