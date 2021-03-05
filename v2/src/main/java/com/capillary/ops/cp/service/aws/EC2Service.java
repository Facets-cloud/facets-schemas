package com.capillary.ops.cp.service.aws;

import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

import java.util.*;

@Service
public class EC2Service {

    private static final Logger logger = LoggerFactory.getLogger(EC2Service.class);

    public List<Snapshot> getSnapshots(AwsCluster cluster, Collection<Filter> filters) {
        DescribeSnapshotsRequest snapshotsRequest = DescribeSnapshotsRequest.builder()
                .filters(filters)
                .build();
        DescribeSnapshotsResponse describeSnapshotsResponse = getEc2Client(cluster).describeSnapshots(snapshotsRequest);
        if (!describeSnapshotsResponse.hasSnapshots()) {
            throw new NotFoundException("No ec2 snapshots found with the matching criteria");
        }

        List<Snapshot> snapshots = describeSnapshotsResponse.snapshots();
        logger.info("fetched {} ec2 snapshots for cluster: {}", snapshots.size(), cluster.getId());

        return snapshots;
    }

    private AwsCredentialsProvider getCredentialsProvider(AwsCluster cluster) {
        if (!StringUtils.isEmpty(cluster.getRoleARN()) && !StringUtils.isEmpty(cluster.getExternalId())) {
            String sessionName = String.format("cc-%s", UUID.randomUUID());
            return getRoleCredentialsProvider(Region.of(cluster.getAwsRegion()),
                    cluster.getRoleARN(), cluster.getExternalId(), sessionName);
        }

        return getStaticCredentialsProvider(cluster.getAccessKeyId(), cluster.getSecretAccessKey());
    }

    private Ec2Client getEc2Client(AwsCluster cluster) {
        AwsCredentialsProvider credentialsProvider = getCredentialsProvider(cluster);
        return Ec2Client.builder()
                .region(Region.of(cluster.getAwsRegion()))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    private AwsCredentialsProvider getRoleCredentialsProvider(Region region, String roleArn, String externalId,
                                                              String roleSessionName) {
        AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
                .roleArn(roleArn)
                .externalId(externalId)
                .roleSessionName(roleSessionName)
                .build();

        StsClient stsClient = StsClient.builder().region(region).build();

        return StsAssumeRoleCredentialsProvider
                .builder()
                .stsClient(stsClient)
                .refreshRequest(assumeRoleRequest)
                .asyncCredentialUpdateEnabled(true)
                .build();
    }

    private AwsCredentialsProvider getStaticCredentialsProvider(String accessKeyId, String secretAccessKey) {
        AwsBasicCredentials basicCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        return StaticCredentialsProvider.create(basicCredentials);
    }
}
