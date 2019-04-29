package com.capillary.ops.deployer.bo;

import java.util.List;

public class DeploymentStatusDetails {

    private ApplicationServiceDetails service;

    private ApplicationDeploymentDetails deployment;

    private List<ApplicationPodDetails> pods;

    public DeploymentStatusDetails() {}

    public DeploymentStatusDetails(ApplicationServiceDetails service, ApplicationDeploymentDetails deployment, List<ApplicationPodDetails> pods) {
        this.service = service;
        this.deployment = deployment;
        this.pods = pods;
    }

    public ApplicationServiceDetails getService() {
        return service;
    }

    public void setService(ApplicationServiceDetails service) {
        this.service = service;
    }

    public ApplicationDeploymentDetails getDeployment() {
        return deployment;
    }

    public void setDeployment(ApplicationDeploymentDetails deployment) {
        this.deployment = deployment;
    }

    public List<ApplicationPodDetails> getPods() {
        return pods;
    }

    public void setPods(List<ApplicationPodDetails> pods) {
        this.pods = pods;
    }
}
