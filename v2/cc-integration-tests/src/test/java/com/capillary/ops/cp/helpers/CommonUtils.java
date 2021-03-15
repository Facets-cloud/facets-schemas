package com.capillary.ops.cp.helpers;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class CommonUtils {

    private final String DEPLOYMENT_CONTEXT_PATH = "../capillary-cloud-tf/deploymentcontext.json";

    public HashMap<String, String> getDeploymentContext() throws Exception {
        Gson gson = new Gson();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(DEPLOYMENT_CONTEXT_PATH));
        HashMap<String, String> rawJson = gson.fromJson(bufferedReader, HashMap.class);
        return rawJson;
    }
}
