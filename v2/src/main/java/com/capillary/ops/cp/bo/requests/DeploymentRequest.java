package com.capillary.ops.cp.bo.requests;

import org.springframework.data.annotation.Id;
import software.amazon.awssdk.services.codebuild.model.EnvironmentVariable;

import java.util.ArrayList;
import java.util.List;

public class DeploymentRequest {

    @Id
    String id;

    String tag;

    private ReleaseType releaseType;

    private List<EnvironmentVariable> extraEnv = new ArrayList<>();

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

    public List<EnvironmentVariable> getExtraEnv() {
        return extraEnv;
    }

    public void setExtraEnv(List<EnvironmentVariable> extraEnv) {
        this.extraEnv = extraEnv;
    }
}
