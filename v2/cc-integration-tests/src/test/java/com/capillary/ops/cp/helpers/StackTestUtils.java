package com.capillary.ops.cp.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

@Component
public class StackTestUtils {

    @Value("${stack.name}")
    private String STACK_NAME = "cc-stack-crm";

    private String STACK_ROOT = "../../capillary-cloud-tf/stacks/" + STACK_NAME;


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
            sizingMetaPath = STACK_ROOT + "/" + moduleName + "/" + "sizing." + "json";
        } else {
            sizingMetaPath = STACK_ROOT + "/" + moduleName + "/" + "sizing." + instanceSizeStrategy + ".json";
        }
        JsonParser parser = new JsonParser();
        bufferedReader = new BufferedReader(new FileReader(sizingMetaPath));
        //HashMap<String, String> sizingJson = gson.fromJson(bufferedReader, HashMap.class);
        JsonObject sizingJsonObject = parser.parse(bufferedReader).getAsJsonObject();

        HashMap<String, String> podSize = gson.fromJson(sizingJsonObject.getAsJsonObject(instanceSize).toString(), HashMap.class);
        return podSize;
    }
}
