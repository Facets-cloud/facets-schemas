package com.capillary.ops.cp.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;

@Service
public class CommonUtils {

    private final String DEPLOYMENT_CONTEXT_PATH = "../../capillary-cloud-tf/deploymentcontext.json";

    public JsonObject getDeploymentContext() throws Exception {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(DEPLOYMENT_CONTEXT_PATH));
        return parser.parse(bufferedReader).getAsJsonObject();
    }
}
