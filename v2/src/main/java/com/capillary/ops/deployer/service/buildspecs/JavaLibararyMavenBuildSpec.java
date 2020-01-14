package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;

import java.util.ArrayList;
import java.util.List;

public class JavaLibararyMavenBuildSpec extends MavenBuildSpec {

    public JavaLibararyMavenBuildSpec(Application application) {
        super(application);
    }

    public JavaLibararyMavenBuildSpec(Application application, boolean testBuild) {
        super(application, testBuild);
    }

    @Override
    protected List<String> getBuildCommands() {
        ArrayList<String> buildCommands = new ArrayList<>();
        buildCommands.add("mvn clean deploy -Dmaven.test.failure.ignore=false -DskipFormat=true -Dmaven.test.skip=true -U");
        return buildCommands;
    }

    @Override
    public boolean configureDockerBuildSteps() {
        return false;
    }
}
