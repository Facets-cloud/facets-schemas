package com.capillary.ops.deployer.bo;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ObjectMeta;

import java.util.List;

public class KubeApplicationDetails {

    public enum K8sResourceType {
        DEPLOYMENT,
        CRONJOB,
        STATEFULSET
    }

    public KubeApplicationDetails() {}

    public KubeApplicationDetails(K8sResourceType resourceType, List<Container> containers, ObjectMeta metadata) {
        this.resourceType = resourceType;
        this.containers = containers;
        this.metadata = metadata;
    }

    private K8sResourceType resourceType;
    private List<Container> containers;
    private ObjectMeta metadata;

    public K8sResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(K8sResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public List<Container> getContainers() {
        return containers;
    }

    public void setContainers(List<Container> containers) {
        this.containers = containers;
    }

    public ObjectMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(ObjectMeta metadata) {
        this.metadata = metadata;
    }
}
