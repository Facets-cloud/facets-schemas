package com.capillary.ops.cp.helpers;

import com.capillary.ops.cp.bo.AdditionalPeering;
import com.capillary.ops.cp.exceptions.ClusterDetailsNotFound;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommonUtils {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    @Value("${stack.name}")
    private String stackName;

    private final String DEPLOYMENT_CONTEXT_PATH = "../../capillary-cloud-tf/deploymentcontext.json";

    public JsonObject getDeploymentContext() throws Exception {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(DEPLOYMENT_CONTEXT_PATH));
        return parser.parse(bufferedReader).getAsJsonObject();
    }

    public Optional<JsonObject> getCluster() {
        Optional<JsonObject> cluster;
        try {
            cluster = Optional.ofNullable(new Gson().fromJson(getDeploymentContext().get("cluster"), JsonObject.class));
        } catch (Exception e) {
            cluster = Optional.empty();
        }
        return cluster;
    }

    public String getClusterName() {
        return getCluster().map(x -> x.get("name").getAsString()).orElseThrow(ClusterDetailsNotFound::new);
    }

    public String getStackName() {
        return getCluster().map(x -> x.get("stackName").getAsString()).orElseThrow(ClusterDetailsNotFound::new);
    }

    public String getAwsRegion() {
        return getCluster().map(x -> x.get("awsRegion").getAsString()).orElseThrow(ClusterDetailsNotFound::new);
    }

    public String getRoleArn() {
        return getCluster().map(x -> x.get("roleARN").getAsString()).orElseThrow(ClusterDetailsNotFound::new);
    }

    public String getExternalId() {
        return getCluster().map(x -> x.get("externalId").getAsString()).orElseThrow(ClusterDetailsNotFound::new);
    }

    public List<AdditionalPeering> getAdditionalPeerings() {
        String additionalPeeringsPath = String.format("../../capillary-cloud-tf/stacks/%s/additional_peering/instances/", stackName);

        File fileDir = new File(additionalPeeringsPath);

        File[] listFiles = fileDir.listFiles();

        List<AdditionalPeering> additionalPeerings = new ArrayList<>();

        if (listFiles != null){
            ObjectMapper mapper = new ObjectMapper();

            List<Optional<AdditionalPeering>> optionalPeerings = Arrays.stream(listFiles).map(x -> {
                Optional<AdditionalPeering> peering;
                try {
                    peering = Optional.ofNullable(mapper.readValue(x, AdditionalPeering.class));
                } catch (IOException e) {
                    peering = Optional.empty();
                }
                return peering;
            }).collect(Collectors.toList());

            additionalPeerings.addAll(optionalPeerings.stream().filter(x -> x.isPresent() && !x.get().getDisabled()).map(Optional::get).collect(Collectors.toList()));

        }

        return additionalPeerings;
    }

    public List<String> getArtifactories() {
        List<String> artifactories = new ArrayList<>();
        try {
            getDeploymentContext().getAsJsonArray("artifactoryDetails").forEach(x -> artifactories.add(new Gson().fromJson(x, JsonObject.class).get("name").getAsString()));
        } catch (Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }

        return artifactories;
    }

    public String getK8sClusterVersion() {
        return getCluster().map(x -> x.get("componentVersions").getAsJsonObject().get("KUBERNETES").getAsString()).orElseThrow(ClusterDetailsNotFound::new);
    }
}
