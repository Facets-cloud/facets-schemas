package com.capillary.ops.cp.bo;

public class StackIngressRule {
    private String ingressName;

    private String k8sService;

    private Integer timeout;

    private String ingressClass;

    private Boolean basicAuthEnabled;

    private String host;

    private String path;

    private Integer targetPort;

    public StackIngressRule() {

    }
    public StackIngressRule(StackIngressRule stackIngressRule) {
        this.ingressName = stackIngressRule.ingressName;
        this.k8sService = stackIngressRule.k8sService;
        this.timeout = stackIngressRule.timeout;
        this.ingressClass = stackIngressRule.ingressClass;
        this.basicAuthEnabled = stackIngressRule.basicAuthEnabled;
        this.host = stackIngressRule.host;
        this.path = stackIngressRule.path;
        this.targetPort = stackIngressRule.targetPort;
    }

    public String getIngressName() {
        return ingressName;
    }

    public void setIngressName(String ingressName) {
        this.ingressName = ingressName;
    }

    public String getK8sService() {
        return k8sService;
    }

    public void setK8sService(String k8sService) {
        this.k8sService = k8sService;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getIngressClass() {
        return ingressClass;
    }

    public void setIngressClass(String ingressClass) {
        this.ingressClass = ingressClass;
    }

    public Boolean getBasicAuthEnabled() {
        return basicAuthEnabled;
    }

    public void setBasicAuthEnabled(Boolean basicAuthEnabled) {
        this.basicAuthEnabled = basicAuthEnabled;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(Integer targetPort) {
        this.targetPort = targetPort;
    }

    @Override
    public String toString() {
        return "StackIngressRule{" +
                "ingressName='" + ingressName + '\'' +
                ", k8sService='" + k8sService + '\'' +
                ", timeout=" + timeout +
                ", ingressClass='" + ingressClass + '\'' +
                ", basicAuthEnabled=" + basicAuthEnabled +
                ", host='" + host + '\'' +
                ", path='" + path + '\'' +
                ", targetPort=" + targetPort +
                '}';
    }
}
