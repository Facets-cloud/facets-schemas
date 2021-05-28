package com.capillary.ops.cp.service.aws;

import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.K8sCredentials;
import com.capillary.ops.cp.repository.K8sCredentialsRepository;
import com.capillary.ops.cp.service.BaseDRService;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.batch.JobBuilder;
import io.fabric8.kubernetes.api.model.batch.JobTemplateSpec;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.volumesnapshot.client.DefaultVolumeSnapshotClient;
import io.fabric8.volumesnapshot.client.VolumeSnapshotClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BaseAwsDRService extends BaseDRService {

    @Autowired
    private K8sCredentialsRepository k8sCredentialsRepository;

    private static final Logger logger = LoggerFactory.getLogger(BaseAwsDRService.class);

    KubernetesClient getKubernetesClientForCluster(String clusterId) {
        Optional<K8sCredentials> credentialsO = k8sCredentialsRepository.findOneByClusterId(clusterId);
        K8sCredentials k8sCredentials = credentialsO.get();
        return new DefaultKubernetesClient(new ConfigBuilder().withMasterUrl(k8sCredentials.getKubernetesApiEndpoint())
                .withOauthToken(k8sCredentials.getKubernetesToken()).withTrustCerts(true).build());
    }

    VolumeSnapshotClient getVolumeSnapshotClientForCluster(String clusterId) {
        Optional<K8sCredentials> credentialsO = k8sCredentialsRepository.findOneByClusterId(clusterId);
        K8sCredentials k8sCredentials = credentialsO.get();
        return new DefaultVolumeSnapshotClient(new ConfigBuilder().withMasterUrl(k8sCredentials.getKubernetesApiEndpoint())
                .withOauthToken(k8sCredentials.getKubernetesToken()).withTrustCerts(true).build());
    }

    public boolean createJobFromCronjobInK8sCluster(AwsCluster cluster, String cronjobName) {
        KubernetesClient kubernetesClient = getKubernetesClientForCluster(cluster.getId());
        CronJob existingCronjob = kubernetesClient.batch()
                .cronjobs()
                .inNamespace("default")
                .withName(cronjobName)
                .get();

        if (existingCronjob == null) {
            logger.error("cannot take snapshot, no cronjob found with name " + cronjobName);
            return false;
        }

        JobTemplateSpec jobTemplate = existingCronjob.getSpec().getJobTemplate();

        ObjectMeta metadata = jobTemplate.getMetadata();
        String jobName = existingCronjob.getMetadata().getName() + "-manual-" + System.currentTimeMillis();
        metadata.setName(jobName);

        Job job = new JobBuilder().withSpec(jobTemplate.getSpec())
                .withMetadata(metadata)
                .build();
        kubernetesClient.batch()
                .jobs()
                .inNamespace("default")
                .create(job);

        return true;
    }
}
