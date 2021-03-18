package com.capillary.ops.cp.helpers;

import com.capillary.ops.cp.exceptions.ClusterDetailsNotFound;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

import java.util.Optional;

@Service
public class AwsCommonUtils {
    @Autowired
    private CommonUtils commonUtils;

    public Optional<JsonObject> getCluster() {
        Optional<JsonObject> cluster;
        try{
            cluster = Optional.ofNullable(new Gson().fromJson(commonUtils.getDeploymentContext().get("cluster"), JsonObject.class));
        } catch (Exception e){
            cluster = Optional.empty();
        }
        return cluster;
    }

    public String getAwsRegion() {
        return getCluster().map(x -> x.get("awsRegion").getAsString()).orElseThrow(ClusterDetailsNotFound::new);
    }

    public String getRoleArn() {
        return getCluster().map(x -> x.get("roleARN").getAsString()).orElseThrow(ClusterDetailsNotFound::new);
    }

    public String getExternalId() {
        return getCluster().map(x -> x.get("externalId").getAsString()).orElseThrow(ClusterDetailsNotFound::new);
    }

    public AwsCredentialsProvider getSTSCredentialsProvider() {
        AssumeRoleRequest request = AssumeRoleRequest.builder().roleSessionName("integration-test-session").roleArn(getRoleArn()).externalId(getExternalId()).durationSeconds(3600).build();
        return StsAssumeRoleCredentialsProvider.builder().refreshRequest(request).build();
    }

    public Ec2Client getEC2Client(){
        return Ec2Client.builder().region(Region.of(getAwsRegion())).credentialsProvider(getSTSCredentialsProvider()).build();
    }
}
