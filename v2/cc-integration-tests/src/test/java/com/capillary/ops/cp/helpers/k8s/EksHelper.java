package com.capillary.ops.cp.helpers.k8s;

import com.capillary.ops.cp.bo.K8sConfig;
import com.capillary.ops.cp.helpers.AwsCommonUtils;
import com.capillary.ops.cp.helpers.CommonUtils;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.eks.*;
import software.amazon.awssdk.services.eks.model.Cluster;
import software.amazon.awssdk.services.eks.model.DescribeClusterRequest;

@Component
@TestPropertySource(locations="classpath:test.properties")
public class EksHelper implements K8sHelper {

    @Value("${stack.name}")
    private String STACK_NAME;

    @Value("${cluster.id}")
    private String CLUSTER_ID;

    @Autowired
    CommonUtils commonUtils;

    @Autowired
    AwsCommonUtils awsCommonUtils;

    @Override
    public K8sConfig getK8sConfig() throws Exception {
        JsonObject deploymentContextJson =  commonUtils.getDeploymentContext();
        JsonObject cluster = deploymentContextJson.getAsJsonObject("cluster");
        K8sConfig k8sConfig = new K8sConfig();

        String awsRegion = cluster.get("awsRegion").getAsString();
        String k8sClusterName = cluster.get("name").getAsString() + "-k8s-cluster";

        EksClient eksClient = EksClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(awsCommonUtils.getSTSCredentialsProvider())
                .build();

        DescribeClusterRequest request = DescribeClusterRequest
                .builder()
                .name(k8sClusterName)
                .build();

        Cluster eksCluster = eksClient.describeCluster(request).cluster();
        k8sConfig.setKubernetesApiEndpoint(eksCluster.endpoint());
        k8sConfig.setKubernetesToken(eksCluster.clientRequestToken());

        return k8sConfig;
    }
}
