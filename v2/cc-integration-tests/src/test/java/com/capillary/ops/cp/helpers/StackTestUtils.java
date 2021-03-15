package com.capillary.ops.cp.helpers;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class StackTestUtils {

    private String STACK_ROOT = "";

    public HashMap<String, String> getInstanceSizing(String moduleName, String instanceName) throws Exception {
        Gson gson = new Gson();
        BufferedReader bufferedReader;

        String instancesPath = STACK_ROOT + "/" + moduleName + "/instances/" + instanceName + ".json";
        bufferedReader = new BufferedReader(new FileReader(instancesPath));
        HashMap<String, String> instanceJson = gson.fromJson(bufferedReader, HashMap.class);
        String instanceSize = instanceJson.get("size");
        String instanceSizeStrategy = instanceJson.get("resourceAllocationStrategy");

        String sizingMetaPath = "";
        if (instanceSizeStrategy == null || instanceSizeStrategy.isEmpty()) {
            sizingMetaPath = STACK_ROOT + "/" + moduleName + "/" + "sizing." + ".json";
        } else {
            sizingMetaPath = STACK_ROOT + "/" + moduleName + "/" + "sizing." + instanceSizeStrategy + ".json";
        }
        bufferedReader = new BufferedReader(new FileReader(sizingMetaPath));
        HashMap<String, String> sizingJson = gson.fromJson(bufferedReader, HashMap.class);

        HashMap<String, String> podSize = gson.fromJson(sizingJson.get(instanceSize), HashMap.class);
        return podSize;
    }
}
