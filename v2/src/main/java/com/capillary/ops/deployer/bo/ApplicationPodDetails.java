package com.capillary.ops.deployer.bo;

import java.util.HashMap;
import java.util.Map;

public class ApplicationPodDetails {

    public ApplicationPodDetails() {}

    public ApplicationPodDetails(String name, Map<String, String> labels, String podStauts, String image, String imageID, String creationTimestamp, Boolean ready) {
        this.name = name;
        this.labels = labels;
        this.podStauts = podStauts;
        this.image = image;
        this.imageID = imageID;
        this.creationTimestamp = creationTimestamp;
        this.ready = ready;
    }

    private String name;

    private Map<String, String> labels = new HashMap<>();

    private String podStauts;

    private String image;

    private String imageID;

    private String creationTimestamp;

    private PodResource resourceUsage;

    private Boolean ready;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public String getPodStauts() {
        return podStauts;
    }

    public void setPodStauts(String podStauts) {
        this.podStauts = podStauts;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public PodResource getResourceUsage() {
        return resourceUsage;
    }

    public ApplicationPodDetails setResourceUsage(PodResource resourceUsage) {
        this.resourceUsage = resourceUsage;
        return this;
    }

    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }
}
