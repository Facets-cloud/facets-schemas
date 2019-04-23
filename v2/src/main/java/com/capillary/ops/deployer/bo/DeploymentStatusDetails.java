package com.capillary.ops.deployer.bo;

import java.util.List;

public class DeploymentStatusDetails {

    private ApplicationServiceDetails service;

    private ApplicationDeploymentDetails deplyment;

    private List<ApplicationPodDetails> pods;

    public DeploymentStatusDetails() {}

    public DeploymentStatusDetails(ApplicationServiceDetails service, ApplicationDeploymentDetails deplyment, List<ApplicationPodDetails> pods) {
        this.service = service;
        this.deplyment = deplyment;
        this.pods = pods;
    }

    public ApplicationServiceDetails getService() {
        return service;
    }

    public void setService(ApplicationServiceDetails service) {
        this.service = service;
    }

    public ApplicationDeploymentDetails getDeplyment() {
        return deplyment;
    }

    public void setDeplyment(ApplicationDeploymentDetails deplyment) {
        this.deplyment = deplyment;
    }

    public List<ApplicationPodDetails> getPods() {
        return pods;
    }

    public void setPods(List<ApplicationPodDetails> pods) {
        this.pods = pods;
    }
}
