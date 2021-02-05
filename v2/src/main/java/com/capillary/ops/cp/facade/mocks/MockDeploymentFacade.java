package com.capillary.ops.cp.facade.mocks;

import com.capillary.ops.cp.bo.CodeBuildStatusCallback;
import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.ClusterResourceDetails;
import com.capillary.ops.cp.bo.ResourceDetails;
import com.capillary.ops.cp.repository.DeploymentLogRepository;
import com.capillary.ops.cp.repository.ClusterResourceDetailsRepository;
import com.capillary.ops.cp.service.TFBuildService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jcabi.aspects.Loggable;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
                List<ResourceDetails> details = null;
                try {
                    details = getLocalResourceDetails();
                    resourceJob.setResourceDetails(details);
                    clusterResourceDetailsRepository.save(resourceJob);
                    logger.info("resource details saved successfully");
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("error occured while trying to read the path ", e);
                } catch (JsonSyntaxException e){
                    logger.error("error while parsing the json in s3 path ", e);
                }
            }
        });
    }


    private List<ResourceDetails> getLocalResourceDetails() throws IOException, JsonSyntaxException {
        ResourceDetails resourceDetails = new ResourceDetails();
        resourceDetails.setName("mock_name");
        resourceDetails.setResourceName("mock_resource_name");
        resourceDetails.setResourceType("mock_resource_type");
        resourceDetails.setKey("mock_key");
        resourceDetails.setValue("mock_value");

        return new ArrayList<ResourceDetails>(){{add(resourceDetails);}};
    }
}
