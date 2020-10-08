package com.capillary.ops.cp.bo.wrappers;

import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.deployer.bo.Deployment;

import java.util.List;

public class ListDeploymentsWrapper {
    private String clusterId;
    private List<DeploymentLog> deployments;
    private List<String> downStreamClusterNames;
    private DeploymentLog currentSignedOffDeployment;
    private Stack stack;

    public ListDeploymentsWrapper() {
    }

    public ListDeploymentsWrapper(Stack stack, String clusterId, List<DeploymentLog> deployments,
                                  List<String> downStreamClusterNames, DeploymentLog currentSignedOffDeployment) {
        this.clusterId = clusterId;
        this.deployments = deployments;
        this.downStreamClusterNames = downStreamClusterNames;
        this.currentSignedOffDeployment = currentSignedOffDeployment;
        this.stack = stack;
    }

    public String getClusterId() {
        return clusterId;
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
