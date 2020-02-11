package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.codebuild.CodeBuildClient;
import software.amazon.awssdk.services.codebuild.model.EnvironmentVariable;
import software.amazon.awssdk.services.codebuild.model.EnvironmentVariableType;
import software.amazon.awssdk.services.codebuild.model.ResourceNotFoundException;
import software.amazon.awssdk.services.codebuild.model.StartBuildRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * CodeBuild service to trigger TF Builds.
 */
public class AwsCodeBuildService implements TFBuildService {

    private static final String BUILD_SUFFIX = "_TF";

    private static final String CLUSTER_ID = "CLUSTER_ID";

    private static final Region BUILD_REGION = Region.US_WEST_1;

    /**
     * Deploy the latest build in the specified clusterId
     *
     * @param cluster Cluster Information
     * @return
     */
    @Override
    public String deployLatest(AbstractCluster cluster) {
        //DONE: Check if code build is defined for the said cloud
        List<EnvironmentVariable> environmentVariables = new ArrayList<>();
        environmentVariables.add(EnvironmentVariable.builder().name(CLUSTER_ID).value(cluster.getId())
            .type(EnvironmentVariableType.PLAINTEXT).build());
        StartBuildRequest startBuildRequest = StartBuildRequest.builder().projectName(cluster.getCloud() + BUILD_SUFFIX)
            .environmentVariablesOverride(environmentVariables).build();
        try {
            return getCodeBuildClient().startBuild(startBuildRequest).build().id();
        } catch (ResourceNotFoundException ex) {
            throw new RuntimeException("No Build defined for " + cluster.getCloud(), ex);
        }
    }

    private CodeBuildClient getCodeBuildClient() {
        return CodeBuildClient.builder().region(BUILD_REGION).credentialsProvider(DefaultCredentialsProvider.create())
            .build();
    }

}
