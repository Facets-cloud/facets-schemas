package com.capillary.ops.service;

import com.capillary.ops.bo.Environments;
import com.capillary.ops.bo.HelmInfrastructureResource;
import com.capillary.ops.repository.HelmInfrastructureRepository;
import com.google.gson.internal.LinkedTreeMap;
import hapi.chart.ChartOuterClass;
import hapi.release.InfoOuterClass;
import hapi.release.ReleaseOuterClass;
import hapi.services.tiller.Tiller.*;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.microbean.helm.ReleaseManager;
import org.microbean.helm.Tiller;
import org.microbean.helm.chart.repository.ChartRepository;
import org.microbean.helm.chart.resolver.ChartResolverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class HelmInfrastructureService {

    @Autowired
    @Qualifier("HelmChartConfig")
    private Map<String, LinkedTreeMap> helmChartConfig;

    @Autowired
    private HelmInfrastructureRepository helmInfrastructureRepository;

    @Value("${deis.kubernetes.master.url}")
    private static String KUBERNETES_MASTER_URL;

    private static String DEFAULT_NAMESPACE = "infra";

    private String getChartConfig(String type, String configKey) {
        LinkedTreeMap chartConfig = helmChartConfig.get(type);
        System.out.println("chartConfig = " + chartConfig);

        String configValue = null;
        try {
            configValue = chartConfig.get(configKey).toString();
            System.out.println("configKey = " + configKey + "; configValue = " + configValue);
        } catch (Exception e) {
            System.out.println("e = " + e);
            throw new RuntimeException();
        }

        return configValue;
    }

    public void update(HelmInfrastructureResource helmResource, Environments environment) throws IOException, URISyntaxException, ChartResolverException {
        String resourceType = helmResource.getType();

        URI chartUri = new URI(getChartConfig(helmResource.getType(), "repository"));
        ChartRepository repository = new ChartRepository("google", chartUri);

        ChartOuterClass.Chart.Builder chartBuilder = repository.resolve(resourceType, getChartConfig(helmResource.getType(), "chartVersion"));
        System.out.println("mongodb.getMetadataOrBuilder() = " + chartBuilder.getMetadataOrBuilder());

        // Move this to a getter if only release manager is needed
        DefaultKubernetesClient client = new DefaultKubernetesClient();
        Tiller tiller = new Tiller(client);
        ReleaseManager releaseManager = new ReleaseManager(tiller);

        String valueParams = new Yaml().dump(helmResource.getValueParams());

        final UpdateReleaseRequest.Builder upadteReleaseRequestBuilder = UpdateReleaseRequest.newBuilder();
        upadteReleaseRequestBuilder.setName(helmResource.getDeploymentName());
        upadteReleaseRequestBuilder.setTimeout(5000L);
        upadteReleaseRequestBuilder.setWait(true);
        upadteReleaseRequestBuilder.getValuesBuilder().setRaw(valueParams);
        upadteReleaseRequestBuilder.setResetValues(false);
        upadteReleaseRequestBuilder.setReuseValues(true);

        Future<UpdateReleaseResponse> updateFuture = releaseManager.update(upadteReleaseRequestBuilder, chartBuilder);
        try {
            final ReleaseOuterClass.Release release = updateFuture.get().getRelease();
            helmInfrastructureRepository.save(helmResource);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public ReleaseOuterClass.Release deploy(HelmInfrastructureResource helmResource, Environments environment) throws URISyntaxException, ChartResolverException, IOException {
        String resourceType = helmResource.getType();

        URI chartUri = new URI(getChartConfig(helmResource.getType(), "repository"));
        ChartRepository repository = new ChartRepository("google", chartUri);

        ChartOuterClass.Chart.Builder chartBuilder = repository.resolve(resourceType, getChartConfig(helmResource.getType(), "chartVersion"));
        System.out.println("metadata = " + chartBuilder.getMetadataOrBuilder());

        // Move this to a getter if only release manager is needed
        DefaultKubernetesClient client = new DefaultKubernetesClient();
        Tiller tiller = new Tiller(client);
        ReleaseManager releaseManager = new ReleaseManager(tiller);

        String valueParams = new Yaml().dump(helmResource.getValueParams());
        final InstallReleaseRequest.Builder requestBuilder = getReleaseRequestBuilder(valueParams);

        ReleaseOuterClass.Release release = null;
        final Future<InstallReleaseResponse> releaseFuture = releaseManager.install(requestBuilder, chartBuilder);
        try {
             release = releaseFuture.get().getRelease();
            helmInfrastructureRepository.save(helmResource);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return release;
    }

    private InstallReleaseRequest.Builder getReleaseRequestBuilder(String valueParams) {
        final InstallReleaseRequest.Builder requestBuilder = InstallReleaseRequest.newBuilder();
        requestBuilder.setTimeout(5000L);
        requestBuilder.setWait(true);
        requestBuilder.setNamespace(DEFAULT_NAMESPACE);
        requestBuilder.getValuesBuilder().setRaw(valueParams);

        return requestBuilder;
    }

    public static void main(String[] args) throws URISyntaxException, ChartResolverException, IOException, ExecutionException, InterruptedException {
        DefaultKubernetesClient client = new DefaultKubernetesClient("https://localhost:6443");
        Tiller tiller = new Tiller(client);
        ReleaseManager releaseManager = new ReleaseManager(tiller);

        GetReleaseContentRequest.Builder contentRequest = GetReleaseContentRequest.newBuilder();
        contentRequest.setName("intouch-mongodb");
        GetReleaseContentRequest contentRequest1 = contentRequest.build();
        Future<GetReleaseContentResponse> content = releaseManager.getContent(contentRequest1);
        String raw = content.get().getRelease().getConfig().getRaw();

        Yaml yaml = new Yaml();
        Map<String, Object> obj = yaml.load(raw);
        System.out.println("obj = " + obj);

        final InstallReleaseRequest.Builder requestBuilder = InstallReleaseRequest.newBuilder();
        requestBuilder.setName("intouch-mongodb");
        requestBuilder.setTimeout(300L);
        requestBuilder.setWait(true);
        requestBuilder.setNamespace(DEFAULT_NAMESPACE);

        UpdateReleaseRequest.Builder upadteReleaseRequestBuilder = UpdateReleaseRequest.newBuilder();
        upadteReleaseRequestBuilder.setName("intouch-mongodb");
        upadteReleaseRequestBuilder.setTimeout(300L);
        upadteReleaseRequestBuilder.setWait(true);

        GetReleaseStatusRequest.Builder builder = GetReleaseStatusRequest.newBuilder();
        builder.setName("intouch-mongodb");
        GetReleaseStatusRequest build = builder.build();

        Future<GetReleaseStatusResponse> status = releaseManager.getStatus(build);
        InfoOuterClass.Info info = status.get().getInfo();
        System.out.println("info = " + info);

        Map<String, Object> resourceLimits = new HashMap<>();
        resourceLimits.put("memory", "400Mi");

        Map<String, Object> resourcesObj = new HashMap<>();
        resourcesObj.put("limits", resourceLimits);

        obj.put("resources", resourcesObj);
        System.out.println("obj = " + obj);

        Map<String, Object> map = new HashMap<>();
        map.put("resources", resourcesObj);
        System.out.println("map = " + map);

        String valueParams = new Yaml().dump(map);
        upadteReleaseRequestBuilder.getValuesBuilder().setRaw(valueParams);
        upadteReleaseRequestBuilder.setResetValues(false);
        upadteReleaseRequestBuilder.setReuseValues(true);

        URI chartUri = new URI("https://kubernetes-charts.storage.googleapis.com/");
        ChartRepository repository = new ChartRepository("google", chartUri);

        ChartOuterClass.Chart.Builder chartBuilder = repository.resolve("mongodb", "4.9.0");
        Future<UpdateReleaseResponse> update = releaseManager.update(upadteReleaseRequestBuilder, chartBuilder);
        ReleaseOuterClass.Release info1 = update.get().getRelease();
        System.out.println("info1 = " + info1);

//        final ListReleasesRequest.Builder listBuilder = ListReleasesRequest.newBuilder();
//        listBuilder.setNamespace("infra");
//        ListReleasesRequest releasesRequest = listBuilder.build();
//
//        Iterator<ListReleasesResponse> responseIterator = releaseManager.list(releasesRequest);
////        responseIterator.
//        responseIterator.forEachRemaining((i) -> {
//            System.out.println("i.getReleasesList() = " + i.getReleasesList());
//        });
    }
}
