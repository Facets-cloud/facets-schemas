package com.capillary.ops.cp.bo;

import software.amazon.awssdk.services.codebuild.model.StatusType;

public class CodeBuildStatusCallback {

    public CodeBuildStatusCallback(String codebuidId, StatusType status) {
        this.codebuidId = codebuidId;
        this.status = status;
    }

    public CodeBuildStatusCallback() {
    }

    private String codebuidId;
    private StatusType status;

    public String getCodebuidId() {
        return codebuidId;
    }

    public void setCodebuidId(String codebuidId) {
        this.codebuidId = codebuidId;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }
}
