package com.capillary.ops.cp.bo;

public class QASuiteModuleResult {
    public QASuiteModuleResult() {}

    public QASuiteModuleResult(String qaSuiteResultId, String moduleName, String deploymentId, K8sJobStatus status) {
        this.qaSuiteResultId = qaSuiteResultId;
        this.moduleName = moduleName;
        this.deploymentId = deploymentId;
        this.status = status;
    }

    private String qaSuiteResultId;

    private String moduleName;

    private String deploymentId;

    private K8sJobStatus status;

    public String getQaSuiteResultId() {
        return qaSuiteResultId;
    }

    public void setQaSuiteResultId(String qaSuiteResultId) {
        this.qaSuiteResultId = qaSuiteResultId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

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
}
