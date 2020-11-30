package com.capillary.ops.cp.bo;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
public class Substack extends Stack implements Serializable {
    private String artifactPath;

    public String getArtifactPath() {
        return artifactPath;
    }

    public void setArtifactPath(String artifactPath) {
        this.artifactPath = artifactPath;
    }
}
