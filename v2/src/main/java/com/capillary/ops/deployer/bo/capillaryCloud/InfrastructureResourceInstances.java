package com.capillary.ops.deployer.bo.capillaryCloud;

import java.util.Map;

public class InfrastructureResourceInstances {

    Map<String, VPC> vpcs;
    Map<String, K8sCluster> k8sClusters;

    public InfrastructureResourceInstances() {
    }

    public Map<String, VPC> getVpcs() {
        return vpcs;
    }

    public void setVpcs(Map<String, VPC> vpcs) {
        this.vpcs = vpcs;
    }

    public Map<String, K8sCluster> getK8sClusters() {
        return k8sClusters;
    }

    public void setK8sClusters(Map<String, K8sCluster> k8sClusters) {
        this.k8sClusters = k8sClusters;
    }
}
