package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.bo.Kube2IamConfiguration;
import com.capillary.ops.deployer.service.interfaces.IIAMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.ListRolesRequest;
import software.amazon.awssdk.services.iam.model.ListRolesResponse;
import software.amazon.awssdk.services.iam.model.Role;

@Profile("!dev")
@Service
public class IAMService implements IIAMService {

    private static final Logger logger = LoggerFactory.getLogger(IAMService.class);

    private final String ROOT_LEVEL = "/deployer/";

    @Override
    public String getApplicationRole(Application application, Environment environment) {
        IamClient iamClient;
        String roleName = null;
        try {
            Kube2IamConfiguration kube2IamConfiguration = environment.getEnvironmentConfiguration().getKube2IamConfiguration();
            iamClient = IamClient.builder().region(Region.AWS_GLOBAL).credentialsProvider(() -> new AwsCredentials() {
                @Override
                public String accessKeyId() {
                    return kube2IamConfiguration.getAwsAccessKeyID();
                }

                @Override
                public String secretAccessKey() {
                    return kube2IamConfiguration.getAwsSecretAccessKey();
                }
            }).build();
            String appName = application.getName();
            String appFamily = application.getApplicationFamily().name();
            String rolePath = ROOT_LEVEL + appFamily;
            ListRolesResponse rolesList = iamClient.listRoles(ListRolesRequest.builder().pathPrefix(rolePath).build());
            Role appRole = rolesList.roles().stream().filter(role -> role.roleName().equals(appName)).findFirst().orElse(null);
            if (appRole != null) {
                roleName = appRole.roleName();
            }
        } catch (Exception e) {
            logger.error("Error fetching IAM roles", e);
        }
        return roleName;
    }
}
