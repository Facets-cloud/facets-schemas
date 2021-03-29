package com.capillary.ops.cp.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import springfox.documentation.spring.web.json.Json;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Set;

@Component
@TestPropertySource(locations="classpath:test.properties")
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

    public JsonObject getInstanceSizing(String moduleName, String instanceName) throws Exception {
        JsonObject instance = getInstance(moduleName, instanceName);
        String instanceSize = instance.getAsJsonObject("size").toString();
        String instanceSizeStrategy = instance.getAsJsonObject("resourceAllocationStrategy").toString();

        String sizingMetaPath = "";
        if (instanceSizeStrategy == null || instanceSizeStrategy.isEmpty()) {
            sizingMetaPath = STACK_ROOT + STACK_NAME + "/" + moduleName + "/" + "sizing." + "json";
        } else {
            sizingMetaPath = STACK_ROOT + STACK_NAME + "/" + moduleName + "/" + "sizing." + instanceSizeStrategy + ".json";
        }
        JsonObject sizingJsonObject = readFile(sizingMetaPath);
        return sizingJsonObject.getAsJsonObject(instanceSize);
    }

    public HashMap getStackVars() throws Exception {
        String stackDefinitionFile = STACK_ROOT + STACK_NAME + "/stack.json";
        JsonObject stackVarsObject = readFile(stackDefinitionFile).getAsJsonObject("stackVariables");
        return new Gson().fromJson(stackVarsObject.toString(),HashMap.class);
    }

    public Set getClusterVars() throws Exception {
        String stackDefinitionFile = STACK_ROOT + STACK_NAME + "/stack.json";
        JsonObject clusterVarsObject = readFile(stackDefinitionFile).getAsJsonObject("clusterVariablesMeta");
        return new Gson().fromJson(clusterVarsObject.toString(),HashMap.class).keySet();
    }

    public HashMap getInstanceEnvVariables(String moduleName, String instanceName) throws Exception {
        HashMap<String, String> envMap = new HashMap<>();
        JsonObject instance = getInstance(moduleName, instanceName);
        JsonObject staticVars = instance.getAsJsonObject("environmentVariables").getAsJsonObject("static");
        JsonObject dynamicVars = instance.getAsJsonObject("environmentVariables").getAsJsonObject("static");
        envMap.putAll(new Gson().fromJson(staticVars.toString(),HashMap.class));
        envMap.putAll(new Gson().fromJson(dynamicVars.toString(),HashMap.class));
        return envMap;
    }

}
