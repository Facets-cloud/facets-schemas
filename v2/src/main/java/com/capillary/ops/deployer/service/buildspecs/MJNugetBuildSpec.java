package com.capillary.ops.deployer.service.buildspecs;

import com.capillary.ops.deployer.bo.Application;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.codebuild.model.EnvironmentType;

import java.util.ArrayList;
import java.util.List;

public class MJNugetBuildSpec extends BuildSpec {

    public MJNugetBuildSpec(Application application) {
        super(application);
    }

    @Override
    protected List<String> getPostBuildCommands() {
        List<String> postBuildCommands = new ArrayList<>();
        postBuildCommands.add("nuget push bin\\Release\\*.nupkg -Source mj-snapshot");
        return postBuildCommands;
    }

    @Override
    protected List<String> getBuildCommands() {
        ArrayList<String> buildCommands = new ArrayList<>();
        long version = System.currentTimeMillis();
        buildCommands.add("msbuild -t:restore");
        buildCommands.add("dotnet pack $package -c Release  -p:VersionSuffix=$versionSuffix");
        // -alpha-$env:CODEBUILD_SOURCE_VERSION'
        return buildCommands;
    }

    @Override
    protected List<String> getPreBuildCommands() {
        List<String> preBuildCommands = new ArrayList<>();
        preBuildCommands.add("$package = ($(Get-ChildItem -Filter \"*.*proj\")[0]).Name");
        //preBuildCommands.add("$versionSuffix = 'alpha-' + [int64](([datetime]::UtcNow)-(get-date \"1/1/1970\"))" +
                // ".TotalMilliseconds");
        preBuildCommands.add("if ($env:CODEBUILD_SOURCE_VERSION -eq \"origin/production\") {" +
                "        $versionSuffix = \"\"" +
                "       }" +
                "       elseif ($env:CODEBUILD_SOURCE_VERSION.StartsWith(\"origin/hotfix\") -or  $env:CODEBUILD_SOURCE_VERSION.StartsWith(\"origin/release\") ) {" +
                "         $versionSuffix = 'beta-'+ [int64](([datetime]::UtcNow)-(get-date \"1/1/1970\")).TotalMilliseconds" +
                "       }" +
                "       else { " +
                "       $versionSuffix = 'alpha-' + [int64](([datetime]::UtcNow)-(get-date \"1/1/1970\")).TotalMilliseconds " +
                "       }");
        return preBuildCommands;
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