package com.capillary.ops.cp.helpers;

import com.capillary.ops.cp.exceptions.ClusterDetailsNotFound;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Optional;

@Service
public class CommonUtils {

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
}
