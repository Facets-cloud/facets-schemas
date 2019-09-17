package com.capillary.ops.deployer.bo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CompoundIndexes({
        @CompoundIndex(name = "unique_application", unique = true, def = "{'name':1, 'applicationFamily':1}")
})
@Document
public class Application {
    public enum DnsType {
        PUBLIC,
        PRIVATE
    }

    public enum ApplicationType {
        SERVICE,
        SCHEDULED_JOB,
        STATEFUL_SET
    }

    public Application(ApplicationType applicationType, String name, @NotNull VCSProvider vcsProvider, @NotNull String repositoryUrl, @NotBlank String applicationRootDirectory, @NotNull List<Port> ports, @NotNull LoadBalancerType loadBalancerType, List<PVC> pvcList, @NotNull BuildType buildType, @NotNull ApplicationFamily applicationFamily, String dnsPrefix, HealthCheck healthCheck, DnsType dnsType, Map<String, String> commonConfigs) {
        this.applicationType = applicationType;
        this.name = name;
        this.vcsProvider = vcsProvider;
        this.repositoryUrl = repositoryUrl;
        this.applicationRootDirectory = applicationRootDirectory;
        this.ports = ports;
        this.loadBalancerType = loadBalancerType;
        this.pvcList = pvcList;
        this.buildType = buildType;
        this.applicationFamily = applicationFamily;
        this.dnsPrefix = dnsPrefix;
        this.healthCheck = healthCheck;
        this.dnsType = dnsType;
        this.commonConfigs = commonConfigs;
    }

    public Application() {
    }

    @Id
    private String id;

    private ApplicationType applicationType;

    private String name;

    @NotNull
    private VCSProvider vcsProvider;

    @NotNull
    private String repositoryUrl;

    @NotBlank
    private String applicationRootDirectory;

    @NotNull
    private List<Port> ports;

    @NotNull
    private LoadBalancerType loadBalancerType;

    private List<PVC> pvcList;

    @NotNull
    private BuildType buildType;

    @NotNull
    private ApplicationFamily applicationFamily;

    private String dnsPrefix;

    private HealthCheck healthCheck;

    private DnsType dnsType;

    private Map<String, String> commonConfigs = new HashMap<>();

    private DeploymentStrategy deploymentStrategy = DeploymentStrategy.RollingUpdate;

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

    public List<PVC> getPvcList() {
        return pvcList;
    }

    public void setPvcList(List<PVC> pvcList) {
        this.pvcList = pvcList;
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

    public String getDnsPrefix() {
        return dnsPrefix;
    }

    public void setDnsPrefix(String dnsPrefix) {
        this.dnsPrefix = dnsPrefix;
    }

    public HealthCheck getHealthCheck() {
        return healthCheck;
    }

    public void setHealthCheck(HealthCheck healthCheck) {
        this.healthCheck = healthCheck;
    }

    public DnsType getDnsType() {
        return dnsType;
    }

    public void setDnsType(DnsType dnsType) {
        this.dnsType = dnsType;
    }

    public Map<String, String> getCommonConfigs() {
        return commonConfigs;
    }

    public void setCommonConfigs(Map<String, String> commonConfigs) {
        this.commonConfigs = commonConfigs;
    }

    public DeploymentStrategy getDeploymentStrategy() {
        return deploymentStrategy;
    }

    public void setDeploymentStrategy(DeploymentStrategy deploymentStrategy) {
        this.deploymentStrategy = deploymentStrategy;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }
}
