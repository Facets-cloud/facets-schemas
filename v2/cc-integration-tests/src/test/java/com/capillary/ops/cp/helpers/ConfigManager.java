package com.capillary.ops.cp.helpers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

@Component
public class ConfigManager {

    @Autowired
    CommonUtils commonUtils;
    private String TEST_CONFIG_PATH = "src/main/resources/testConfiguration.json";

    public JsonObject readFile(String filePath) throws FileNotFoundException {
        JsonParser parser = new JsonParser();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        return parser.parse(bufferedReader).getAsJsonObject();
    }

    public JsonObject getTestConfiguration() throws Exception {
        String clusterName = commonUtils.getClusterName();
        return readFile(TEST_CONFIG_PATH).getAsJsonObject(clusterName);
    }
}
