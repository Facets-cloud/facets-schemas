package com.capillary.ops.cp.bo.requests;

import org.springframework.data.annotation.Id;
import software.amazon.awssdk.services.codebuild.model.EnvironmentVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeploymentRequest {

    @Id
    String id;

    String tag;

    private ReleaseType releaseType;

    private String overrideCCVersion;

    private Map<String, String> extraEnv = new HashMap<>();

    private List<String> overrideBuildSteps = new ArrayList<>();

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

    public Map<String, String> getExtraEnv() {
        if (this.extraEnv == null) {
            return new HashMap<>();
        }
        return extraEnv;
    }

    public void setExtraEnv(Map<String, String> extraEnv) {
        this.extraEnv = extraEnv;
    }

    public List<String> getOverrideBuildSteps() {
        return overrideBuildSteps;
    }

    public void setOverrideBuildSteps(List<String> overrideBuildSteps) {
        this.overrideBuildSteps = overrideBuildSteps;
    }

    public String getOverrideCCVersion() {
        return overrideCCVersion;
    }

    public void setOverrideCCVersion(String overrideCCVersion) {
        this.overrideCCVersion = overrideCCVersion;
    }
}
