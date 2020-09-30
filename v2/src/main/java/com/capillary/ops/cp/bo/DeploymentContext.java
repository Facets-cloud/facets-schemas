package com.capillary.ops.cp.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Map;

public class DeploymentContext {

    public DeploymentContext() {
    }

    public DeploymentContext(AbstractCluster cluster,
                             Map<String, Map<String, Artifact>> artifacts,
                             List<OverrideObject> overrides,
                             Map<String, Map<String, SnapshotInfo>> snapshots) {
        this.cluster = cluster;
        this.artifacts = artifacts;
        this.overrides = overrides;
        this.snapshots = snapshots;
    }

    @JsonIgnore
    private AbstractCluster cluster;
    private Map<String, Map<String, Artifact>> artifacts;
    private List<OverrideObject> overrides;
    private Map<String, Map<String, SnapshotInfo>> snapshots;

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

}
