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

    Map<String, String> modules;

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

    public Map<String, String> getModules() {
        return modules;
    }

    public void setModule(Map<String, String> module) {
        this.modules = module;
    }
}
