package com.capillary.ops.service;

import com.capillary.ops.App;
import com.capillary.ops.bo.Application;
import com.capillary.ops.bo.Deployment;
import com.capillary.ops.bo.Environments;
import com.capillary.ops.bo.exceptions.ApplicationDoesNotExist;
import com.capillary.ops.bo.exceptions.ResourceAlreadyExists;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class DeisApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GitService gitService;

    public String login(Environments environment) {
        Gson gson = new Gson();
        String data = gson.toJson(new ImmutableMap.Builder<String, String>()
                .put("username", environment.getDeisUser())
                .put("password", environment.getDeisPassword()).build());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(data, headers);
        String endpoint = String.format("http://%s/v2/auth/login/", environment.getDeisEndpoint());
        String response = restTemplate.postForObject(endpoint, entity, String.class);
        Map<String, String> responseMap = gson.fromJson(response, HashMap.class);
        return responseMap.get("token");
    }

    public void updateApplication(Environments environment, Application application) {
        try {
            addConfigs(environment, application);
        } catch (Exception e) {
            System.out.println("Error while updating: "+ e.getMessage());
            e.printStackTrace();
        }
    }

    public void createApplication(Environments environment, Application application) {
        String token = login(environment);
        Gson gson = new Gson();
        String data = gson.toJson(new ImmutableMap.Builder<String, String>()
                .put("id", environment.generateAppName(application.getName()))
                .build());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", String.format("token %s", token));
        HttpEntity<String> entity = new HttpEntity<String>(data, headers);
        String endpoint = String.format("http://%s/v2/apps/", environment.getDeisEndpoint());
        try {
            restTemplate.postForObject(endpoint, entity, String.class);
            addKey(environment, application);
            addConfigs(environment, application);
        } catch (ResourceAlreadyExists e) {
            System.out.println("Application already exits");
        }
    }

    public void addKey(Environments environment, Application application) {
        String token = login(environment);
        ObjectMapper om = new ObjectMapper();
        try {
            String data = om.writeValueAsString(new ImmutableMap.Builder<String, String>()
                    .put("public", application.getPublicKey())
                    .put("id", application.getId())
                    .build());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.set("Authorization", String.format("token %s", token));
            HttpEntity<String> entity = new HttpEntity<String>(data, headers);
            String endpoint = String.format("http://%s/v2/keys/", environment.getDeisEndpoint());
            String s = restTemplate.postForObject(endpoint, entity, String.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ResourceAlreadyExists e) {
            System.out.println("Key already exits");
        }
    }

    public void addConfigs(Environments environment, Application application) {
        String token = login(environment);
        Gson gson = new Gson();
        String data = gson.toJson(new ImmutableMap.Builder<String, Map<String, String>>()
                .put("values", application.getConfigs().get(environment))
                .put("tags", ImmutableMap.of("environment", environment.name()))
                .build());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", String.format("token %s", token));
        HttpEntity<String> entity = new HttpEntity<String>(data, headers);
        String endpoint = String.format("http://%s/v2/apps/%s/config/",
                environment.getDeisEndpoint(),
                environment.generateAppName(application.getName()));
        restTemplate.postForObject(endpoint, entity, String.class);
    }

    public void deleteApplication(Environments environment, Application application) {
        String token = login(environment);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", String.format("token %s", token));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String endpoint = String.format("http://%s/v2/apps/%s",environment.getDeisEndpoint(),environment.generateAppName(application.getName()));
        try {
            restTemplate.exchange(endpoint, HttpMethod.DELETE, entity, String.class);
        } catch (Exception e) {
            System.out.println("Error deleting the app: "+ e.getMessage());
        }
    }
}
