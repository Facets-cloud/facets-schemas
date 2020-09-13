package com.capillary.ops.cp.bo.requests;

import software.amazon.awssdk.services.codebuild.model.EnvironmentVariable;

import java.util.ArrayList;
import java.util.List;

public class ApplicationHotfixRequest {

    String tag;

    private List<EnvironmentVariable> extraEnv = new ArrayList<>();

    private List<String> applicationTargets;

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

    public List<String> getApplicationTargets() {
        return applicationTargets;
    }

    public void setApplicationTargets(List<String> applicationTargets) {
        this.applicationTargets = applicationTargets;
    }
}
