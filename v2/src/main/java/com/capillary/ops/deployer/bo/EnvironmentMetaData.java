package com.capillary.ops.deployer.bo;

public class EnvironmentMetaData {

    public EnvironmentMetaData(EnvironmentType environmentType, String name, ApplicationFamily applicationFamily) {
        this.environmentType = environmentType;
        this.name = name;
        this.applicationFamily = applicationFamily;
    }

    public EnvironmentMetaData() {
    }

    private EnvironmentType environmentType;
    private String name;
    private ApplicationFamily applicationFamily;
    private boolean gitFlowDevelopmentBranchDeploymentAllowed = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnvironmentType getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(EnvironmentType environmentType) {
        this.environmentType = environmentType;
    }

    public ApplicationFamily getApplicationFamily() {
        return applicationFamily;
    }

    public void setApplicationFamily(ApplicationFamily applicationFamily) {
        this.applicationFamily = applicationFamily;
    }

    public boolean isGitFlowDevelopmentBranchDeploymentAllowed() {
        return gitFlowDevelopmentBranchDeploymentAllowed;
    }

    public void setGitFlowDevelopmentBranchDeploymentAllowed(boolean gitFlowDevelopmentBranchDeploymentAllowed) {
        this.gitFlowDevelopmentBranchDeploymentAllowed = gitFlowDevelopmentBranchDeploymentAllowed;
    }
}
