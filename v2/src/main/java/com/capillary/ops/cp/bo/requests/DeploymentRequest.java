package com.capillary.ops.cp.bo.requests;

import org.springframework.data.annotation.Id;

public class DeploymentRequest {

    @Id
    String id;

    String tag;

    private ReleaseType releaseType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ReleaseType getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(ReleaseType releaseType) {
        this.releaseType = releaseType;
    }
}
