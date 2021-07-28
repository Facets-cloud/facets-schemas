package com.capillary.ops.cp.bo;

import com.capillary.ops.cp.bo.components.ComponentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document
public class Stack {

    @Id
    private String name;

    private String vcsUrl;

    private VCS vcs;

    private String relativePath;

    private String user;

//    @Transient
    private String appPassword;

    private Map<ComponentType, String> componentVersions = new HashMap<>();

    private boolean pauseReleases = false;

    private Map<String, String> stackVars = new HashMap<>();

    private Map<String, StackFile.VariableDetails> clusterVariablesMeta = new HashMap<>();

    // @JsonIgnore
    private List<String> childStacks = new ArrayList<>();

    private List<String> artifactories = new ArrayList<>();

    private Set<Resource> providedResources = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VCS getVcs() {
        return vcs;
    }

    public void setVcs(VCS vcs) {
        this.vcs = vcs;
    }

    public String getVcsUrl() {
        return vcsUrl;
    }

    public void setVcsUrl(String vcsUrl) {
        this.vcsUrl = vcsUrl;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAppPassword() {
        return appPassword;
    }

    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }

    public Map<ComponentType, String> getComponentVersions() {
        return componentVersions;
    }

    public void setComponentVersions(Map<ComponentType, String> componentVersions) {
        this.componentVersions = componentVersions;
    }

    public Map<String, String> getStackVars() {
        return stackVars;
    }

    public void setStackVars(Map<String, String> stackVars) {
        this.stackVars = stackVars;
    }

    public void setClusterVariablesMeta(Map<String, StackFile.VariableDetails> clusterVariablesMeta) {
        this.clusterVariablesMeta = clusterVariablesMeta;
    }

    public Map<String, StackFile.VariableDetails> getClusterVariablesMeta() {
        return clusterVariablesMeta;
    }

    public boolean isPauseReleases() {
        return pauseReleases;
    }

    public void setPauseReleases(boolean pauseReleases) {
        this.pauseReleases = pauseReleases;
    }

    public List<String> getChildStacks() {
        return childStacks;
    }

    public void setChildStacks(List<String> childStacks) {
        this.childStacks = childStacks;
    }

    public List<String> getArtifactories() {
        return artifactories;
    }

    public void setArtifactories(List<String> artifactories) {
        this.artifactories = artifactories;
    }

  public Set<Resource> getProvidedResources() {
    return providedResources;
  }

  public void setProvidedResources(Set<Resource> providedResources) {
    this.providedResources = providedResources;
  }
}
