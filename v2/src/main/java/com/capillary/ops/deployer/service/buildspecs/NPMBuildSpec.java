package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Registry;
import com.capillary.ops.deployer.bo.webhook.sonar.CallbackBody;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NPMBuildSpec extends BuildSpec {

    private static final Logger logger = LoggerFactory.getLogger(NPMBuildSpec.class);

    public NPMBuildSpec(Application application) {
        super(application);
    }

    public NPMBuildSpec(Application application, boolean testBuild, List<Registry> registries) {
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
        List<String> buildCommands = new ArrayList<>();
        buildCommands.add("npm install");
        buildCommands.add("npm build --prod");
        return buildCommands;
    }

    @Override
    protected List<String> getBuildCommandsTest() {
        ArrayList<String> buildCommands = new ArrayList<>();
        buildCommands.add("npm install");
        buildCommands.add("npm test");
        buildCommands.add("sonar-scanner -Dsonar.host.url=" + this.getSonarUrl() +
                " -Dsonar.branch.name=${CODEBUILD_SOURCE_VERSION}" +
                " -D" + CallbackBody.PR_NUMBER + "=$pullRequestNumber " +
                " -D" + CallbackBody.DEPLOYER_BUILD_ID + "=$deployerBuildId " +
                " -D" + CallbackBody.APP_ID + "=$appId " +
                " -D" + CallbackBody.APP_FAMILY+"=$appFamily " +
                " -Dsonar.projectVersion=${CODEBUILD_RESOLVED_SOURCE_VERSION}-${pullRequestNumber}" +
                " -Dsonar.javascript.lcov.reportPaths=reports/coverage/lcov.info" +
                " -Dsonar.exclusions=node_modules");
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
        List<String> preBuildCommandsTest = new ArrayList<>();
        preBuildCommandsTest.add("npm install -g sonarqube-scanner ");
        return preBuildCommandsTest;
    }

    @Override
    protected List<String> getArtifactSpec() {
        return new ArrayList<>();
    }

    @Override
    protected List<String> getArtifactSpecTest() {
        return Lists.newArrayList();
    }

    @Override
    protected List<String> getCachePaths() {
        String rootDirectory = this.application.getApplicationRootDirectory();
        return Arrays.asList(rootDirectory + "/node_modules");
    }

    @Override
    public String getBuildEnvironmentImage() {
        return "486456986266.dkr.ecr.us-west-1.amazonaws.com/ops/nodejsbuildimage:f18f9d0";
    }

}
