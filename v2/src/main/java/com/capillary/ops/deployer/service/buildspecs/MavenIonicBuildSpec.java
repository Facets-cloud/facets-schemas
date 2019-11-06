package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MavenIonicBuildSpec extends MavenBuildSpec {

    public MavenIonicBuildSpec(Application application) {
        super(application);
    }

    public MavenIonicBuildSpec(Application application, boolean testBuild) {
        super(application, testBuild);
    }

    @Override
    public String getBuildEnvironmentImage() {
        return "486456986266.dkr.ecr.us-west-1.amazonaws.com/ops/mavenionicbuildenv:72a11f1";
    }
}
