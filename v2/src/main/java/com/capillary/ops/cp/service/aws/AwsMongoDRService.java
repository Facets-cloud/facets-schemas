package com.capillary.ops.cp.service.aws;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.SnapshotInfo;
import com.capillary.ops.cp.service.DRCloudService;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.batch.JobBuilder;
import io.fabric8.kubernetes.api.model.batch.JobTemplateSpec;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.model.Filter;
import software.amazon.awssdk.services.ec2.model.Snapshot;
import software.amazon.awssdk.services.ec2.model.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AwsMongoDRService extends BaseAwsDRService implements DRCloudService {

    @Autowired
    private EC2Service ec2Service;

    private static final Logger logger = LoggerFactory.getLogger(AwsMongoDRService.class);

    @Override
    public String getResourceType() {
        return "mongo";
    }

    private SnapshotInfo toSnapshotInfo(String clusterId, Snapshot snapshot, String resourceType, String instanceName) {
        String snapshotName = getSnapshotName(snapshot);
        return new SnapshotInfo(clusterId, snapshotName, snapshot.snapshotId(), resourceType, instanceName,
                snapshot.volumeId(), snapshot.startTime(), null, snapshot.volumeSize());
    }

    private String getSnapshotName(Snapshot snapshot) {
        String snapshotName = snapshot.snapshotId();
        for (Tag tag : snapshot.tags()) {
            if (tag.key().equals("Name")) {
                snapshotName = tag.value();
                break;
            }
        }

        return snapshotName;
    }

    private List<SnapshotInfo> toSnapshotInfo(String clusterId, List<Snapshot> snapshots, String resourceType, String instanceName) {
        return snapshots.parallelStream()
                .map(snapshot -> toSnapshotInfo(clusterId, snapshot, resourceType, instanceName))
                .collect(Collectors.toList());
    }

    @Override
    public List<SnapshotInfo> listSnapshots(AbstractCluster cluster) {
        return null;
    }

    @Override
    public List<SnapshotInfo> listSnapshots(AbstractCluster cluster, String instanceName) {
        AwsCluster awsCluster = (AwsCluster) cluster;

        Filter applicationFilter = Filter.builder()
                .name("tag:applicationName")
                .values(instanceName)
                .build();
        Filter clusterFilter = Filter.builder()
                .name(String.format("tag:kubernetes.io/cluster/%s-k8s-cluster", cluster.getName()))
                .values("owned")
                .build();
        Filter pvFilter = Filter.builder()
                .name("tag:kubernetes.io/created-for/pv/name")
                .values(String.format("pv-datadir-mongo-rs-%s-mongodb-primary-0", instanceName))
                .build();

        ArrayList<Filter> filters = Lists.newArrayList(applicationFilter, clusterFilter, pvFilter);
        logger.info("fetching mongo snapshots for cluster: {}, with filters: {}", cluster.getId(), filters);

        List<Snapshot> snapshots = ec2Service.getSnapshots(awsCluster, filters);
        logger.info("fetched {} mongo snapshots for instance: {} and cluster: {}", snapshots.size(), cluster.getId(), filters);

        return toSnapshotInfo(cluster.getId(), snapshots, getResourceType(), instanceName);
    }

    @Override
    public boolean createSnapshot(AbstractCluster cluster, String resourceType, String instanceName) {
        AwsCluster awsCluster = (AwsCluster) cluster;
        String cronjobName = instanceName + "-mongo-snapshot-creation-cron-0";
        return createJobFromCronjobInK8sCluster(awsCluster, cronjobName);
    }

}

