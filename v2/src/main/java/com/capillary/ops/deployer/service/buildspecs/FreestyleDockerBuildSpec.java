package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import org.springframework.stereotype.Component;

import java.util.*;

public class FreestyleDockerBuildSpec extends BuildSpec {

    public FreestyleDockerBuildSpec(Application application) {
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
        buildCommands.add("docker build -t $APP_NAME:$TAG .");
        buildCommands.add("docker tag $APP_NAME:$TAG $REPO/$APP_NAME:$TAG");
        return buildCommands;
    }

    @Override
    protected List<String> getPreBuildCommands() {
        String ECR_REPO = "486456986266.dkr.ecr.us-east-1.amazonaws.com";
        List<String> preBuildCommands = new ArrayList<>();
        preBuildCommands.add("TAG=$(echo $CODEBUILD_RESOLVED_SOURCE_VERSION | head -c 7)");
        preBuildCommands.add("echo $TAG");
        preBuildCommands.add("REPO=" + ECR_REPO);
        preBuildCommands.add("APP_NAME=" + application.getName());
        return preBuildCommands;
    }

    @Override
    protected List<String> getCachePaths() {
        return Arrays.asList();
    }

    @Override
    public String getBuildEnvironmentImage() {
        return "486456986266.dkr.ecr.us-east-1.amazonaws.com/mavendocker:v1.4";
    }
}
