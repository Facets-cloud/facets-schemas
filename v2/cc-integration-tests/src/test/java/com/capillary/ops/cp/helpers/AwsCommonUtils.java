package com.capillary.ops.cp.helpers;

import com.capillary.ops.cp.exceptions.ClusterDetailsNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

import java.util.ArrayList;
import java.util.List;

@Service
public class AwsCommonUtils {

    @Autowired
    private CommonUtils commonUtils;

    public AwsCredentialsProvider getSTSCredentialsProvider() {
        AssumeRoleRequest request = AssumeRoleRequest.builder()
                .roleSessionName("integration-test-session")
                .roleArn(commonUtils.getRoleArn())
                .externalId(commonUtils.getExternalId())
                .durationSeconds(3600)
                .build();

        StsClient stsClient = StsClient.builder().region(Region.of(commonUtils.getAwsRegion())).build();

        return StsAssumeRoleCredentialsProvider
                .builder()
                .stsClient(stsClient)
                .refreshRequest(request)
                .asyncCredentialUpdateEnabled(true)
                .build();
    }

    public Ec2Client getEC2Client() {
        return Ec2Client.builder()
                .region(Region.of(getAwsRegion()))
                .credentialsProvider(getSTSCredentialsProvider())
                .build();
    }

    public AutoScalingClient getAutoScalingGroupsClient() {
        return AutoScalingClient.builder().region(Region.of(getAwsRegion())).credentialsProvider(getSTSCredentialsProvider()).build();
    }

    public String getAwsRegion() {
        return commonUtils.getCluster().map(x -> x.get("awsRegion").getAsString()).orElseThrow(ClusterDetailsNotFound::new);
    }

    public String getRoleArn() {
        return commonUtils.getCluster().map(x -> x.get("roleARN").getAsString()).orElseThrow(ClusterDetailsNotFound::new);
    }

    public String getExternalId() {
        return commonUtils.getCluster().map(x -> x.get("externalId").getAsString()).orElseThrow(ClusterDetailsNotFound::new);
    }

    public String getVpcCIDR() {
        return commonUtils.getCluster().map(x -> x.get("vpcCIDR").getAsString()).orElseThrow(ClusterDetailsNotFound::new);
    }

    public List<String> getSpotInstanceTypes() {
        List<String> instanceTypes = new ArrayList<>();

        commonUtils.getCluster().map(x -> x.getAsJsonArray("instanceTypes")).orElseThrow(ClusterDetailsNotFound::new).forEach(x -> instanceTypes.add(x.getAsString()));

        return instanceTypes;
    }

    // TODO can create an inheritence tree amongst commonutils and aws common utils
    // TODO and can move the below method to common place.

    public String getClusterName() {
        return commonUtils.getCluster().map(x -> x.get("name").getAsString()).orElseThrow(ClusterDetailsNotFound::new);
    }
}
