package com.capillary.ops.cp.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.capillary.ops.cp.bo.ClusterResourceDetails;
import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.repository.ClusterResourceDetailsRepository;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcabi.aspects.Loggable;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Service
@Loggable
public class ClusterResourceRefreshService {

    @Autowired
    private ClusterResourceDetailsRepository clusterResourceDetailsRepository;

    @Value("${aws.s3bucket.testOutputBucket.name}")
    private String artifactS3Bucket;

    @Value("${aws.s3bucket.testOutputBucket.region}")
    private String artifactS3BucketRegion;

    private static final Logger logger = LoggerFactory.getLogger(ClusterResourceRefreshService.class);

    public void saveClusterResourceDetails(final String codeBuildId, final StatusType resourceRefreshStatus) {

        Optional<ClusterResourceDetails> refreshResourceJob = clusterResourceDetailsRepository.findByCodeBuildId(codeBuildId);

        refreshResourceJob.ifPresent(resourceJob -> {
            try{
                resourceJob.setStatus(resourceRefreshStatus);
                if (StatusType.SUCCEEDED.equals(resourceJob.getStatus())){
                    Map<String, String> map = getResourceDetails(codeBuildId);
                    resourceJob.setResourceDetails(map);
                }
                clusterResourceDetailsRepository.save(resourceJob);
                logger.info("resource details saved successfully");
            } catch (IOException e){
                logger.error("error occured while trying to read the s3 path", e);
            }
        });
    }

    public void refreshResourceCodeBuildSave(final DeploymentLog deploymentLog) {
        ClusterResourceDetails clusterResourceDetails = new ClusterResourceDetails();
        clusterResourceDetails.setCodeBuildId(deploymentLog.getCodebuildId());
        clusterResourceDetails.setClusterId(deploymentLog.getClusterId());
        clusterResourceDetailsRepository.save(clusterResourceDetails);
    }

    public Map<String, String> getClusterResourceDetails(final String clusterId) {
        Optional<ClusterResourceDetails> first = clusterResourceDetailsRepository.findFirstByClusterIdAndStatusOrderByIdDesc(clusterId, StatusType.SUCCEEDED);

        return first.orElseThrow(() -> new NotFoundException(String.format("cluster details for id %s not found", clusterId))).getResourceDetails();
    }

    private Map<String, String> getResourceDetails(String codeBuildId) throws IOException {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.valueOf(artifactS3BucketRegion)).build();

        String resource_path = String.format("%s/capillary-cloud-tf-apply/capillary-cloud-tf/tfaws/resource_values.json", codeBuildId.split(":")[1]);
        String details = IOUtils.toString(amazonS3.getObject(artifactS3Bucket, resource_path).getObjectContent(), StandardCharsets.UTF_8.name());
        return new Gson().fromJson(details, new TypeToken<Map<String, String >>(){}.getType());
    }
}
