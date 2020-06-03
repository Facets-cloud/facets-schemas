package com.capillary.ops.cp.bo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class QASuiteResult {

    @NotNull
    @NotEmpty
    private String deploymentId;

    @NotNull
    @NotEmpty
    private String status;

    Map<String, K8sJobStatus> modules;

    private boolean redeployment = false;

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, K8sJobStatus> getModules() {
        return modules;
    }

    public void setModule(Map<String, K8sJobStatus> module) {
        this.modules = module;
    }

    public boolean isRedeployment() {
        return redeployment;
    }

    public void setRedeployment(boolean redeployment) {
        this.redeployment = redeployment;
    }
}
