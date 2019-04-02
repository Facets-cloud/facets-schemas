package com.capillary.ops.bo.helm;

import java.util.List;

public class FamilyEnvironment {

    private String clusterName;

    private String kubeUri;

    private List<String> nodeGroup;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getKubeUri() {
        return kubeUri;
    }

    public void setKubeUri(String kubeUri) {
        this.kubeUri = kubeUri;
    }

    public List<String> getNodeGroup() {
        return nodeGroup;
    }

    public void setNodeGroup(List<String> nodeGroup) {
        this.nodeGroup = nodeGroup;
    }
}
