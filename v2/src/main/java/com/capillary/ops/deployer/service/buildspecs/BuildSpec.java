package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.App;
import com.capillary.ops.deployer.bo.Application;

import java.util.*;

public abstract class BuildSpec {

    public String getVersion() {
        return "0.2";
    }

    public Map<String, Object> getPhases(Application application) {
        Map<String, Object> installPhase = getInstallPhase();
        Map<String, Object> preBuildPhase = getPreBuildPhase(application);
        Map<String, Object> buildPhase = getBuildPhase(application);
        Map<String, Object> postBuildPhase = getPostBuildPhase(application);

        Map<String, Object> phases = new HashMap<>();
        phases.put("install", installPhase);
        phases.put("pre_build", preBuildPhase);
        phases.put("build", buildPhase);
        phases.put("post_build", postBuildPhase);
        return phases;
    }

    private Map<String, Object> getPostBuildPhase(Application application) {
        List<String> postBuildCommands = getPostBuildCommands(application);
        Map<String, Object> postBuildPhase = new HashMap<>();
        postBuildPhase.put("commands", postBuildCommands);
        return postBuildPhase;
    }

    protected abstract List<String> getPostBuildCommands(Application application);

    private Map<String, Object> getBuildPhase(Application application) {
        List<String> buildCommands = new ArrayList<>();
        Map<String, Object> buildPhase = new HashMap<>();
        buildCommands.add("$(aws ecr get-login --region us-east-1 --no-include-email)");
        buildCommands.add(String.format("cd %s", application.getApplicationRootDirectory()));
        buildCommands.addAll(getBuildCommands(application));
        buildPhase.put("commands", buildCommands);
        return buildPhase;
    }

    protected abstract List<String> getBuildCommands(Application application);

    private Map<String, Object> getPreBuildPhase(Application application) {
        List<String> preBuildCommands = getPreBuildCommands(application);
        Map<String, Object> preBuildPhase = new HashMap<>();
        preBuildPhase.put("commands", preBuildCommands);
        return preBuildPhase;
    }

    protected abstract List<String> getPreBuildCommands(Application application);

    private Map<String, Object> getInstallPhase() {
        List<String> installCommands = new ArrayList<>();
        Map<String, Object> installPhase = new HashMap<>();
        installCommands.add(
                "nohup dockerd --host=unix:///var/run/docker.sock --host=tcp://0.0.0.0:2375 --storage-driver=overlay&");
        installCommands.add("timeout 15 sh -c \"until docker info; do echo .; sleep 1; done\"");
        installPhase.put("commands", installCommands);
        return installPhase;
    }


    public Map<String, Object> getCache(Application application) {
        Map<String, Object> cache = new HashMap<>();
        List<String> cachePaths = getCachePaths(application);
        cachePaths.add("/root/.m2/**/*");
        cache.put("paths", cachePaths);
        return cache;
    }

    protected abstract List<String> getCachePaths(Application application);

    public abstract String getBuildEnvironmentImage();
}
