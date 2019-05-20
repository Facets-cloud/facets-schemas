package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.App;
import com.capillary.ops.deployer.bo.Application;

import java.util.*;

public abstract class BuildSpec {

    protected Application application;

    public BuildSpec(Application application) {
        this.application = application;
    }

    public String getVersion() {
        return "0.2";
    }

    public Map<String, Object> getPhases() {
        Map<String, Object> installPhase = getInstallPhase();
        Map<String, Object> preBuildPhase = getPreBuildPhase();
        Map<String, Object> buildPhase = getBuildPhase();
        Map<String, Object> postBuildPhase = getPostBuildPhase();

        Map<String, Object> phases = new HashMap<>();
        phases.put("install", installPhase);
        phases.put("pre_build", preBuildPhase);
        phases.put("build", buildPhase);
        phases.put("post_build", postBuildPhase);
        return phases;
    }

    private Map<String, Object> getPostBuildPhase() {
        List<String> postBuildCommands = getPostBuildCommands();
        Map<String, Object> postBuildPhase = new HashMap<>();
        postBuildPhase.put("commands", postBuildCommands);
        return postBuildPhase;
    }

    protected abstract List<String> getPostBuildCommands();

    private Map<String, Object> getBuildPhase() {
        List<String> buildCommands = new ArrayList<>();
        Map<String, Object> buildPhase = new HashMap<>();
        buildCommands.add("$(aws ecr get-login --region us-west-1 --no-include-email)");
        buildCommands.add(String.format("cd %s", application.getApplicationRootDirectory()));
        buildCommands.addAll(getBuildCommands());
        buildPhase.put("commands", buildCommands);
        return buildPhase;
    }

    protected abstract List<String> getBuildCommands();

    private Map<String, Object> getPreBuildPhase() {
        List<String> preBuildCommands = getPreBuildCommands();
        Map<String, Object> preBuildPhase = new HashMap<>();
        preBuildPhase.put("commands", preBuildCommands);
        return preBuildPhase;
    }

    protected abstract List<String> getPreBuildCommands();

    private Map<String, Object> getInstallPhase() {
        List<String> installCommands = new ArrayList<>();
        Map<String, Object> installPhase = new HashMap<>();
        installCommands.add(
                "nohup dockerd --host=unix:///var/run/docker.sock --host=tcp://0.0.0.0:2375 --storage-driver=overlay&");
        installCommands.add("timeout 15 sh -c \"until docker info; do echo .; sleep 1; done\"");
        installPhase.put("commands", installCommands);
        return installPhase;
    }


    public Map<String, Object> getCache() {
        Map<String, Object> cache = new HashMap<>();
        List<String> cachePaths = getCachePaths();
        cache.put("paths", cachePaths);
        return cache;
    }

    protected abstract List<String> getCachePaths();

    public abstract String getBuildEnvironmentImage();
}
