package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.codebuild.CodeBuildClient;
import software.amazon.awssdk.services.codebuild.model.EnvironmentVariable;
import software.amazon.awssdk.services.codebuild.model.EnvironmentVariableType;
import software.amazon.awssdk.services.codebuild.model.ResourceNotFoundException;
import software.amazon.awssdk.services.codebuild.model.StartBuildRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CodeBuild service to trigger TF Builds.
 */
public class AwsCodeBuildService implements TFBuildService {

    private static final String BUILD_SUFFIX = "_TF";

    private static final String CLUSTER_ID = "CLUSTER_ID";

    private static final Region BUILD_REGION = Region.US_WEST_1;

    private static final String VCS_URL = "VCS_URL";

    private static final String VCS_TYPE = "VCS_TYPE";

    @Autowired
    private StackRepository stackRepository;

    /**
     * Deploy the latest build in the specified clusterId
     *
     * @param cluster Cluster Information
     * @return
     */
    @Override
    public String deployLatest(AbstractCluster cluster) {
        Optional<Stack> stackO = stackRepository.findById(cluster.getStackName());
        if(!stackO.isPresent()){
            throw new NotFoundException("The Stack for this cluster does not exist NOW");
        }
        Stack stack = stackO.get();
        //DONE: Check if code build is defined for the said cloud
        List<EnvironmentVariable> environmentVariables = new ArrayList<>();
        environmentVariables.add(EnvironmentVariable.builder().name(CLUSTER_ID).value(cluster.getId())
            .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(EnvironmentVariable.builder().name(VCS_URL).value(stack.getVcsUrl())
            .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(EnvironmentVariable.builder().name(VCS_TYPE).value(stack.getVcs().name())
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
