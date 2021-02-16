package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Registry;

import java.util.List;

public class Dotnet22BuildSpec extends DotnetBuildSpec {
    public Dotnet22BuildSpec(Application application) {
        super(application);
    }

    public Dotnet22BuildSpec(Application application, boolean testBuild, List<Registry> registries) {
        super(application, testBuild, registries);
    }

    @Override
    public String getBuildEnvironmentImage() {
        return "486456986266.dkr.ecr.us-west-1.amazonaws.com/ops/dotnetcore2p2:buildenv1.0";
    }
}
