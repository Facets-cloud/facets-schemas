package com.capillary.ops.cp.bo.requests;

import software.amazon.awssdk.services.codebuild.model.EnvironmentVariable;

import java.util.ArrayList;
import java.util.List;

public class HotfixRequest {

    String tag;

    private List<EnvironmentVariable> extraEnv = new ArrayList<>();

    private CloudCodeBuildSpecRequest buildSpec;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<EnvironmentVariable> getExtraEnv() {
        return extraEnv;
    }

    public void setExtraEnv(List<EnvironmentVariable> extraEnv) {
        this.extraEnv = extraEnv;
    }

    public CloudCodeBuildSpecRequest getBuildSpec() {
        return buildSpec;
    }

    public void setBuildSpec(CloudCodeBuildSpecRequest buildSpec) {
        this.buildSpec = buildSpec;
    }
}
