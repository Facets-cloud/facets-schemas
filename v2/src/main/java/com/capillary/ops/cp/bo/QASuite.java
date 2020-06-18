package com.capillary.ops.cp.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class QASuite {

    @Id
    @JsonIgnore
    private String id;

    private String runId;

    @JsonIgnore
    private String jobReferenceId;

    @NotNull
    @NotEmpty
    private String module;

    private String deploymentId;

    Map<String, String> environmentVariables;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getJobReferenceId() {
        return jobReferenceId;
    }

    public void setJobReferenceId(String jobReferenceId) {
        this.jobReferenceId = jobReferenceId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void setEnvironmentVariables(Map<String, String> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }
}
