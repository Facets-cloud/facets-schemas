package com.capillary.ops.deployer.bo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Map;

public enum ApplicationFamily {
    CRM,
    ECOMMERCE,
    INTEGRATIONS,
    OPS;

    public Environment getEnvironment(String environmentName) {
        Gson gson = new Gson();
        Type REVIEW_TYPE = new TypeToken<Map<String, Environment>>() {}.getType();
        JsonReader reader = null;
        try {
            String fileName = String.format("/etc/capillary/clusterdetails/%s.json", this.name().toLowerCase());
            reader = new JsonReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Map<String, Environment> environments = gson.fromJson(reader, REVIEW_TYPE);
        Environment environment = environments.get(environmentName);
        if(environment == null) {
            throw new RuntimeException("Unknown environment {0}".format(environmentName));
        }
        environment.setName(environmentName);
        return environment;
    }
}
