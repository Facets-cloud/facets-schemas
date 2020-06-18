package com.capillary.ops.cp.bo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class QASuiteResult {
    public QASuiteResult() {}

    public QASuiteResult(QASuiteResult qaSuiteResult) {
        this.deploymentId = qaSuiteResult.deploymentId;
        this.status = qaSuiteResult.status;
        this.moduleStatusMap = qaSuiteResult.moduleStatusMap;
        this.redeployment = qaSuiteResult.redeployment;
    }

    @NotNull
    @NotEmpty
    private String deploymentId;

    @NotNull
    private K8sJobStatus status;

    Map<String, K8sJobStatus> moduleStatusMap;

    private boolean redeployment = false;

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public K8sJobStatus getStatus() {
        return status;
    }

    public void setStatus(K8sJobStatus status) {
        this.status = status;
    }

    public Map<String, K8sJobStatus> getModuleStatusMap() {
        return moduleStatusMap;
    }

    public void setModuleStatusMap(Map<String, K8sJobStatus> moduleStatusMap) {
        this.moduleStatusMap = moduleStatusMap;
    }

    public boolean isRedeployment() {
        return redeployment;
    }

    public void setRedeployment(boolean redeployment) {
        this.redeployment = redeployment;
    }

    public QASuiteResult withModuleStatusMap(Map<String, K8sJobStatus> moduleStatusMap) {
        QASuiteResult copy = new QASuiteResult(this);
        copy.setModuleStatusMap(moduleStatusMap);
        return copy;
    }
}
