package com.capillary.ops.cp.service.aws;

import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.*;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class RDSService {

    private static final Logger logger = LoggerFactory.getLogger(RDSService.class);

    enum SnapshotType {
        manual,
        automated,
        shared
    }

    public List<DBClusterSnapshot> getSnapshots(AwsCluster cluster, String instanceName, SnapshotType type) {
        RdsClient rdsClient = getRdsClient(cluster);
        DescribeDbClusterSnapshotsRequest snapshotsRequest = DescribeDbClusterSnapshotsRequest.builder()
                .snapshotType(type.name())
                .dbClusterIdentifier(instanceName)
                .build();

        DescribeDbClusterSnapshotsResponse snapshotsResponse = rdsClient.describeDBClusterSnapshots(snapshotsRequest);
        if (!snapshotsResponse.hasDbClusterSnapshots()) {
            throw new NotFoundException("No rds  snapshots found with the matching criteria");
        }

        List<DBClusterSnapshot> dbClusterSnapshots = snapshotsResponse.dbClusterSnapshots();
        logger.info("fetched {} snapshots for cluster: {}", dbClusterSnapshots.size(), cluster.getId());

        return dbClusterSnapshots;
    }

    public List<DBClusterSnapshot> getSnapshots(AwsCluster cluster, SnapshotType type, List<Filter> filters) {
        RdsClient rdsClient = getRdsClient(cluster);
        DescribeDbClusterSnapshotsRequest snapshotsRequest = DescribeDbClusterSnapshotsRequest.builder()
                .snapshotType(type.name())
                .filters(filters)
                .build();
        return rdsClient.describeDBClusterSnapshots(snapshotsRequest).dbClusterSnapshots();
    }

    public List<DBClusterSnapshot> getSnapshots(AwsCluster cluster, SnapshotType type, Filter... filters) {
        return getSnapshots(cluster, type, Arrays.asList(filters));
    }

    public DescribeDbClusterSnapshotsResponse getSnapshotsAfterPosition(AwsCluster cluster, SnapshotType type, String marker) {
        RdsClient rdsClient = getRdsClient(cluster);
        DescribeDbClusterSnapshotsRequest.Builder snapshotsRequestBuilder = DescribeDbClusterSnapshotsRequest.builder()
                .snapshotType(type.name());
        if (marker != null) {
            snapshotsRequestBuilder.marker(marker);
        }
        DescribeDbClusterSnapshotsRequest snapshotsRequest = snapshotsRequestBuilder.build();
        return rdsClient.describeDBClusterSnapshots(snapshotsRequest);
    }

    public List<DBClusterSnapshot> getSnapshots(AwsCluster cluster, SnapshotType type) {
        DescribeDbClusterSnapshotsResponse paginatedResponse = getSnapshotsAfterPosition(cluster, type, null);
        List<DBClusterSnapshot> snapshots = new ArrayList<>(paginatedResponse.dbClusterSnapshots());
        String marker = paginatedResponse.marker();
        while (marker != null) {
            paginatedResponse = getSnapshotsAfterPosition(cluster, type, marker);
            snapshots.addAll(paginatedResponse.dbClusterSnapshots());
            marker = paginatedResponse.marker();
        }
        logger.info("fetched {} rds snapshots of type: {} for cluster: {}", snapshots.size(), type.name(), cluster.getId());
        return snapshots;
    }

    public DBClusterSnapshot createDBClusterSnapshot(AwsCluster cluster, String dbClusterId, String snapshotName, List<Tag> tags) {
        RdsClient rdsClient = getRdsClient(cluster);
        CreateDbClusterSnapshotRequest createDbClusterSnapshotRequest = CreateDbClusterSnapshotRequest.builder()
                .dbClusterIdentifier(dbClusterId)
                .dbClusterSnapshotIdentifier(snapshotName)
                .tags(tags)
                .build();
        CreateDbClusterSnapshotResponse snapshotResponse = rdsClient.createDBClusterSnapshot(createDbClusterSnapshotRequest);
        logger.info("created db cluster snapshot: {}", snapshotResponse);
        return snapshotResponse.dbClusterSnapshot();
    }

    private AwsCredentialsProvider getStaticCredentialsProvider(String accessKeyId, String secretAccessKey) {
        AwsBasicCredentials basicCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        return StaticCredentialsProvider.create(basicCredentials);
    }

    private AwsCredentialsProvider getCredentialsProvider(AwsCluster cluster) {
        if (!StringUtils.isEmpty(cluster.getRoleARN()) && !StringUtils.isEmpty(cluster.getExternalId())) {
            String sessionName = String.format("cc-%s", UUID.randomUUID());
            return getRoleCredentialsProvider(Region.of(cluster.getAwsRegion()),
                    cluster.getRoleARN(), cluster.getExternalId(), sessionName);
        }

        return getStaticCredentialsProvider(cluster.getAccessKeyId(), cluster.getSecretAccessKey());
    }

    private RdsClient getRdsClient(AwsCluster cluster) {
        AwsCredentialsProvider credentialsProvider = getCredentialsProvider(cluster);
        return RdsClient.builder()
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
}
