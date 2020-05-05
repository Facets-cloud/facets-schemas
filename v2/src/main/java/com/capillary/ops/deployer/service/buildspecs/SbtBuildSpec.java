package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SbtBuildSpec extends BuildSpec {

    public SbtBuildSpec(Application application) {
        super(application);
    }

    public SbtBuildSpec(Application application, boolean testBuild) {
        super(application, testBuild);
    }

    @Override
    protected List<String> getPostBuildCommands() {
        List<String> postBuildCommands = new ArrayList<>();
        postBuildCommands.add("docker push $REPO/$APP_NAME:$TAG");
        return postBuildCommands;
    }

    @Override
    protected List<String> getPostBuildCommandsTest() {
        return new ArrayList<>();
    }

    @Override
    protected List<String> getBuildCommands() {
        ArrayList<String> buildCommands = new ArrayList<>();
        buildCommands.add("sbt 'set test in assembly := {}' clean assembly");
        buildCommands.add("docker build -t $APP_NAME:$TAG .");
        buildCommands.add("docker tag $APP_NAME:$TAG $REPO/$APP_NAME:$TAG");
        return buildCommands;
    }

    @Override
    protected List<String> getBuildCommandsTest() {
        ArrayList<String> buildCommands = new ArrayList<>();
        buildCommands.add("sbt clean test");
        return buildCommands;
    }

    @Override
    protected List<String> getPreBuildCommands() {
        String ECR_REPO = "486456986266.dkr.ecr.us-west-1.amazonaws.com";
        List<String> preBuildCommands = new ArrayList<>();
        preBuildCommands.add("TAG=$(echo $CODEBUILD_RESOLVED_SOURCE_VERSION | head -c 7)");
        preBuildCommands.add("TAG=$TAG-$CODEBUILD_BUILD_NUMBER");
        preBuildCommands.add("REPO=" + ECR_REPO);
        preBuildCommands.add("APP_NAME=" + application.getApplicationFamily().name().toLowerCase() + "/" + application.getName());
        return preBuildCommands;
    }

    @Override
    protected List<String> getPreBuildCommandsTest() {
        return new ArrayList<>();
    }

    @Override
    protected List<String> getArtifactSpec() {
        return new ArrayList<>();
    }

    @Override
    protected List<String> getArtifactSpecTest() {
        String rootDirectory = this.application.getApplicationRootDirectory();
        return Lists.newArrayList(rootDirectory + "/target/**/*");
    }

    @Override
    protected List<String> getCachePaths() {
        return Arrays.asList("/root/.sbt/**/*","/root/.ivy2/**/*");
    }

    @Override
    public String getBuildEnvironmentImage() {
        return "486456986266.dkr.ecr.us-west-1.amazonaws.com/capillary/sbtdocker:1.0";
    }
}
