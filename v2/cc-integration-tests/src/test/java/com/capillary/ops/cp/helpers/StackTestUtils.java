package com.capillary.ops.cp.helpers;

import com.capillary.ops.cp.bo.ClusterVariable;
import com.capillary.ops.cp.bo.PodSize;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
@TestPropertySource(locations = "classpath:test.properties")
public class StackTestUtils {

    @Value("${stack.name}")
    private String STACK_NAME;

    private String STACK_ROOT = "../../capillary-cloud-tf/stacks/";

    public JsonObject readFile(String filePath) throws FileNotFoundException {
        JsonParser parser = new JsonParser();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        return parser.parse(bufferedReader).getAsJsonObject();
    }

    public JsonObject getInstance(String moduleName, String instanceName) throws FileNotFoundException {
        String instancesPath = STACK_ROOT + STACK_NAME + "/" + moduleName + "/instances/" + instanceName + ".json";
        return readFile(instancesPath);
    }

    public PodSize getInstanceSizing(String moduleName, String instanceName) throws Exception {
        JsonObject instance = getInstance(moduleName, instanceName);
        String instanceSize = instance.get("size").getAsString();
        String instanceSizeStrategy = instance.get("resourceAllocationStrategy") == null ? null : instance.get("resourceAllocationStrategy").getAsString();

        String sizingMetaPath = "";
        if (instanceSizeStrategy == null || instanceSizeStrategy.isEmpty()) {
            sizingMetaPath = STACK_ROOT + STACK_NAME + "/" + moduleName + "/" + "sizing." + "json";
        } else {
            sizingMetaPath = STACK_ROOT + STACK_NAME + "/" + moduleName + "/" + "sizing." + instanceSizeStrategy + ".json";
        }
        JsonObject sizingJsonObject = readFile(sizingMetaPath).getAsJsonObject(instanceSize);
        Double cpu = Double.parseDouble(sizingJsonObject.get("podCPULimit").toString());
        Double memory = Double.parseDouble(sizingJsonObject.get("podMemoryLimit").toString());
        return new PodSize(cpu, memory);
    }

    public HashMap<String, String> getStackVars() throws Exception {
        String stackDefinitionFile = STACK_ROOT + STACK_NAME + "/stack.json";
        JsonObject stackVarsObject = readFile(stackDefinitionFile).getAsJsonObject("stackVariables");
        return new Gson().fromJson(stackVarsObject.toString(), HashMap.class);
    }

    public Set<String> getClusterVars() throws Exception {
        String stackDefinitionFile = STACK_ROOT + STACK_NAME + "/stack.json";
        JsonObject clusterVarsObject = readFile(stackDefinitionFile).getAsJsonObject("clusterVariablesMeta");
        Type clusterVarsType = new TypeToken<Map<String, ClusterVariable>>() {
        }.getType();
        Map<String, ClusterVariable> envMap = new Gson().fromJson(clusterVarsObject.toString(), clusterVarsType);
        return envMap.entrySet()
                .stream()
                .filter(v -> v.getValue().getSecret() != null)
                .filter(v -> !v.getValue().getSecret())
                .collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue()))
                .keySet();
    }

    public HashMap<String,String> getInstanceEnvVariables(String moduleName, String instanceName) throws Exception {
        HashMap<String, String> envMap = new HashMap<>();
        JsonObject instance = getInstance(moduleName, instanceName);
        JsonObject staticVars = instance.getAsJsonObject("environmentVariables").getAsJsonObject("static");
        JsonObject dynamicVars = instance.getAsJsonObject("environmentVariables").getAsJsonObject("dynamic");
        envMap.putAll(new Gson().fromJson(staticVars.toString(), HashMap.class));
        envMap.putAll(new Gson().fromJson(dynamicVars.toString(), HashMap.class));
        return envMap;
    }

    public Set<String> getEnvsFromCredential(String moduleName, String instanceName) throws Exception {
        Set<String> envVars = new HashSet<>();
        JsonObject instance = getInstance(moduleName, instanceName);
        JsonArray envArray = new JsonArray();
        JsonArray mysqlArray = instance.getAsJsonObject("credentialRequests")
                .getAsJsonObject("dbs")
                .getAsJsonArray("mysql");

        JsonArray mongoArray = instance.getAsJsonObject("credentialRequests")
                .getAsJsonObject("dbs")
                .getAsJsonArray("mongo");


        JsonArray rmqArray = instance.getAsJsonObject("credentialRequests")
                .getAsJsonObject("queues")
                .getAsJsonArray("rabbitmq");

        envVars.addAll(getEnvsFromResourceJsonArray(mysqlArray));
        envVars.addAll(getEnvsFromResourceJsonArray(mongoArray));
        envVars.addAll(getEnvsFromResourceJsonArray(rmqArray));

        return envVars;
    }

    public Set<String> getEnvsFromResourceJsonArray(JsonArray credentialReqArrayOfResource){
        Set<String> envVars = new HashSet<>();
        for(JsonElement ele : credentialReqArrayOfResource){
            JsonArray envVarArray = ele.getAsJsonObject().getAsJsonArray("environmentVariables");
            for(JsonElement env: envVarArray){
                envVars.add(env.getAsJsonObject().get("userName").getAsString());
                envVars.add(env.getAsJsonObject().get("password").getAsString());
            }
        }
        return envVars;
    }

}
