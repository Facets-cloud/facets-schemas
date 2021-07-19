package com.capillary.ops.cp.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.capillary.ops.cp.bo.ClusterResourceDetails;
import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.ResourceDetails;
import com.capillary.ops.cp.repository.ClusterResourceDetailsRepository;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
import java.util.List;
import java.util.Optional;

@Service
@Loggable
public class ClusterResourceRefreshService {

    @Autowired
    private ClusterResourceDetailsRepository clusterResourceDetailsRepository;

    @Value("${cc_artifact_s3bucket}")
    private String artifactS3Bucket;

    @Value("${cc_artifact_s3bucket_region}")
    private String artifactS3BucketRegion;

    @Value("${cc_codebuild_name}")
    public String BUILD_NAME;

    private static final Logger logger = LoggerFactory.getLogger(ClusterResourceRefreshService.class);

    public boolean isSaveClusterResourceDetailsDone(final String codeBuildId, final StatusType resourceRefreshStatus){
        Optional<ClusterResourceDetails> refreshResourceJob = clusterResourceDetailsRepository.findByCodeBuildId(codeBuildId);
        if(refreshResourceJob.isPresent()){
            ClusterResourceDetails resourceJob = refreshResourceJob.get();
            return !StatusType.IN_PROGRESS.equals(resourceJob.getStatus());
        }
        return false;
    }

    public void saveClusterResourceDetails(final String codeBuildId, final StatusType resourceRefreshStatus) {

        Optional<ClusterResourceDetails> refreshResourceJob = clusterResourceDetailsRepository.findByCodeBuildId(codeBuildId);

        refreshResourceJob.ifPresent(resourceJob -> {
            try{
                resourceJob.setStatus(resourceRefreshStatus);
                if (StatusType.SUCCEEDED.equals(resourceJob.getStatus())){
                    List<ResourceDetails> map = getResourceDetails(codeBuildId);
                    resourceJob.setResourceDetails(map);
                }
                clusterResourceDetailsRepository.save(resourceJob);
                logger.info("resource details saved successfully");
            } catch (IOException e){
                logger.error("error occured while trying to read the s3 path", e);
            } catch (JsonSyntaxException jsonSyntaxException){
                logger.error("error occured while trying to parse json in s3 path ", jsonSyntaxException);
            }
        });
    }

    public void refreshResourceCodeBuildSave(final DeploymentLog deploymentLog) {
        ClusterResourceDetails clusterResourceDetails = new ClusterResourceDetails();
        clusterResourceDetails.setCodeBuildId(deploymentLog.getCodebuildId());
        clusterResourceDetails.setClusterId(deploymentLog.getClusterId());
        clusterResourceDetailsRepository.save(clusterResourceDetails);
    }

    public List<ResourceDetails> isSaveClusterResourceDetailsDone(final String clusterId) {
        Optional<ClusterResourceDetails> first = clusterResourceDetailsRepository.findFirstByClusterIdAndStatusOrderByIdDesc(clusterId, StatusType.SUCCEEDED);

        return first.orElseThrow(() -> new NotFoundException(String.format("cluster details for id %s not found", clusterId))).getResourceDetails();
    }

    private List<ResourceDetails> getResourceDetails(String codeBuildId) throws IOException, JsonSyntaxException {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(artifactS3BucketRegion)).build();

        String resource_path = String.format("%s/%s/capillary-cloud-tf/tfaws/resource_values.json", codeBuildId.split(":")[1], BUILD_NAME);
        String details = IOUtils.toString(amazonS3.getObject(artifactS3Bucket, resource_path).getObjectContent(), StandardCharsets.UTF_8.name());
        return new Gson().fromJson(details, new TypeToken<List<ResourceDetails>>(){}.getType());
    }
}
