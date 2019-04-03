package com.capillary.ops.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.annotation.Id;

public class Application {

  public Application() {
  }

  public Application(String id, String name, String privateKey, String publicKey, String repoURL, String projectFolder, Map<String, String> tags, String port, Map<Environments, String> endpoints, Map<Environments, Map<String, String>> configs) {
    this.name = name;
    this.privateKey = privateKey;
    this.publicKey = publicKey;
    this.repoURL = repoURL;
    this.projectFolder = projectFolder;
    this.tags = tags;
    this.port = port;
    this.endpoints = endpoints;
    this.configs = configs;
  }

  @Id @JsonIgnore private String id;

  private String name;

  @JsonIgnore private String privateKey;

  @JsonIgnore private String publicKey;

  private String repoURL;

  private String projectFolder;

  private Map<String, String> tags;

  private String port = "3000";

  @JsonIgnore private Map<Environments, String> endpoints;

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

  @JsonProperty
  public String getId() {
    return id;
  }

  @JsonIgnore
  public void setId(String id) {
    this.id = id;
  }

  public Map<Environments, Map<String, String>> getConfigs() {
    if (configs == null) {
      configs = new HashMap<>();
    }
    for (Environments env : Environments.values()) {
      configs.computeIfAbsent(env, k -> new HashMap<>());
      configs.get(env).put("PORT", port);
    }

    return configs;
  }

  public void setConfigs(Map<Environments, Map<String, String>> configs) {
    this.configs = configs;
  }

  @JsonProperty
  public Map<Environments, String> getEndpoints() {
    Map<Environments, String> ret = new HashMap<>();
    Arrays.stream(Environments.values()).forEach(x -> ret.put(x, x.getDomain(this.name)));
    return ret;
  }

  @JsonIgnore
  public void setEndpoints(Map<Environments, String> endpoints) {
    this.endpoints = endpoints;
  }

  public String getProjectFolder() {
    return projectFolder;
  }

  public void setProjectFolder(String projectFolder) {
    this.projectFolder = projectFolder;
  }

  public Map<String, String> getTags() {
    return tags;
  }

  public void setTags(Map<String, String> tags) {
    this.tags = tags;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getPort() {
    return port;
  }
}
