package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.*;
import com.capillary.ops.deployer.service.helm.HelmValueProviderFactory;
import com.capillary.ops.deployer.repository.EnvironmentRepository;
import com.capillary.ops.deployer.service.interfaces.IHelmService;
import hapi.chart.ChartOuterClass.Chart;
import hapi.release.ReleaseOuterClass.Release;
import hapi.services.tiller.Tiller.*;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.commons.lang3.StringUtils;
import org.microbean.helm.ReleaseManager;
import org.microbean.helm.Tiller;
import org.microbean.helm.chart.DirectoryChartLoader;
import org.microbean.helm.chart.URLChartLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Future;

@Service
@Profile("!dev | helminttest")
public class HelmService implements IHelmService {

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private HelmValueProviderFactory helmValueProviderFactory;

    private static final Logger logger = LoggerFactory.getLogger(HelmService.class);

    @Override
    public void deploy(Environment environment, String releaseName, String chartName, Map<String, Object> valueMap) {
        logger.info("Installing " + chartName + " with values " + valueMap.toString());
        ReleaseManager releaseManager = getReleaseManager(environment);
        Iterator<ListReleasesResponse> listReleasesResponseIterator =
                releaseManager.list(ListReleasesRequest.newBuilder().setFilter("^" + releaseName + "$").build());

        try {
            if (listReleasesResponseIterator.hasNext() && listReleasesResponseIterator.next().getReleasesCount() > 0) {
                releaseManager.close();
                upgrade(environment, releaseName, chartName, valueMap);
            } else {
                releaseManager.close();
                install(environment, releaseName, chartName, valueMap);
            }
        } catch (Exception e) {
            logger.error("error happened while trying to install or upgrade application", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deploy(Application application, Deployment deployment) {
        ApplicationFamily applicationFamily = application.getApplicationFamily();
        String environmentName = deployment.getEnvironment();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        String applicationName = application.getName();

        logger.info("deploying application: {}, for environment: {}", applicationName, environmentName);
        ReleaseManager releaseManager = getReleaseManager(environment);
        logger.debug("fetching the helm release manager");
        String releaseName = getReleaseName(application, environment);
        logger.info("deploying with release name: {}", releaseName);
        Iterator<ListReleasesResponse> listReleasesResponseIterator =
                releaseManager.list(ListReleasesRequest.newBuilder().setFilter("^" + releaseName + "$").build());

        try {
            if (listReleasesResponseIterator.hasNext() && listReleasesResponseIterator.next().getReleasesCount() > 0) {
                logger.info("application {} already exists, upgrading", applicationName);
                releaseManager.close();
                upgrade(application, deployment);
            } else {
                logger.info("deploying application {} for the first time", applicationName);
                releaseManager.close();
                install(application, deployment);
            }
        } catch (Exception e) {
            logger.error("got error while deploying application: {}", applicationName, e);
            throw new RuntimeException(e);
        }
    }

    public boolean doesReleaseExist(ApplicationFamily applicationFamily, Environment environment, String releaseName) {
        ReleaseManager releaseManager = getReleaseManager(environment);
        Iterator<ListReleasesResponse> responseIterator = releaseManager.list(ListReleasesRequest.newBuilder()
                .setFilter("^" + releaseName + "$")
                .setNamespace("default")
                .build());
        boolean ret = responseIterator.hasNext();
        try {
            releaseManager.close();
        } catch (IOException e) {
            logger.error("error closing releasemanager", e);
        }
        return ret;
    }

    @Override
    public void purge(Application application, Environment environment) {
        try {
            getReleaseManager(environment)
                    .uninstall(UninstallReleaseRequest.newBuilder()
                            .setName(getReleaseName(application, environment)).build());
        } catch (Throwable e) {
            logger.warn("Exception uninstalling", e);
        }
    }

    @Override
    public String getReleaseName(Application application, Environment environment) {
        return StringUtils.isEmpty(environment.getEnvironmentConfiguration().getNodeGroup()) ?
                application.getName() :
                environment.getEnvironmentMetaData().getName() + "-" + application.getName();
    }

    private void install(Environment environment, String releaseName, String chartName, Map<String, Object> valueMap) throws Exception {
        URLChartLoader chartLoader = new URLChartLoader();
        Chart.Builder chart = chartLoader.load(this.getClass().getResource("/charts/" + chartName));
        ReleaseManager releaseManager = getReleaseManager(environment);
        String valuesYaml = new Yaml().dump(valueMap);

        final InstallReleaseRequest.Builder requestBuilder = InstallReleaseRequest.newBuilder();
        requestBuilder.setTimeout(300L);
        requestBuilder.setName(releaseName); // Set the Helm release name
        requestBuilder.setWait(false); // Wait for Pods to be ready
        requestBuilder.getValuesBuilder().setRaw(valuesYaml);

        final Future<InstallReleaseResponse> releaseFuture = releaseManager.install(requestBuilder, chart);
        final Release release = releaseFuture.get().getRelease();

        releaseManager.close();
    }

    private String getChartName(Application application, Deployment deployment) {
        if (application.getApplicationType().equals(Application.ApplicationType.STATEFUL_SET)) {
            return "capillary-base-statefulset";
        } else if (application.getApplicationType().equals(Application.ApplicationType.SCHEDULED_JOB)) {
            return "capillary-base-cronjob";
        }

        return "capillary-base";
    }

    private boolean doesPvcExist(Application application) {
        if (application.getPvcList() != null && application.getPvcList().size() > 0) {
            return true;
        }
        return false;
    }

    private void install(Application application, Deployment deployment) throws Exception {
        ApplicationFamily applicationFamily = application.getApplicationFamily();
        String environmentName = deployment.getEnvironment();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        String chartName = getChartName(application, deployment);
        Map<String, Object> valueMap = helmValueProviderFactory.getValues(application, environment, deployment);
        String releaseName = getReleaseName(application, environment);
        install(environment, releaseName, chartName, valueMap);
    }

    private void upgrade(Environment environment, String releaseName, String chartName, Map<String, Object> valueMap) throws Exception {
        URLChartLoader chartLoader = new URLChartLoader();
        Chart.Builder chart = chartLoader.load(this.getClass().getResource("/charts/" + chartName));
        ReleaseManager releaseManager = getReleaseManager(environment);
        String valuesYaml = new Yaml().dump(valueMap);

        final UpdateReleaseRequest.Builder requestBuilder = UpdateReleaseRequest.newBuilder();
        requestBuilder.setTimeout(300L);
        requestBuilder.setName(releaseName); // Set the Helm release name
        requestBuilder.setWait(false); // Wait for Pods to be ready
        requestBuilder.getValuesBuilder().setRaw(valuesYaml);
        requestBuilder.setForce(true);

        final Future<UpdateReleaseResponse> releaseFuture = releaseManager.update(requestBuilder, chart);
        final Release release = releaseFuture.get().getRelease();

        releaseManager.close();
    }

    private void upgrade(Application application, Deployment deployment) throws Exception {
        ApplicationFamily applicationFamily = application.getApplicationFamily();
        String environmentName = deployment.getEnvironment();
        Environment environment = environmentRepository.findOneByEnvironmentMetaDataApplicationFamilyAndEnvironmentMetaDataName(applicationFamily, environmentName).get();
        String chartName = getChartName(application, deployment);
        Map<String, Object> valueMap = helmValueProviderFactory.getValues(application, environment, deployment);
        String releaseName = getReleaseName(application, environment);
        upgrade(environment, releaseName, chartName, valueMap);
    }

    private ReleaseManager getReleaseManager(Environment environment) {
        DefaultKubernetesClient kubernetesClient = new DefaultKubernetesClient(
                new ConfigBuilder()
                        .withMasterUrl(environment.getEnvironmentConfiguration().getKubernetesApiEndpoint())
                        .withOauthToken(environment.getEnvironmentConfiguration().getKubernetesToken())
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

    @Override
    public void rollback(Application application, Environment environment) {
        ReleaseManager releaseManager = getReleaseManager(environment);
        String releaseName = getReleaseName(application,environment);
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