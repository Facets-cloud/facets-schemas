package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Registry;
import com.capillary.ops.deployer.exceptions.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.codebuild.model.EnvironmentType;

import java.util.ArrayList;
import java.util.List;

public class MJNugetBuildSpec extends BuildSpec {

    private static final Logger logger = LoggerFactory.getLogger(MJNugetBuildSpec.class);

    public MJNugetBuildSpec(Application application, String sonarUrl) {
        super(application, sonarUrl);
    }

    public MJNugetBuildSpec(Application application, boolean testBuild, List<Registry> registries, String sonarUrl) {
        super(application, testBuild, registries, sonarUrl);
    }

    @Override
    protected List<String> getPostBuildCommands() {
        List<String> postBuildCommands = new ArrayList<>();
        postBuildCommands.add("nuget push bin\\Release\\*.nupkg -Source mj-snapshot");
        return postBuildCommands;
    }

    @Override
    protected List<String> getPostBuildCommandsTest() {
        logger.error("post build commands phase for MJNugetBuildSpec test build is not implemented");
        throw new NotImplementedException("Post build commands phase for MJNugetBuildSpec test build is not implemented");
    }

    @Override
    protected List<String> getBuildCommands() {
        ArrayList<String> buildCommands = new ArrayList<>();
        long version = System.currentTimeMillis();
        buildCommands.add("$package = ($(Get-ChildItem -Filter \"*.*proj\")[0]).Name");
        buildCommands.add("msbuild -t:restore");
        buildCommands.add("dotnet pack $package -c Release  -p:VersionSuffix=$versionSuffix");
        // -alpha-$env:CODEBUILD_SOURCE_VERSION'
        return buildCommands;
    }

    @Override
    protected List<String> getBuildCommandsTest() {
        logger.error("build commands phase for MJNugetBuildSpec test build is not implemented");
        throw new NotImplementedException("Build commands phase for MJNugetBuildSpec test build is not implemented");
    }

    @Override
    protected List<String> getPreBuildCommands() {
        List<String> preBuildCommands = new ArrayList<>();
        //preBuildCommands.add("$versionSuffix = 'alpha-' + [int64](([datetime]::UtcNow)-(get-date \"1/1/1970\"))" +
                // ".TotalMilliseconds");
        preBuildCommands.add("if ($env:CODEBUILD_SOURCE_VERSION -eq \"production\") {" +
                "        $versionSuffix = \"\"" +
                "       }" +
                "       elseif ($env:CODEBUILD_SOURCE_VERSION.StartsWith(\"hotfix\") -or  $env:CODEBUILD_SOURCE_VERSION.StartsWith(\"release\") ) {" +
                "         $versionSuffix = 'beta-'+ [int64](([datetime]::UtcNow)-(get-date \"1/1/1970\")).TotalMilliseconds" +
                "       }" +
                "       else { " +
                "       $versionSuffix = 'alpha-' + [int64](([datetime]::UtcNow)-(get-date \"1/1/1970\")).TotalMilliseconds " +
                "       }");
        return preBuildCommands;
    }

    @Override
    protected List<String> getPreBuildCommandsTest() {
        logger.error("pre build commands phase for MJNugetBuildSpec test build is not implemented");
        throw new NotImplementedException("Pre build commands phase for MJNugetBuildSpec test build is not implemented");
    }

    @Override
    protected List<String> getArtifactSpec() {
        return new ArrayList<>();
    }

    @Override
    protected List<String> getArtifactSpecTest() {
        logger.error("get artifacts for MJNugetBuildSpec test build is not implemented");
        throw new NotImplementedException("Get artifacts for MJNugetBuildSpec test build is not implemented");
    }

    @Override
    protected List<String> getCachePaths() {
        return null;
    }

    @Override
    public String getBuildEnvironmentImage() {
        return "486456986266.dkr.ecr.us-east-1.amazonaws.com/ecommerce/nuget-base:latest";
    }

    @Override
    public EnvironmentType getBuildEnvironmentType() {
        return EnvironmentType.WINDOWS_CONTAINER;
    }

    @Override
    public boolean buildInVpc() {
        return false;
    }

    @Override
    public Region getAwsRegion() {
        return Region.US_EAST_1;
    }

    @Override
    public boolean useCache() {
        return false;
    }

    @Override
    public boolean configureDockerBuildSteps() {
        return false;
    }

}
