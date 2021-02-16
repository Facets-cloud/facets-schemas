package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.ECRRegistry;
import com.capillary.ops.deployer.bo.Registry;
import com.capillary.ops.deployer.exceptions.NotImplementedException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.codebuild.model.EnvironmentType;
import software.amazon.awssdk.services.ecr.EcrClient;
import software.amazon.awssdk.services.ecr.model.GetAuthorizationTokenRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64;

public abstract class BuildSpec {

    protected Application application;

    protected boolean testBuild = false;

    protected List<Registry> registries;

    public BuildSpec(Application application) {
        this.application = application;
    }

    public BuildSpec(Application application, boolean testBuild, List<Registry> registries) {
        this.application = application;
        this.testBuild = testBuild;
        this.registries = registries;
    }

    public String getVersion() {
        return "0.2";
    }

    public Map<String, Object> getPhases() {
        Map<String, Object> installPhase;
        Map<String, Object> preBuildPhase;
        Map<String, Object> buildPhase;
        Map<String, Object> postBuildPhase;

        if (this.isTestBuild()) {
            installPhase = getInstallPhaseTest();
            preBuildPhase = getPreBuildPhaseTest();
            buildPhase = getBuildPhaseTest();
            postBuildPhase = getPostBuildPhaseTest();
        } else {
            installPhase = getInstallPhase();
            preBuildPhase = getPreBuildPhase();
            buildPhase = getBuildPhase();
            postBuildPhase = getPostBuildPhase();
        }

        Map<String, Object> phases = new HashMap<>();
        phases.put("install", installPhase);
        phases.put("pre_build", preBuildPhase);
        phases.put("build", buildPhase);
        phases.put("post_build", postBuildPhase);
        return phases;
    }

    private Map<String, Object> getPostBuildPhase() {
        List<String> postBuildCommands = getPostBuildCommands();
        if(configureDockerBuildSteps()) {
            this.registries.forEach(x -> {
                postBuildCommands.add(String.format("docker tag $APP_NAME:$TAG %s/$APP_NAME:$TAG", x.getUri()));
                postBuildCommands.add(String.format("docker push %s/$APP_NAME:$TAG", x.getUri()));
            });
        }
        Map<String, Object> postBuildPhase = new HashMap<>();
        postBuildPhase.put("commands", postBuildCommands);
        return postBuildPhase;
    }

    private Map<String, Object> getPostBuildPhaseTest() {
        List<String> postBuildCommands = getPostBuildCommandsTest();
        Map<String, Object> postBuildPhase = new HashMap<>();
        postBuildPhase.put("commands", postBuildCommands);
        return postBuildPhase;
    }

    protected abstract List<String> getPostBuildCommands();

    protected abstract List<String> getPostBuildCommandsTest();

    private Map<String, Object> getBuildPhase() {
        List<String> buildCommands = new ArrayList<>();
        Map<String, Object> buildPhase = new HashMap<>();
        buildCommands.add(String.format("cd %s", application.getApplicationRootDirectory()));
        buildCommands.addAll(getBuildCommands());
        if (configureDockerBuildSteps()) buildCommands.add("docker build -t $APP_NAME:$TAG .");
        buildPhase.put("commands", buildCommands);
        return buildPhase;
    }

    private Map<String, Object> getBuildPhaseTest() {
        List<String> buildCommands = new ArrayList<>();
        Map<String, Object> buildPhase = new HashMap<>();
        buildCommands.add(String.format("cd %s", application.getApplicationRootDirectory()));
        buildCommands.addAll(getBuildCommandsTest());
        buildPhase.put("commands", buildCommands);
        return buildPhase;
    }

    protected abstract List<String> getBuildCommands();

    protected abstract List<String> getBuildCommandsTest();

    private Map<String, Object> getPreBuildPhase() {
        List<String> preBuildCommands;
        preBuildCommands = getPreBuildCommands();
        if(configureDockerBuildSteps()) {
            this.registries.forEach(registry -> preBuildCommands.add(getLoginCommand(registry)));
        }
        Map<String, Object> preBuildPhase = new HashMap<>();
        preBuildPhase.put("commands", preBuildCommands);
        return preBuildPhase;
    }

    private Map<String, Object> getPreBuildPhaseTest() {
        List<String> preBuildCommands;
        preBuildCommands = getPreBuildCommandsTest();
        Map<String, Object> preBuildPhase = new HashMap<>();
        preBuildPhase.put("commands", preBuildCommands);
        return preBuildPhase;
    }

    public Map<String, Object> getArtifacts() {
        List<String> artifactSpec;
        if (this.isTestBuild()) {
            artifactSpec = getArtifactSpecTest();
        } else {
            artifactSpec = getArtifactSpec();
        }

        Map<String, Object> artifacts = new HashMap<>();
        artifacts.put("files", artifactSpec);
        return artifacts;
    }

    protected abstract List<String> getPreBuildCommands();

    protected abstract List<String> getPreBuildCommandsTest();

    protected abstract List<String> getArtifactSpec();

    protected abstract List<String> getArtifactSpecTest();

    private Map<String, Object> getInstallPhase() {
        List<String> installCommands = new ArrayList<>();
        Map<String, Object> installPhase = new HashMap<>();
        if(configureDockerBuildSteps()) {
            installCommands.add(
                    "nohup dockerd --host=unix:///var/run/docker.sock --host=tcp://0.0.0.0:2375 --storage-driver=overlay&");
            installCommands.add("timeout 15 sh -c \"until docker info; do echo .; sleep 1; done\"");
        }
        installPhase.put("commands", installCommands);
        return installPhase;
    }

    private String getLoginCommand(Registry registry){
        if (registry instanceof ECRRegistry){
            ECRRegistry ecrRegistry = (ECRRegistry) registry;
            if (ecrRegistry.getAwsKey() == null || ecrRegistry.getAwsSecret() == null){
                return "$(aws ecr get-login --region us-west-1 --no-include-email)";
            }
            else {
                String authToken = getEcrToken(ecrRegistry);
                String user = authToken.split(":")[0];
                String password = authToken.split(":")[1];
                return String.format("docker login -u %s -p %s https://%s", user, password, ecrRegistry.getUri());
            }
        } else {
            throw new NotImplementedException("registry not supported");
        }
    }

    private String getEcrToken(ECRRegistry ecrRegistry){
        AwsCredentialsProvider provider = StaticCredentialsProvider.create(AwsBasicCredentials.create(ecrRegistry.getAwsKey(), ecrRegistry.getAwsSecret()));
        EcrClient ecrClient = EcrClient.builder().credentialsProvider(provider).region(Region.of(ecrRegistry.getAwsRegion())).build();
        GetAuthorizationTokenRequest request = GetAuthorizationTokenRequest.builder().registryIds(ecrRegistry.getAwsAccountId()).build();
        String authTokenObject = ecrClient.getAuthorizationToken(request).authorizationData().get(0).authorizationToken();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(authTokenObject);

        return new String(bytes);
    }

    protected Map<String, Object> getInstallPhaseTest() {
        return new HashMap<>();
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
        return !Application.ApplicationType.SERVERLESS.equals(application.getApplicationType()) && !isTestBuild();
    }

    protected boolean isTestBuild() {
        return testBuild;
    }

    protected void setTestBuild(boolean testBuild) {
        this.testBuild = testBuild;
    }
}
