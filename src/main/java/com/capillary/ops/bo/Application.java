package com.capillary.ops.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Application {

    @Id
    @JsonIgnore
    private String id;

    private String name;

    @JsonIgnore
    private String privateKey;

    @JsonIgnore
    private String publicKey;

    private String repoURL;

    @JsonIgnore
    private Map<Environments, String> endpoints;

    private Map<Environments, Map<String, String>> configs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @JsonProperty
    public String getPublicKey() {
        return publicKey;
    }

    @JsonIgnore
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getRepoURL() {
        return repoURL;
    }

    public void setRepoURL(String repoURL) {
        this.repoURL = repoURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<Environments, Map<String, String>> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<Environments, Map<String, String>> configs) {
        this.configs = configs;
    }

    @JsonProperty
    public Map<Environments, String> getEndpoints() {
        Map<Environments, String> ret = new HashMap<>();
        Arrays.stream(Environments.values()).forEach(x->ret.put(x, x.getDomain(this.name)));
        return ret;
    }

    @JsonIgnore
    public void setEndpoints(Map<Environments, String> endpoints) {
        this.endpoints = endpoints;
    }
}
