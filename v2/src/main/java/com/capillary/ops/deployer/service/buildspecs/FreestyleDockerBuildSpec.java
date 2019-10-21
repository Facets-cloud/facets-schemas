package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.exceptions.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

public class FreestyleDockerBuildSpec extends BuildSpec {

    private static final Logger logger = LoggerFactory.getLogger(FreestyleDockerBuildSpec.class);

    public FreestyleDockerBuildSpec(Application application) {
        super(application);
    }

    public FreestyleDockerBuildSpec(Application application, boolean testBuild) {
        super(application, testBuild);
    }

    @Override
    protected List<String> getPostBuildCommands() {
        List<String> postBuildCommands = new ArrayList<>();
        postBuildCommands.add("docker push $REPO/$APP_NAME:$TAG");
        return postBuildCommands;
    }

    @Override
    protected List<String> getPostBuildCommandsTest() {
        logger.error("post build commands phase for FreestyleDockerBuildSpec test build is not implemented");
        throw new NotImplementedException("Post build commands phase for FreestyleDockerBuildSpec test build is not implemented");
    }

    @Override
    protected List<String> getBuildCommands() {
        List<String> buildCommands = new ArrayList<>();
        buildCommands.add("docker build -t $APP_NAME:$TAG .");
        buildCommands.add("docker tag $APP_NAME:$TAG $REPO/$APP_NAME:$TAG");
        return buildCommands;
    }

    @Override
    protected List<String> getBuildCommandsTest() {
        logger.error("build commands phase for FreestyleDockerBuildSpec test build is not implemented");
        throw new NotImplementedException("Build commands phase for FreestyleDockerBuildSpec test build is not implemented");
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
    protected List<String> getPreBuildCommandsTest() {
        logger.error("pre build commands phase for FreestyleDockerBuildSpec test build is not implemented");
        throw new NotImplementedException("Pre build commands phase for FreestyleDockerBuildSpec test build is not implemented");
    }

    @Override
    protected List<String> getArtifactSpec() {
        return new ArrayList<>();
    }

    @Override
    protected List<String> getArtifactSpecTest() {
        logger.error("get artifacts for FreestyleDockerBuildSpec test build is not implemented");
        throw new NotImplementedException("Get artifacts for FreestyleDockerBuildSpec test build is not implemented");
    }

    @Override
    protected List<String> getCachePaths() {
        return Arrays.asList();
    }

    @Override
    public String getBuildEnvironmentImage() {
        return "486456986266.dkr.ecr.us-west-1.amazonaws.com/mavendocker:v1.4";
    }
}
