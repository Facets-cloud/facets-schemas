package com.capillary.ops.deployer.bo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

public class Application {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private VCSProvider vcsProvider;

    private String repositoryUrl;

    private String applicationRootDirectory;

    private List<Port> ports;

    private LoadBalancerType loadBalancerType;

    private BuildType buildType;

    private ApplicationFamily applicationFamily;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VCSProvider getVcsProvider() {
        return vcsProvider;
    }

    public void setVcsProvider(VCSProvider vcsProvider) {
        this.vcsProvider = vcsProvider;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getApplicationRootDirectory() {
        return applicationRootDirectory;
    }

    public void setApplicationRootDirectory(String applicationRootDirectory) {
        this.applicationRootDirectory = applicationRootDirectory;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public LoadBalancerType getLoadBalancerType() {
        return loadBalancerType;
    }

    public void setLoadBalancerType(LoadBalancerType loadBalancerType) {
        this.loadBalancerType = loadBalancerType;
    }

    public BuildType getBuildType() {
        return buildType;
    }

    public void setBuildType(BuildType buildType) {
        this.buildType = buildType;
    }

    public ApplicationFamily getApplicationFamily() {
        return applicationFamily;
    }

    public void setApplicationFamily(ApplicationFamily applicationFamily) {
        this.applicationFamily = applicationFamily;
    }
}
