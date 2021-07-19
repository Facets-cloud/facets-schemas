package com.capillary.ops.cp.bo.wrappers;

import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.deployer.bo.Deployment;
import lombok.AllArgsConstructor;

import java.util.List;

public class ListDeploymentsWrapper {
    private String clusterId;
    private List<DeploymentLog> deployments;
    private List<DeploymentLog> deploymentsFull;
    private List<String> downStreamClusterNames;
    private DeploymentLog currentSignedOffDeployment;
    private Stack stack;
    private DeploymentsStats deploymentsStats;

    public ListDeploymentsWrapper() {
    }

    public ListDeploymentsWrapper(Stack stack, String clusterId, List<DeploymentLog> deployments,
                                  List<DeploymentLog> deploymentsFull,
                                  List<String> downStreamClusterNames, DeploymentLog currentSignedOffDeployment, DeploymentsStats deploymentsStats) {
        this.clusterId = clusterId;
        this.deployments = deployments;
        this.downStreamClusterNames = downStreamClusterNames;
        this.currentSignedOffDeployment = currentSignedOffDeployment;
        this.stack = stack;
        this.deploymentsStats = deploymentsStats;
        this.deploymentsFull = deploymentsFull;
    }

    @AllArgsConstructor
    public static class DeploymentsStats {
        public Integer successReleases;
        public Integer failedReleases;
        public Integer noChangeReleases;
    }

    public String getClusterId() {
        return clusterId;
    }

    public List<DeploymentLog> getDeploymentsFull() {
        return deploymentsFull;
    }

    public DeploymentsStats getDeploymentsStats() {
        return deploymentsStats;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public List<DeploymentLog> getDeployments() {
        return deployments;
    }

    public void setDeployments(List<DeploymentLog> deployments) {
        this.deployments = deployments;
    }

    public List<String> getDownStreamClusterNames() {
        return downStreamClusterNames;
    }

    public void setDeploymentsFull(List<DeploymentLog> deploymentsFull) {
        this.deploymentsFull = deploymentsFull;
    }

    public void setDeploymentsStats(DeploymentsStats deploymentsStats) {
        this.deploymentsStats = deploymentsStats;
    }

    public void setDownStreamClusterNames(List<String> downStreamClusterNames) {
        this.downStreamClusterNames = downStreamClusterNames;
    }

    public DeploymentLog getCurrentSignedOffDeployment() {
        return currentSignedOffDeployment;
    }

    public void setCurrentSignedOffDeployment(DeploymentLog currentSignedOffDeployment) {
        this.currentSignedOffDeployment = currentSignedOffDeployment;
    }

    public Stack getStack() {
        return stack;
    }

    public void setStack(Stack stack) {
        this.stack = stack;
    }
}
