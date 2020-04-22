package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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
@Loggable
public class AwsCodeBuildService implements TFBuildService {

    private static final String CLUSTER_ID = "CLUSTER_ID";

    private static final Region BUILD_REGION = Region.US_WEST_1;
    public static final String BUILD_NAME = "capillary-cloud-tf-apply";
    public static final String HOST = "TF_VAR_cc_host";
    public static final String RELEASE_TYPE = "TF_VAR_release_type";
    public static final String CC_AUTH_TOKEN = "TF_VAR_cc_auth_token";

    @Value("${internalApiAuthToken}")
    private String authToken;

    @Autowired
    private HttpServletRequest requestContext;

    @Autowired
    private StackRepository stackRepository;

    /**
     * Deploy the latest build in the specified clusterId
     *
     * @param cluster     Cluster Information
     * @param releaseType
     * @return
     */
    @Override
    public String deployLatest(AbstractCluster cluster, ReleaseType releaseType) {
        Optional<Stack> stackO = stackRepository.findById(cluster.getStackName());
        if (!stackO.isPresent()) {
            throw new NotFoundException("The Stack for this cluster does not exist NOW");
        }
        Stack stack = stackO.get();
        //DONE: Check if code build is defined for the said cloud
        List<EnvironmentVariable> environmentVariables = new ArrayList<>();
        environmentVariables.add(EnvironmentVariable.builder().name(CLUSTER_ID).value(cluster.getId())
            .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(EnvironmentVariable.builder().name(CC_AUTH_TOKEN).value(authToken)
            .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(
            EnvironmentVariable.builder().name(HOST).value(requestContext.getHeader("HOST"))
                .type(EnvironmentVariableType.PLAINTEXT).build());
        environmentVariables.add(EnvironmentVariable.builder().name(RELEASE_TYPE).value(releaseType.name())
            .type(EnvironmentVariableType.PLAINTEXT).build());
        String buildName = "";
        switch (cluster.getCloud()) {

            case AWS:
                buildName = BUILD_NAME;
                break;
        }
        StartBuildRequest startBuildRequest =
            StartBuildRequest.builder().projectName(buildName).environmentVariablesOverride(environmentVariables)
                .build();
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
