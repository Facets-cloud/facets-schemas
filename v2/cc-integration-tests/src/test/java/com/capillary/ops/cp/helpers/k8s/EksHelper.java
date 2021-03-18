package com.capillary.ops.cp.helpers.k8s;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.eks.AmazonEKS;
import com.amazonaws.services.eks.AmazonEKSClientBuilder;
import com.amazonaws.services.eks.model.DescribeClusterRequest;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.capillary.ops.cp.bo.K8sConfig;
import com.capillary.ops.cp.helpers.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class EksHelper implements K8sHelper {

    @Value("${stack.name}")
    private String STACK_NAME = "cc-stack-crm";

    @Value("${cluster.id}")
    private String CLUSTER_ID;

    @Autowired
    CommonUtils commonUtils;

    @Override
    public K8sConfig getK8sConfig() throws Exception {
        HashMap<String, String> deploymentContextJson =  commonUtils.getDeploymentContext();
        JsonObject cluster = new Gson().fromJson(deploymentContextJson.get("cluster"), JsonObject.class);
        K8sConfig k8sConfig = new K8sConfig();

        String roleARN = cluster.get("roleARN").getAsString();
        String externalId = cluster.get("externalId").getAsString();
        String awsRegion = cluster.get("awsRegion").getAsString();
        String k8sClusterName = cluster.get("name").getAsString() + "-k8s-cluster";

        AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider())
                .withRegion(awsRegion)
                .build();

        AssumeRoleRequest roleRequest = new AssumeRoleRequest()
                .withDurationSeconds(3600)
                .withRoleArn(roleARN)
                .withExternalId(externalId)
                .withRoleSessionName("integration-test-session");
        AssumeRoleResult roleResponse = stsClient.assumeRole(roleRequest);
        Credentials sessionCredentials = roleResponse.getCredentials();

        BasicSessionCredentials awsCredentials = new BasicSessionCredentials(
                sessionCredentials.getAccessKeyId(),
                sessionCredentials.getSecretAccessKey(),
                sessionCredentials.getSessionToken());

        AmazonEKS eks = AmazonEKSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(awsRegion)
                .build();

        DescribeClusterRequest request = new DescribeClusterRequest();
        request.setName(k8sClusterName);

        k8sConfig.setKubernetesApiEndpoint(eks.describeCluster(request).getCluster().getEndpoint());
        k8sConfig.setKubernetesToken(eks.describeCluster(request).getCluster().getClientRequestToken());

        return k8sConfig;
    }
}
