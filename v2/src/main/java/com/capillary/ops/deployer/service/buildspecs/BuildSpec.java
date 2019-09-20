package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.codebuild.model.EnvironmentType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BuildSpec {

    protected Application application;

    protected boolean testBuild = false;

    public BuildSpec(Application application) {
        this.application = application;
    }

    public BuildSpec(Application application, boolean testBuild) {
        this.application = application;
        this.testBuild = testBuild;
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
        if(configureDockerBuildSteps() && !this.isTestBuild()) {
            buildCommands.add("$(aws ecr get-login --region us-west-1 --no-include-email)");
        }
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

    public Map<String, Object> getArtifacts() {
        List<String> artifactSpec = getArtifactSpec();
        Map<String, Object> artifacts = new HashMap<>();
        artifacts.put("files", artifactSpec);
        return artifacts;
    }

    protected abstract List<String> getPreBuildCommands();

    protected abstract List<String> getArtifactSpec();

    private Map<String, Object> getInstallPhase() {
        List<String> installCommands = new ArrayList<>();
        Map<String, Object> installPhase = new HashMap<>();
        if(configureDockerBuildSteps() && !this.isTestBuild()) {
            installCommands.add(
                    "nohup dockerd --host=unix:///var/run/docker.sock --host=tcp://0.0.0.0:2375 --storage-driver=overlay&");
            installCommands.add("timeout 15 sh -c \"until docker info; do echo .; sleep 1; done\"");
        }
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

    public EnvironmentType getBuildEnvironmentType() {
        return EnvironmentType.LINUX_CONTAINER;
    }

    public boolean buildInVpc() {
        return true;
    }

    public Region getAwsRegion() {
        return Region.US_WEST_1;
    }

    public boolean useCache() {
        return true;
    }

    public boolean configureDockerBuildSteps() {
        return true;
    }

    public boolean isTestBuild() {
        return testBuild;
    }

    public void setTestBuild(boolean testBuild) {
        this.testBuild = testBuild;
    }
}
