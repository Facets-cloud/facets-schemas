package com.capillary.ops.service.impl;

import com.capillary.ops.bo.AbstractDeploymentResource;
import com.capillary.ops.bo.HelmInfrastructureResource;
import com.capillary.ops.repository.HelmInfrastructureRepository;
import com.capillary.ops.service.HelmDeploymentService;
import com.google.gson.internal.LinkedTreeMap;
import hapi.chart.ChartOuterClass;
import hapi.release.ReleaseOuterClass;
import hapi.services.tiller.Tiller.InstallReleaseRequest;
import hapi.services.tiller.Tiller.InstallReleaseResponse;
import hapi.services.tiller.Tiller.UpdateReleaseRequest;
import hapi.services.tiller.Tiller.UpdateReleaseResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.microbean.helm.ReleaseManager;
import org.microbean.helm.chart.repository.ChartRepository;
import org.microbean.helm.chart.resolver.ChartResolverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

@Service
public class HelmDeploymentServiceImpl implements HelmDeploymentService {

  @Autowired
  @Qualifier("HelmChartConfig")
  private Map<String, LinkedTreeMap> helmChartConfig;

  @Autowired private HelmInfrastructureRepository helmInfrastructureRepository;

  @Autowired
  @Qualifier(value = "HelmReleaseManager")
  private ReleaseManager releaseManager;

  private static String DEFAULT_NAMESPACE = "infra";

  private String getChartConfig(String type, String configKey) {
    LinkedTreeMap chartConfig = helmChartConfig.get(type);
    System.out.println("chartConfig = " + chartConfig);

    String configValue;
    try {
      configValue = chartConfig.get(configKey).toString();
      System.out.println("configKey = " + configKey + "; configValue = " + configValue);
    } catch (Exception e) {
      throw new RuntimeException("error happened while getting helm chart config", e);
    }

    return configValue;
  }

  @Override
  public ReleaseOuterClass.Release update(AbstractDeploymentResource deploymentResource)
      throws IOException {
    HelmInfrastructureResource helmResource = (HelmInfrastructureResource) deploymentResource;
    String resourceType = helmResource.getType();

    ChartOuterClass.Chart.Builder chartBuilder = getHelmChartBuilder(helmResource, resourceType);

    String valueParams = new Yaml().dump(helmResource.getValueParams());
    final UpdateReleaseRequest.Builder upadteReleaseRequestBuilder =
        getUpdateReleaseBuilder(helmResource, valueParams);

    Future<UpdateReleaseResponse> updateFuture =
        releaseManager.update(upadteReleaseRequestBuilder, chartBuilder);
    final ReleaseOuterClass.Release release;
    try {
      release = updateFuture.get().getRelease();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException("error happened while updating helm release", e);
    }

    return release;
  }

  private UpdateReleaseRequest.Builder getUpdateReleaseBuilder(
      HelmInfrastructureResource helmResource, String valueParams) {
    final UpdateReleaseRequest.Builder upadteReleaseRequestBuilder =
        UpdateReleaseRequest.newBuilder();
    upadteReleaseRequestBuilder.setName(helmResource.getDeploymentName());
    upadteReleaseRequestBuilder.setTimeout(5000L);
    upadteReleaseRequestBuilder.setWait(true);
    upadteReleaseRequestBuilder.getValuesBuilder().setRaw(valueParams);
    upadteReleaseRequestBuilder.setResetValues(false);
    upadteReleaseRequestBuilder.setReuseValues(true);
    return upadteReleaseRequestBuilder;
  }

  @Override
  public ReleaseOuterClass.Release deploy(AbstractDeploymentResource deploymentResource)
      throws IOException {
    System.out.println("going to deploy helm resource: " + deploymentResource.getResourceName());

    HelmInfrastructureResource helmResource = (HelmInfrastructureResource) deploymentResource;
    String resourceType = helmResource.getType();

    String valueParams = new Yaml().dump(helmResource.getValueParams());
    final InstallReleaseRequest.Builder requestBuilder = getReleaseRequestBuilder(valueParams);

    ChartOuterClass.Chart.Builder chartBuilder = getHelmChartBuilder(helmResource, resourceType);

    ReleaseOuterClass.Release release = null;
    final Future<InstallReleaseResponse> releaseFuture =
        releaseManager.install(requestBuilder, chartBuilder);
    try {
      release = releaseFuture.get().getRelease();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    return release;
  }

  private ChartOuterClass.Chart.Builder getHelmChartBuilder(
      HelmInfrastructureResource helmResource, String resourceType) {
    ChartRepository repository = getChartRepository(helmResource);

    ChartOuterClass.Chart.Builder chartBuilder =
        getChartBuilder(helmResource, resourceType, repository);
    System.out.println("metadata = " + chartBuilder.getMetadataOrBuilder());

    return chartBuilder;
  }

  private ChartOuterClass.Chart.Builder getChartBuilder(
      HelmInfrastructureResource helmResource, String resourceType, ChartRepository repository) {
    ChartOuterClass.Chart.Builder chartBuilder;
    String chartVersion = getChartConfig(helmResource.getType(), "chartVersion");

    try {
      chartBuilder = repository.resolve(resourceType, chartVersion);
    } catch (ChartResolverException e) {
      e.printStackTrace();
      throw new RuntimeException("cannot resolve helm chart for version: " + chartVersion);
    }

    return chartBuilder;
  }

  private ChartRepository getChartRepository(HelmInfrastructureResource helmResource) {
    URI chartUri;
    String repositoryUrl = getChartConfig(helmResource.getType(), "repository");

    try {
      chartUri = new URI(repositoryUrl);
    } catch (URISyntaxException e) {
      e.printStackTrace();
      throw new RuntimeException("cannot resolve helm chart url: " + repositoryUrl);
    }

    return new ChartRepository("google", chartUri, true);
  }

  private InstallReleaseRequest.Builder getReleaseRequestBuilder(String valueParams) {
    final InstallReleaseRequest.Builder requestBuilder = InstallReleaseRequest.newBuilder();
    requestBuilder.setTimeout(5000L);
    requestBuilder.setWait(true);
    requestBuilder.setNamespace(DEFAULT_NAMESPACE);
    requestBuilder.getValuesBuilder().setRaw(valueParams);

    return requestBuilder;
  }
}
