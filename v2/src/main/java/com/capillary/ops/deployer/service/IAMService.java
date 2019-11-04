package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Environment;
import com.capillary.ops.deployer.bo.Kube2IamConfiguration;
import com.capillary.ops.deployer.service.interfaces.IIAMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.ListRolesRequest;
import software.amazon.awssdk.services.iam.model.ListRolesResponse;
import software.amazon.awssdk.services.iam.model.Role;
import software.amazon.awssdk.services.iam.model.Tag;

import java.util.List;
import java.util.stream.Collectors;

public class IAMService implements IIAMService {

    private static final Logger logger = LoggerFactory.getLogger(IAMService.class);

    private final String K8S_ROLE_PATH = "/k8s/";
    private final String APP_NAME_KEY = "k8s-app";
    private final String APP_FAMILY_KEY = "application-family";

    @Override
    public String getApplicationRole(Application application, Environment environment) {
        String roleName = null;
        IamClient iamClient;
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
            ListRolesResponse rolesList = iamClient.listRoles(ListRolesRequest.builder().pathPrefix(K8S_ROLE_PATH).build());
            Role appRole = rolesList.roles().stream().filter(r -> matchTags(r.tags(), appName, appFamily)).collect(Collectors.toList()).get(0);
            roleName = appRole.roleName();
        } catch (Exception e) {
            logger.error("Error fetching IAM roles", e);
        }
        return roleName;
    }

    private boolean matchTags(List<Tag> tags, String appName, String appFamily) {
        if (tags.stream().anyMatch(t -> t.key().equals(APP_NAME_KEY))
                && tags.stream().anyMatch(t -> t.key().equals(APP_FAMILY_KEY))) {
            if (tags.stream().filter(t -> t.key().equals(APP_NAME_KEY)).collect(Collectors.toList()).get(0).value().equals(appName)
                    && tags.stream().filter(t -> t.key().equals(APP_FAMILY_KEY)).collect(Collectors.toList()).get(0).value().equals(appFamily)) {
                return true;
            }
        }
        return false;
    }
}
