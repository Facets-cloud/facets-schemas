package com.capillary.ops.deployer.bo;

import java.util.Map;

public class EnvironmentConfiguration {

    public EnvironmentConfiguration(String kubernetesToken, String nodeGroup, String kubernetesApiEndpoint,
                                    ExternalDnsConfiguration privateDnsConfiguration,
                                    ExternalDnsConfiguration publicDnsConfiguration,
                                    Map<String, String> commonConfigs, Map<String, String> commonCredentials,
                                    SSLConfigs sslConfigs, String ecrMirrorRepo) {
        this.kubernetesToken = kubernetesToken;
        this.nodeGroup = nodeGroup;
        this.kubernetesApiEndpoint = kubernetesApiEndpoint;
        this.privateDnsConfiguration = privateDnsConfiguration;
        this.publicDnsConfiguration = publicDnsConfiguration;
        this.commonConfigs = commonConfigs;
        this.commonCredentials = commonCredentials;
        this.sslConfigs = sslConfigs;
        this.ecrMirrorRepo = ecrMirrorRepo;
    }

    public EnvironmentConfiguration() {
    }

    private String kubernetesToken;
    private String nodeGroup;
    private String kubernetesApiEndpoint;
    private ExternalDnsConfiguration privateDnsConfiguration;
    private ExternalDnsConfiguration publicDnsConfiguration;
    private S3DumpAwsConfig s3DumpAwsConfig;
    private Map<String, String> commonConfigs;
    private Map<String, String> commonCredentials;
    private SSLConfigs sslConfigs;
    private String ecrMirrorRepo;
    private K8sLoggingConfiguration k8sLoggingConfiguration;
    private Kube2IamConfiguration kube2IamConfiguration;
    private boolean spotTerminationHandlingEnabled = false;
    private String newRelicClusterName;
    private boolean metricServerEnabled = false;
    private ClusterAutoscalerConfiguration clusterAutoscalerConfiguration;

    public String getKubernetesToken() {
        return kubernetesToken;
    }

    public void setKubernetesToken(String kubernetesToken) {
        this.kubernetesToken = kubernetesToken;
    }

    public String getNodeGroup() {
        return nodeGroup;
    }

    public void setNodeGroup(String nodeGroup) {
        this.nodeGroup = nodeGroup;
    }

    public String getKubernetesApiEndpoint() {
        return kubernetesApiEndpoint;
    }

    public void setKubernetesApiEndpoint(String kubernetesApiEndpoint) {
        this.kubernetesApiEndpoint = kubernetesApiEndpoint;
    }

    public ExternalDnsConfiguration getPrivateDnsConfiguration() {
        return privateDnsConfiguration;
    }

    public void setPrivateDnsConfiguration(ExternalDnsConfiguration privateDnsConfiguration) {
        this.privateDnsConfiguration = privateDnsConfiguration;
    }

    public ExternalDnsConfiguration getPublicDnsConfiguration() {
        return publicDnsConfiguration;
    }

    public void setPublicDnsConfiguration(ExternalDnsConfiguration publicDnsConfiguration) {
        this.publicDnsConfiguration = publicDnsConfiguration;
    }

    public S3DumpAwsConfig getS3DumpAwsConfig() {
        return s3DumpAwsConfig;
    }

    public void setS3DumpAwsConfig(S3DumpAwsConfig s3DumpAwsConfig) {
        this.s3DumpAwsConfig = s3DumpAwsConfig;
    }

    public Map<String, String> getCommonConfigs() {
        return commonConfigs;
    }

    public void setCommonConfigs(Map<String, String> commonConfigs) {
        this.commonConfigs = commonConfigs;
    }

    public Map<String, String> getCommonCredentials() {
        return commonCredentials;
    }

    public void setCommonCredentials(Map<String, String> commonCredentials) {
        this.commonCredentials = commonCredentials;
    }

    public SSLConfigs getSslConfigs() {
        return sslConfigs;
    }

    public void setSslConfigs(SSLConfigs sslConfigs) {
        this.sslConfigs = sslConfigs;
    }

    public String getEcrMirrorRepo() {
        return ecrMirrorRepo;
    }

    public void setEcrMirrorRepo(String ecrMirrorRepo) {
        this.ecrMirrorRepo = ecrMirrorRepo;
    }

    public K8sLoggingConfiguration getK8sLoggingConfiguration() {
        return k8sLoggingConfiguration;
    }

    public void setK8sLoggingConfiguration(K8sLoggingConfiguration k8sLoggingConfiguration) {
        this.k8sLoggingConfiguration = k8sLoggingConfiguration;
    }

    public Kube2IamConfiguration getKube2IamConfiguration() {
        return kube2IamConfiguration;
    }

    public void setKube2IamConfiguration(Kube2IamConfiguration kube2IamConfiguration) {
        this.kube2IamConfiguration = kube2IamConfiguration;
    }

    public boolean isSpotTerminationHandlingEnabled() {
        return spotTerminationHandlingEnabled;
    }

    public void setSpotTerminationHandlingEnabled(boolean spotTerminationHandlingEnabled) {
        this.spotTerminationHandlingEnabled = spotTerminationHandlingEnabled;
    }

    public String getNewRelicClusterName() {
        return newRelicClusterName;
    }

    public void setNewRelicClusterName(String newRelicClusterName) {
        this.newRelicClusterName = newRelicClusterName;
    }

    public boolean isMetricServerEnabled() {
        return metricServerEnabled;
    }

    public void setMetricServerEnabled(boolean metricServerEnabled) {
        this.metricServerEnabled = metricServerEnabled;
    }

    public ClusterAutoscalerConfiguration getClusterAutoscalerConfiguration() {
        return clusterAutoscalerConfiguration;
    }

    public void setClusterAutoscalerConfiguration(ClusterAutoscalerConfiguration clusterAutoscalerConfiguration) {
        this.clusterAutoscalerConfiguration = clusterAutoscalerConfiguration;
    }
}
