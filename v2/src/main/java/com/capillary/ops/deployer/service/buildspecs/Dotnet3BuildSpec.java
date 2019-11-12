package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;

public class Dotnet3BuildSpec extends DotnetBuildSpec {
    public Dotnet3BuildSpec(Application application) {
        super(application);
    }

    public Dotnet3BuildSpec(Application application, boolean testBuild) {
        super(application, testBuild);
    }

    @Override
    public String getBuildEnvironmentImage() {
        return "486456986266.dkr.ecr.us-west-1.amazonaws.com/ops/dotnetcore3:buildenv1.0";
    }
}
