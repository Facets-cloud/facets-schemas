package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.webhook.sonar.CallbackBody;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NPMUIBuildSpec extends BuildSpec {

    private static final Logger logger = LoggerFactory.getLogger(NPMUIBuildSpec.class);

    public NPMUIBuildSpec(Application application) {
        super(application);
    }

    public NPMUIBuildSpec(Application application, boolean testBuild) {
        super(application, testBuild);
    }

    @Override
    protected List<String> getPostBuildCommands() {
        List<String> postBuildCommands = new ArrayList<>();
        if (!this.isTestBuild()) {
            postBuildCommands.add("docker push $REPO/$APP_NAME:$TAG");
        }
        return postBuildCommands;
    }

    @Override
    protected List<String> getPostBuildCommandsTest() {
        return new ArrayList<>();
    }

    @Override
    protected List<String> getBuildCommands() {
        List<String> buildCommands = new ArrayList<>();
        buildCommands.add("npm install");
        buildCommands.add("npm run-script build");
        buildCommands.add("docker build -t $APP_NAME:$TAG .");
        buildCommands.add("docker tag $APP_NAME:$TAG $REPO/$APP_NAME:$TAG");
        return buildCommands;
    }

    @Override
    protected List<String> getBuildCommandsTest() {
        ArrayList<String> buildCommands = new ArrayList<>();
        buildCommands.add("sonar-scanner -Dsonar.host.url=http://sonar.capillary.in" +
                " -Dsonar.branch.name=${CODEBUILD_SOURCE_VERSION}" +
                " -D" + CallbackBody.PR_NUMBER + "=$pullRequestNumber " +
                " -D" + CallbackBody.DEPLOYER_BUILD_ID + "=$deployerBuildId " +
                " -D" + CallbackBody.APP_ID + "=$appId " +
                " -D" + CallbackBody.APP_FAMILY+"=$appFamily " +
                " -Dsonar.projectVersion=${CODEBUILD_RESOLVED_SOURCE_VERSION}-${pullRequestNumber}");
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
