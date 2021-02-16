package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Registry;
import com.capillary.ops.deployer.bo.webhook.sonar.CallbackBody;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SbtBuildSpec extends BuildSpec {

    public SbtBuildSpec(Application application) {
        super(application);
    }

    public SbtBuildSpec(Application application, boolean testBuild, List<Registry> registries) {
        super(application, testBuild, registries);
    }

    @Override
    protected List<String> getPostBuildCommands() {
        return new ArrayList<>();
    }

    @Override
    protected List<String> getPostBuildCommandsTest() {
        return new ArrayList<>();
    }

    @Override
    protected List<String> getBuildCommands() {
        ArrayList<String> buildCommands = new ArrayList<>();
        buildCommands.add("sbt 'set test in assembly := {}' clean assembly");
        return buildCommands;
    }

    @Override
    protected List<String> getBuildCommandsTest() {
        ArrayList<String> buildCommands = new ArrayList<>();
        //buildCommands.add("sbt clean test");
        buildCommands.add("sbt clean package test sonarScan -Dmaven.test.failure.ignore=true " +
                " -Dsonar.host.url=http://sonar.capillary.in/ " +
                " -Dsonar.projectVersion=${CODEBUILD_RESOLVED_SOURCE_VERSION}-${pullRequestNumber}" +
                " -Dsonar.branch.name=${CODEBUILD_SOURCE_VERSION}" +
                " -D"+ CallbackBody.PR_NUMBER+"=$pullRequestNumber " +
                " -D"+ CallbackBody.DEPLOYER_BUILD_ID+"=$deployerBuildId " +
                " -D"+ CallbackBody.APP_FAMILY+"=$appFamily " +
                " -D"+ CallbackBody.APP_ID+"=$appId ");

        return buildCommands;
    }

    @Override
    protected List<String> getPreBuildCommands() {
        List<String> preBuildCommands = new ArrayList<>();
        preBuildCommands.add("TAG=$(echo $CODEBUILD_RESOLVED_SOURCE_VERSION | head -c 7)");
        preBuildCommands.add("TAG=$TAG-$CODEBUILD_BUILD_NUMBER");
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
