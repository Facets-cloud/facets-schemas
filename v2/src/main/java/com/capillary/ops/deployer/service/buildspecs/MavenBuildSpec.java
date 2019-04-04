package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MavenBuildSpec extends BuildSpec {

    @Override
    protected List<String> getPostBuildCommands(Application application) {
        return new ArrayList<>();
    }

    @Override
    protected List<String> getBuildCommands(Application application) {
        return Arrays.asList("mvn clean package -Dmaven.test.failure.ignore=false -DskipFormat=true -Dmaven.test.skip=true -U -Pdocker-cibuild");
    }

    @Override
    protected List<String> getPreBuildCommands(Application application) {
        return new ArrayList<>();
    }

    @Override
    protected List<String> getCachePaths(Application application) {
        return Arrays.asList("/root/.m2/**/*");
    }

    @Override
    public String getBuildEnvironmentImage() {
        return "486456986266.dkr.ecr.us-east-1.amazonaws.com/mavendocker:v1.4";
    }
}
