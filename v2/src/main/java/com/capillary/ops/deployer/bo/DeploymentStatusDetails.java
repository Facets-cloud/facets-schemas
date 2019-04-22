package com.capillary.ops.deployer.bo;

import java.util.List;

public class DeploymentStatusDetails {

    private ServiceCheckDetails service;

    private DeploymentCheckDetails deplyment;

    private List<PodCheckDetails> pods;

    public DeploymentStatusDetails() {}

    public DeploymentStatusDetails(ServiceCheckDetails service, DeploymentCheckDetails deplyment, List<PodCheckDetails> pods) {
        this.service = service;
        this.deplyment = deplyment;
        this.pods = pods;
    }

    public ServiceCheckDetails getService() {
        return service;
    }

    public void setService(ServiceCheckDetails service) {
        this.service = service;
    }

    public DeploymentCheckDetails getDeplyment() {
        return deplyment;
    }

    public void setDeplyment(DeploymentCheckDetails deplyment) {
        this.deplyment = deplyment;
    }

    public List<PodCheckDetails> getPods() {
        return pods;
    }

    public void setPods(List<PodCheckDetails> pods) {
        this.pods = pods;
    }
}
