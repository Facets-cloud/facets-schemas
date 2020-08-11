package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.webhook.sonar.CallbackBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaLibararyMavenBuildSpec extends MavenBuildSpec {

    public JavaLibararyMavenBuildSpec(Application application) {
        super(application);
    }

    public JavaLibararyMavenBuildSpec(Application application, boolean testBuild) {
        super(application, testBuild);
    }

    @Override
    protected List<String> getPreBuildCommands() {
        List<String> preBuildCommands = new ArrayList<>();
        preBuildCommands.add("ssh-keyscan mvnrepo.capillary.co.in >> /root/.ssh/known_hosts");
        return preBuildCommands;
    }

    @Override
    protected List<String> getBuildCommands() {
        ArrayList<String> buildCommands = new ArrayList<>();
        buildCommands.add("mvn clean deploy -Dmaven.test.failure.ignore=true -DskipFormat=true -Dmaven.test" +
                ".skip=true -U");
        return buildCommands;
    }

    @Override
    protected List<String> getPostBuildCommands() {
        return new ArrayList<>();
    }

    @Override
    public boolean configureDockerBuildSteps() {
        return false;
    }

    @Override
    public String getBuildEnvironmentImage() {
        return "486456986266.dkr.ecr.us-west-1.amazonaws.com/mavenjavalibrary:v1.0";
    }

    @Override
    protected List<String> getCachePaths() {
        return Arrays.asList("/root/.m2/repository/**/*");
    }
}
