package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Registry;

import java.util.List;

public class Dotnet3BuildSpec extends DotnetBuildSpec {
    public Dotnet3BuildSpec(Application application, String sonarUrl) {
        super(application, sonarUrl);
    }

    public Dotnet3BuildSpec(Application application, boolean testBuild, List<Registry> registries, String sonarUrl) {
        super(application, testBuild, registries, sonarUrl);
    }

    @Override
    public String getBuildEnvironmentImage() {
        return "486456986266.dkr.ecr.us-west-1.amazonaws.com/ops/dotnetcore3:buildenv1.0";
    }
}
