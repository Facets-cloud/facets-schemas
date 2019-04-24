package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Deployment;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.bo.Port;
import hapi.chart.ChartOuterClass;
import hapi.chart.ChartOuterClass.Chart;
import hapi.release.ReleaseOuterClass;
import hapi.release.ReleaseOuterClass.Release;
import hapi.services.tiller.Tiller.*;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.microbean.helm.ReleaseManager;
import org.microbean.helm.Tiller;
import org.microbean.helm.chart.DirectoryChartLoader;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class HelmService {
    public void deploy(Application application, Deployment deployment) {
        Environment environment = application.getApplicationFamily().getEnvironment(deployment.getEnvironment());
        ReleaseManager releaseManager = getReleaseManager(environment);
        String releaseName = environment.getName() + "-" + application.getName();
        Iterator<ListReleasesResponse> listReleasesResponseIterator =
                releaseManager.list(ListReleasesRequest.newBuilder().setFilter("^" + releaseName + "$").build());

        try {
            if (listReleasesResponseIterator.hasNext() && listReleasesResponseIterator.next().getReleasesCount() > 0) {
                releaseManager.close();
                upgrade(application, deployment);
            } else {
                releaseManager.close();
                install(application, deployment);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void install(Application application, Deployment deployment) throws Exception {
        DirectoryChartLoader chartLoader = new DirectoryChartLoader();
        Chart.Builder chart = chartLoader.load(Paths.get("/charts/capillary-base"));
        Environment environment = application.getApplicationFamily().getEnvironment(deployment.getEnvironment());
        ReleaseManager releaseManager = getReleaseManager(environment);
        String valuesYaml = getValuesYaml(application, deployment);
        String releaseName = environment.getName() + "-" + application.getName();
        final InstallReleaseRequest.Builder requestBuilder = InstallReleaseRequest.newBuilder();
        requestBuilder.setTimeout(300L);
        requestBuilder.setName(releaseName); // Set the Helm release name
        requestBuilder.setWait(true); // Wait for Pods to be ready
        requestBuilder.getValuesBuilder().setRaw(valuesYaml);
        final Future<InstallReleaseResponse> releaseFuture = releaseManager.install(requestBuilder, chart);
        final Release release = releaseFuture.get().getRelease();
        releaseManager.close();
    }

    private void upgrade(Application application, Deployment deployment) throws Exception {
        DirectoryChartLoader chartLoader = new DirectoryChartLoader();
        Chart.Builder chart = chartLoader.load(Paths.get("/charts/capillary-base"));
        Environment environment = application.getApplicationFamily().getEnvironment(deployment.getEnvironment());
        ReleaseManager releaseManager = getReleaseManager(environment);
        String valuesYaml = getValuesYaml(application, deployment);
        String releaseName = environment.getName() + "-" + application.getName();
        final UpdateReleaseRequest.Builder requestBuilder = UpdateReleaseRequest.newBuilder();
        requestBuilder.setTimeout(300L);
        requestBuilder.setName(releaseName); // Set the Helm release name
        requestBuilder.setWait(true); // Wait for Pods to be ready
        requestBuilder.getValuesBuilder().setRaw(valuesYaml);
        final Future<UpdateReleaseResponse> releaseFuture = releaseManager.update(requestBuilder, chart);
        final Release release = releaseFuture.get().getRelease();
        releaseManager.close();
    }

    private String getValuesYaml(Application application, Deployment deployment) {
        final Map<String, Object> yaml = new LinkedHashMap<>();
        yaml.put("image", deployment.getImage());
        yaml.put("podCPULimit",deployment.getPodSize().getCpu());
        yaml.put("podMemoryLimit",deployment.getPodSize().getMemory());
        String nodeGroup = application.getApplicationFamily().getEnvironment(deployment.getEnvironment()).getNodeGroup();
        if(!nodeGroup.isEmpty()) {
            yaml.put("nodeSelector", nodeGroup);
        }
        yaml.put("lbType", application.getLoadBalancerType().name().toLowerCase());
        List<Map<String, Object>> ports = application.getPorts().stream().map(this::getPortMap).collect(Collectors.toList());
        yaml.put("ports", ports);
        yaml.put("configurations", deployment.getConfigurations());
        final String yamlString = new Yaml().dump(yaml);
        return yamlString;
    }

    private Map<String, Object> getPortMap(Port port) {
        final Map<String, Object> out = new LinkedHashMap<>();
        out.put("name", port.getName());
        out.put("containerPort", port.getContainerPort());
        out.put("lbPort", port.getLbPort());
        return out;
    }
    private ReleaseManager getReleaseManager(Environment environment) {
        DefaultKubernetesClient kubernetesClient = new DefaultKubernetesClient(
                new ConfigBuilder()
                        .withMasterUrl(environment.getKubernetesApiEndpoint())
                        .withOauthToken(environment.getKubernetesToken())
                        .withTrustCerts(true)
                        .build());
        try {
            ReleaseManager releaseManager = new ReleaseManager(new Tiller(kubernetesClient));
            return releaseManager;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}