package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContext;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.codebuild.CodeBuildClient;
import software.amazon.awssdk.services.codebuild.model.EnvironmentVariable;
import software.amazon.awssdk.services.codebuild.model.EnvironmentVariableType;
import software.amazon.awssdk.services.codebuild.model.ResourceNotFoundException;
import software.amazon.awssdk.services.codebuild.model.StartBuildRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CodeBuild service to trigger TF Builds.
 */
@Component
public class AwsCodeBuildService implements TFBuildService {


    private static final String CLUSTER_ID = "CLUSTER_ID";

    private static final Region BUILD_REGION = Region.US_WEST_1;

    @Value("${internalApiAuthToken}")
    private String authToken;

    @Autowired
    private HttpServletRequest requestContext;

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
        environmentVariables.add(EnvironmentVariable.builder().name("TF_VAR_cc_auth_token").value(authToken)
            .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(EnvironmentVariable.builder().name("TF_VAR_cc_host").value(requestContext.getHeader(
            "HOST"))
            .type(EnvironmentVariableType.PLAINTEXT).build());
        String buildName = "";
        switch (cluster.getCloud()){

            case AWS:
                buildName = "capillary-cloud-tf-apply";
                break;
        }
        StartBuildRequest startBuildRequest = StartBuildRequest.builder().projectName(buildName)
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
