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
    INTEGRATIONS;

    public Environment getEnvironment(String environmentName) {
        Gson gson = new Gson();
        Type REVIEW_TYPE = new TypeToken<Map<String, Environment>>() {}.getType();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader("/etc/capillary/clusterdetails/{0}.json".format(this.name().toLowerCase())));
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

    public String getAwsAccountId() {
        return System.getenv(this.name() + "_AWS_ACCOUNT_ID");
    }
}
