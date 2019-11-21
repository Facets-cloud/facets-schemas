package com.capillary.ops.deployer.bo;

import com.capillary.ops.deployer.service.buildspecs.BuildSpec;
import software.amazon.awssdk.services.codebuild.model.*;

public class ProjectSpec {
    public ProjectSpec(BuildSpec buildSpec, ProjectSource projectSource, ProjectEnvironment projectEnvironment, ProjectCache projectCache, String serviceRole, LogsConfig logsConfig, VpcConfig vpcConfig, ProjectArtifacts projectArtifacts) {
        this.buildSpec = buildSpec;
        this.projectSource = projectSource;
        this.projectEnvironment = projectEnvironment;
        this.projectCache = projectCache;
        this.serviceRole = serviceRole;
        this.logsConfig = logsConfig;
        this.vpcConfig = vpcConfig;
        this.projectArtifacts = projectArtifacts;
    }

    BuildSpec buildSpec;
    ProjectSource projectSource;
    ProjectEnvironment projectEnvironment;
    ProjectCache projectCache;
    String serviceRole;
    LogsConfig logsConfig;
    VpcConfig vpcConfig;
    ProjectArtifacts projectArtifacts;

    public BuildSpec getBuildSpec() {
        return buildSpec;
    }

    public void setBuildSpec(BuildSpec buildSpec) {
        this.buildSpec = buildSpec;
    }

    public ProjectSource getProjectSource() {
        return projectSource;
    }

    public void setProjectSource(ProjectSource projectSource) {
        this.projectSource = projectSource;
    }

    public ProjectEnvironment getProjectEnvironment() {
        return projectEnvironment;
    }

    public void setProjectEnvironment(ProjectEnvironment projectEnvironment) {
        this.projectEnvironment = projectEnvironment;
    }

    public ProjectCache getProjectCache() {
        return projectCache;
    }

    public void setProjectCache(ProjectCache projectCache) {
        this.projectCache = projectCache;
    }

    public String getServiceRole() {
        return serviceRole;
    }

    public void setServiceRole(String serviceRole) {
        this.serviceRole = serviceRole;
    }

    public LogsConfig getLogsConfig() {
        return logsConfig;
    }

    public void setLogsConfig(LogsConfig logsConfig) {
        this.logsConfig = logsConfig;
    }

    public VpcConfig getVpcConfig() {
        return vpcConfig;
    }

    public void setVpcConfig(VpcConfig vpcConfig) {
        this.vpcConfig = vpcConfig;
    }

    public ProjectArtifacts getProjectArtifacts() {
        return projectArtifacts;
    }

    public void setProjectArtifacts(ProjectArtifacts projectArtifacts) {
        this.projectArtifacts = projectArtifacts;
    }
}
