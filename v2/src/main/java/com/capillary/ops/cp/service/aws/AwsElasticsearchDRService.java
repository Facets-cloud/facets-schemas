package com.capillary.ops.cp.service.aws;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.SnapshotInfo;
import com.capillary.ops.cp.repository.K8sCredentialsRepository;
import com.capillary.ops.cp.service.DRCloudService;
import com.capillary.ops.deployer.exceptions.NotImplementedException;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.LocalPortForward;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class AwsElasticsearchDRService extends BaseAwsDRService implements DRCloudService {

    @Autowired
    private K8sCredentialsRepository k8sCredentialsRepository;

    private static final Logger logger = LoggerFactory.getLogger(AwsElasticsearchDRService.class);

    private static final int ELASTICSEARCH_PORT = 9200;

    private static final String ELASTICSEARCH_DR_ENDPOINT = "http://127.0.0.1:%s/_cat/snapshots/dr_s3_repository?format=json";

    @Override
    public String getResourceType() {
        return "elasticsearch";
    }

    private SnapshotInfo toSnapshotInfo(String clusterId, JSONObject snapshot, String instanceName) throws Exception {
        try {
            String id = snapshot.getString("id");
            long startEpoch = Long.parseLong(snapshot.getString("start_epoch"));
            long endEpoch = Long.parseLong(snapshot.getString("end_epoch"));
            return new SnapshotInfo(clusterId, id, id, getResourceType(), instanceName, instanceName,
                    Instant.ofEpochSecond(startEpoch), Instant.ofEpochSecond(endEpoch), null);
        } catch (Exception ex) {
            logger.error("error happened while converting snapshot to snapshot info: {}", snapshot);
            throw new Exception("error happened while converting snapshot to snapshot info");
        }
    }

    private List<SnapshotInfo> toSnapshotInfo(String clusterId, JSONArray snapshots, String instanceName) throws Exception {
        List<SnapshotInfo> snapshotInfos = new ArrayList<>();
        for (Object snapshot : snapshots) {
            JSONObject snap = (JSONObject) snapshot;
            if (snap.has("status") && snap.getString("status").equals("SUCCESS")) {
                snapshotInfos.add(toSnapshotInfo(clusterId, snap, instanceName));
            }
        }

        return snapshotInfos;
    }

    private JSONArray getSnapshotsFromK8sCluster(AwsCluster cluster, String instanceName) throws Exception {
        KubernetesClient client = getKubernetesClientForCluster(cluster.getId());
        LocalPortForward portForward = client.services()
                .inNamespace("default")
                .withName(instanceName + "-master")
                .portForward(ELASTICSEARCH_PORT);

        logger.info("port forward to elasticsearch service {}-master successful", instanceName);

        String endpoint = String.format(ELASTICSEARCH_DR_ENDPOINT, portForward.getLocalPort());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("error happened while listing snapshots for elasticsearch instance: " + instanceName);
        }

        logger.info("fetched elasticsearch snapshots for instance: {}", instanceName);

        portForward.close();
        return new JSONArray(response.getBody());
    }

    @Override
    public List<SnapshotInfo> listSnapshots(AbstractCluster cluster) {
        throw new NotImplementedException("List snapshots for all ES instances is not implemented yet");
    }

    @Override
    public List<SnapshotInfo> listSnapshots(AbstractCluster cluster, String instanceName) {
        AwsCluster awsCluster = (AwsCluster) cluster;

        try {
            JSONArray snapshots = getSnapshotsFromK8sCluster(awsCluster, instanceName);
            List<SnapshotInfo> snapshotInfos = toSnapshotInfo(cluster.getId(), snapshots, instanceName);
            logger.info("fetched {} elasticsearch snapshots for instance: {} and cluster: {}", snapshotInfos.size(),
                    instanceName, cluster.getId());
            return snapshotInfos;
        } catch (Exception e) {
            logger.error("error happened while fetching snapshots for elasticsearch instance: {}", instanceName);
            logger.error("error", e);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean createSnapshot(AbstractCluster cluster, String resourceType, String instanceName) {
        AwsCluster awsCluster = (AwsCluster) cluster;
        String cronjobName = instanceName + "-es-create-snap-cron-0";
        return createJobFromCronjobInK8sCluster(awsCluster, cronjobName);
    }
}
