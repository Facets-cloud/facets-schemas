package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.webhook.sonar.CallbackBody;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.*;

public class MavenBuildSpec extends BuildSpec {

    public MavenBuildSpec(Application application) {
        super(application);
    }

    public MavenBuildSpec(Application application, boolean testBuild) {
        super(application, testBuild);
    }

    @Override
    protected List<String> getPostBuildCommands() {
        List<String> postBuildCommands = new ArrayList<>();
        if(configureDockerBuildSteps()) {
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
        ArrayList<String> buildCommands = new ArrayList<>();
        buildCommands.add("mvn clean package -Dmaven.test.failure.ignore=true -DskipFormat=true -Dmaven.test" +
                ".skip=true -U");
        if(configureDockerBuildSteps()) {
            buildCommands.add("docker build -t $APP_NAME:$TAG .");
            buildCommands.add("docker tag $APP_NAME:$TAG $REPO/$APP_NAME:$TAG");
        }
        return buildCommands;
    }

    @Override
    protected List<String> getBuildCommandsTest() {
        ArrayList<String> buildCommands = new ArrayList<>();
        buildCommands.add("mvn clean test sonar:sonar -Dmaven.test.failure.ignore=true " +
                " -Dsonar.host.url=http://sonar.capillary.in/ " +
                " -Dsonar.projectVersion=${CODEBUILD_RESOLVED_SOURCE_VERSION}-${pullRequestNumber}" +
                " -Dsonar.branch.name=${CODEBUILD_SOURCE_VERSION}" +
                " -D"+ CallbackBody.PR_NUMBER+"=$pullRequestNumber " +
                " -D"+ CallbackBody.DEPLOYER_BUILD_ID+"=$deployerBuildId " +
                " -D"+ CallbackBody.APP_FAMILY+"=$appFamily " +
                " -D"+ CallbackBody.APP_ID+"=$appId ");
        // failures will be handled from sonar
        // removed -Dsonar.language=java jacoco:report as they are defaulted
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
        preBuildCommands.add("ssh-keyscan mvnrepo.capillary.co.in >> /root/.ssh/known_hosts");
        return preBuildCommands;
    }

    @Override
    protected List<String> getPreBuildCommandsTest() {
        return new ArrayList<>();
    }

    @Override
    protected List<String> getArtifactSpec() {
        if(Application.ApplicationType.SERVERLESS.equals(application.getApplicationType())) {
            return Lists.newArrayList(this.application.getApplicationRootDirectory() + "/**/*");
        }
        return new ArrayList<>();
    }

    @Override
    protected List<String> getArtifactSpecTest() {
        String rootDirectory = this.application.getApplicationRootDirectory();
        return Lists.newArrayList(rootDirectory + "/target/**/*");
    }

    @Override
    protected List<String> getCachePaths() {
        return Arrays.asList("/root/.m2/**/*");
    }

    @Override
    public String getBuildEnvironmentImage() {
        return "486456986266.dkr.ecr.us-west-1.amazonaws.com/mavendocker:v1.4";
    }
}
