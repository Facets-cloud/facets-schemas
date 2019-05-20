package com.capillary.ops.deployer.bo;

import java.util.HashMap;
import java.util.Map;

public class ApplicationPodDetails {

    public ApplicationPodDetails() {}

    public ApplicationPodDetails(String name, Map<String, String> labels, String podStauts, String image, String imageID, String creationTimestamp) {
        this.name = name;
        this.labels = labels;
        this.podStauts = podStauts;
        this.image = image;
        this.imageID = imageID;
        this.creationTimestamp = creationTimestamp;
    }

    private String name;

    private Map<String, String> labels = new HashMap<>();

    private String podStauts;

    private String image;

    private String imageID;

    private String creationTimestamp;

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
}
