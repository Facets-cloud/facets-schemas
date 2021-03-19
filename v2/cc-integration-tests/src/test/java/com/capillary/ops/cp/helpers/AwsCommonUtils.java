package com.capillary.ops.cp.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

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
        return StsAssumeRoleCredentialsProvider.builder().refreshRequest(request).build();
    }

    public Ec2Client getEC2Client() {
        return Ec2Client.builder()
                .region(Region.of(commonUtils.getAwsRegion()))
                .credentialsProvider(getSTSCredentialsProvider())
                .build();
    }
}
