package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DotnetBuildSpec extends BuildSpec {

    public DotnetBuildSpec(Application application) {
        super(application);
    }

    @Override
    protected List<String> getPostBuildCommands() {
        throw new NotImplementedException();
    }

    @Override
    protected List<String> getBuildCommands() {
        throw new NotImplementedException();
    }

    @Override
    protected List<String> getPreBuildCommands() {
        throw new NotImplementedException();

    }

    @Override
    protected List<String> getCachePaths() {
        throw new NotImplementedException();

    }

    @Override
    public String getBuildEnvironmentImage() {
        throw new NotImplementedException();
    }
}
