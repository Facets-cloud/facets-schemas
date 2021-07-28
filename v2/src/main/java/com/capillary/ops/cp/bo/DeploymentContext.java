package com.capillary.ops.cp.bo;

import com.capillary.ops.cp.bo.providedresources.ProvidedResources;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Map;

public class DeploymentContext {

    public DeploymentContext() {
    }

    public DeploymentContext(AbstractCluster cluster,
                             Map<String, Map<String, Artifact>> artifacts,
                             List<Artifactory> artifactoryDetails,
                             List<OverrideObject> overrides,
                             Map<String, Map<String, SnapshotInfo>> snapshots,
                             Map<String, String> extraEnv, ProvidedResources providedResources) {
        this.cluster = cluster;
        this.artifacts = artifacts;
        this.artifactoryDetails = artifactoryDetails;
        this.overrides = overrides;
        this.snapshots = snapshots;
        this.extraEnv = extraEnv;
        this.providedResources = providedResources;
    }

    @JsonIgnore
    private AbstractCluster cluster;
    private Map<String, Map<String, Artifact>> artifacts;
    private List<Artifactory> artifactoryDetails;
    private List<OverrideObject> overrides;
    private Map<String, Map<String, SnapshotInfo>> snapshots;
    private Map<String, String> extraEnv;
    private ProvidedResources providedResources;

    public AbstractCluster getCluster() {
        return cluster;
    }

    public void setCluster(AbstractCluster cluster) {
        this.cluster = cluster;
    }

    public Map<String, Map<String, Artifact>> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(Map<String, Map<String, Artifact>> artifacts) {
        this.artifacts = artifacts;
    }

    public List<Artifactory> getArtifactoryDetails() {
        return artifactoryDetails;
    }

    public void setArtifactoryDetails(List<Artifactory> artifactoryDetails) {
        this.artifactoryDetails = artifactoryDetails;
    }

    public List<OverrideObject> getOverrides() {
        return overrides;
    }

    public void setOverrides(List<OverrideObject> overrides) {
        this.overrides = overrides;
    }

    public Map<String, Map<String, SnapshotInfo>> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(Map<String, Map<String, SnapshotInfo>> snapshots) {
        this.snapshots = snapshots;
    }

    public Map<String, String> getExtraEnv() {
        return extraEnv;
    }

    public void setExtraEnv(Map<String, String> extraEnv) {
        this.extraEnv = extraEnv;
    }

  public ProvidedResources getProvidedResources() {
    return providedResources;
  }

  public void setProvidedResources(ProvidedResources providedResources) {
    this.providedResources = providedResources;
  }
}
