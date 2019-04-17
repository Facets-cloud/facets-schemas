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
        List<String> postBuildCommands = new ArrayList<>();
        postBuildCommands.add("docker push $REPO/$APP_NAME:$TAG");
        return postBuildCommands;
    }

    @Override
    protected List<String> getBuildCommands() {
        List<String> buildCommands = new ArrayList<>();
        buildCommands.add("dotnet clean");
        buildCommands.add("dotnet publish");
        buildCommands.add("docker build -t $APP_NAME:$TAG .");
        buildCommands.add("docker tag $APP_NAME:$TAG $REPO/$APP_NAME:$TAG");
        return buildCommands;
    }

    @Override
    protected List<String> getPreBuildCommands() {
        String ECR_REPO = "486456986266.dkr.ecr.us-west-1.amazonaws.com";
        List<String> preBuildCommands = new ArrayList<>();
        preBuildCommands.add("TAG=$(echo $CODEBUILD_RESOLVED_SOURCE_VERSION | head -c 7)");
        preBuildCommands.add("REPO=" + ECR_REPO);
        preBuildCommands.add("APP_NAME=" + application.getApplicationFamily().name().toLowerCase() + "/" + application.getName());
        return preBuildCommands;
    }

    @Override
    protected List<String> getCachePaths() {
        return Arrays.asList("/root/.nuget/NuGet/packages/*");
    }

    @Override
    public String getBuildEnvironmentImage() {
        return  "486456986266.dkr.ecr.us-west-1.amazonaws.com/dotnetcore:v1.0";
    }
}
