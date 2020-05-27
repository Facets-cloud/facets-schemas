package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.K8sCredentials;
import com.capillary.ops.cp.repository.K8sCredentialsRepository;
import hapi.release.ReleaseOuterClass.Release;
import hapi.services.tiller.Tiller.*;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.microbean.helm.ReleaseManager;
import org.microbean.helm.Tiller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.Future;

@Service
public class CCHelmService {

    @Autowired
    private K8sCredentials k8sCredentials;

    @Autowired
    private K8sCredentialsRepository k8sCredentialsRepository;

    private static final Logger logger = LoggerFactory.getLogger(CCHelmService.class);

    private ReleaseManager getReleaseManager(String clusterId) {
        Optional<K8sCredentials> credentials = k8sCredentialsRepository.findOneByClusterId(clusterId);
        K8sCredentials k8sCredentials = credentials.get();
        DefaultKubernetesClient kubernetesClient = new DefaultKubernetesClient(
                new ConfigBuilder()
                        .withMasterUrl(k8sCredentials.getKubernetesApiEndpoint())
                        .withOauthToken(k8sCredentials.getKubernetesToken())
                        .withClientCertData(null)
                        .withCaCertData(null)
                        .withClientKeyData(null)
                        .withUsername(null)
                        .withPassword(null)
                        .withTrustCerts(true)
                        .withWebsocketTimeout(60*1000)
                        .withConnectionTimeout(30*1000)
                        .withRequestTimeout(30*1000)
                        .build());
        try {
            ReleaseManager releaseManager = new ReleaseManager(new Tiller(kubernetesClient));
            return releaseManager;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rollback(String clusterId, String moduleName) {
        ReleaseManager releaseManager = getReleaseManager(clusterId);
        String releaseName = moduleName;
        Iterator<ListReleasesResponse> listReleasesResponseIterator =
                releaseManager.list(ListReleasesRequest.newBuilder().setFilter("^" + releaseName + "$").build());
        int releaseStatus = listReleasesResponseIterator.next().getReleasesCount();

        try {
            if (releaseStatus > 0) {
                logger.info("rollback {} to previous release",releaseName);
                final RollbackReleaseRequest.Builder rollbackRequest = RollbackReleaseRequest.newBuilder();
                rollbackRequest.setName(releaseName);
                rollbackRequest.setTimeout(300L);
                rollbackRequest.setWait(false);
                rollbackRequest.setRecreate(true);
                rollbackRequest.setVersion(0); // 0 -> Rollback to previous revision
                final Future<RollbackReleaseResponse> rollbackFuture = releaseManager.rollback(rollbackRequest.build());
                final Release release = rollbackFuture.get().getRelease();
                releaseManager.close();
            } else {
                releaseManager.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}