package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MavenIonicBuildSpec extends BuildSpec {

    public MavenIonicBuildSpec(Application application) {
        super(application);
    }

    @Override
    protected List<String> getPostBuildCommands() {
        return new ArrayList<>();
    }

    @Override
    protected List<String> getBuildCommands() {
        return Arrays.asList("mvn clean package -Dmaven.test.failure.ignore=false -DskipFormat=true -Dmaven.test.skip=true -U -Pdocker-cibuild");
    }

    @Override
    protected List<String> getPreBuildCommands() {
        return new ArrayList<>();
    }

    @Override
    protected List<String> getCachePaths() {
        return Arrays.asList("/root/.m2/**/*");
    }

    @Override
    public String getBuildEnvironmentImage() {
        return "486456986266.dkr.ecr.us-west-1.amazonaws.com/ops/mavenionicbuildenv:02eb143";
    }
}
