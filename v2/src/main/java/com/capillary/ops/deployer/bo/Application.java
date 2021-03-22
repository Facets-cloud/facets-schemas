package com.capillary.ops.deployer.bo;

import com.google.common.collect.Sets;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.*;
import java.util.stream.Collectors;

@CompoundIndexes({
        @CompoundIndex(name = "unique_application", unique = true, def = "{'name':1, 'applicationFamily':1}")
})
@Document
public class Application {

    private static final String DEFAULT_STATUS_CALLBACK_URL = "https://api.flock.com/hooks/sendMessage/74e69c78-021a-49f4-9775-e526bbf861e0";

    public enum DnsType {
        PUBLIC,
        PRIVATE
    }

    public enum ApplicationType {
        SERVICE,
        SCHEDULED_JOB,
        STATEFUL_SET,
        SERVERLESS
    }

    public enum ResourceAllocationStrategy {
        GENERAL_PURPOSE,
        CPU_INTENSIVE
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

    public Application(ApplicationType applicationType, String name, @NotNull VCSProvider vcsProvider, @NotNull String repositoryUrl, @NotBlank String applicationRootDirectory, @NotNull List<Port> ports, @NotNull LoadBalancerType loadBalancerType, List<PVC> pvcList, @NotNull BuildType buildType, @NotNull ApplicationFamily applicationFamily, String dnsPrefix, HealthCheck healthCheck, DnsType dnsType, Map<String, String> commonConfigs, List<String> tagBuildRepositoryIds, List<String> branchBuildRepositoryIds){
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
        this.tagBuildRepositoryIds = tagBuildRepositoryIds;
        this.branchBuildRepositoryIds = branchBuildRepositoryIds;
    }

    public Application() {
    }

    @Id
    private String id;

    private ApplicationType applicationType = ApplicationType.SERVICE;

    private String name;

    @NotNull
    private VCSProvider vcsProvider;

    @NotNull
    private String repositoryUrl;

    // this is needed for scheduled sonar triggers
    @Null
    private String repositoryDefaultBranch;

    @NotBlank
    private String applicationRootDirectory;

    @NotNull
    private List<Port> ports;

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

    private boolean ciEnabled = false;

    private String webhookId;

    private DeploymentStrategy deploymentStrategy = DeploymentStrategy.RollingUpdate;

    private int elbIdleTimeoutSeconds = 300;

    private boolean strictGitFlowModeEnabled = false;

    private String statusCallbackUrl;

    private String newRelicAlertRecipients;

    private List<String> tagBuildRepositoryIds;

    private List<String> branchBuildRepositoryIds;

    private ResourceAllocationStrategy resourceAllocationStrategy = ResourceAllocationStrategy.GENERAL_PURPOSE;

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

    public boolean isCiEnabled() {
        return ciEnabled;
    }

    public void setCiEnabled(boolean ciEnabled) {
        this.ciEnabled = ciEnabled;
    }

    public String getWebhookId() {
        return webhookId;
    }

    public void setWebhookId(String webhookId) {
        this.webhookId = webhookId;
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

    public int getElbIdleTimeoutSeconds() {
        return elbIdleTimeoutSeconds;
    }

    public void setElbIdleTimeoutSeconds(int elbIdleTimeoutSeconds) {
        this.elbIdleTimeoutSeconds = elbIdleTimeoutSeconds;
    }

    public boolean isStrictGitFlowModeEnabled() {
        return strictGitFlowModeEnabled;
    }

    public void setStrictGitFlowModeEnabled(boolean strictGitFlowModeEnabled) {
        this.strictGitFlowModeEnabled = strictGitFlowModeEnabled;
    }

    public String getNewRelicAlertRecipients() {
        return newRelicAlertRecipients;
    }

    public void setNewRelicAlertRecipients(String newRelicAlertRecipients) {
        this.newRelicAlertRecipients = newRelicAlertRecipients;
    }

    public String getStatusCallbackUrl() {
        return statusCallbackUrl;
    }

    public void setStatusCallbackUrl(String statusCallbackUrl) {
        this.statusCallbackUrl = statusCallbackUrl;
    }

    public List<String> getStatusCallbackUrls() {
        Set<String> callbackUrls = Sets.newHashSet(this.statusCallbackUrl, DEFAULT_STATUS_CALLBACK_URL);
        return callbackUrls.stream().filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.toList());
    }

    public ResourceAllocationStrategy getResourceAllocationStrategy() {
        return resourceAllocationStrategy;
    }

    public void setResourceAllocationStrategy(ResourceAllocationStrategy resourceAllocationStrategy) {
        this.resourceAllocationStrategy = resourceAllocationStrategy;
    }

    public List<String> getTagBuildRepositoryIds() {
        return tagBuildRepositoryIds == null ? new ArrayList<>() : tagBuildRepositoryIds;
    }

    public void setTagBuildRepositoryIds(List<String> tagBuildRepositoryIds) {
        this.tagBuildRepositoryIds = tagBuildRepositoryIds;
    }

    public List<String> getBranchBuildRepositoryIds() {
        return branchBuildRepositoryIds == null ? new ArrayList<>() : branchBuildRepositoryIds;
    }

    public void setBranchBuildRepositoryIds(List<String> branchBuildRepositoryIds) {
        this.branchBuildRepositoryIds = branchBuildRepositoryIds;
    }

    public String getRepositoryDefaultBranch() {
        return repositoryDefaultBranch;
    }

    public void setRepositoryDefaultBranch(String repositoryDefaultBranch) {
        this.repositoryDefaultBranch = repositoryDefaultBranch;
    }
}
