package com.capillary.ops.cp.helpers.k8s;

import com.capillary.ops.cp.bo.K8sConfig;
import com.capillary.ops.cp.helpers.AwsCommonUtils;
import com.capillary.ops.cp.helpers.CommonUtils;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.signer.Aws4Signer;
import software.amazon.awssdk.auth.signer.params.Aws4PresignerParams;
import software.amazon.awssdk.http.SdkHttpFullRequest;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.eks.*;
import software.amazon.awssdk.services.eks.model.Cluster;
import software.amazon.awssdk.services.eks.model.DescribeClusterRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Clock;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
@TestPropertySource(locations="classpath:test.properties")
public class EksHelper implements K8sHelper {

    @Value("${stack.name}")
    private String STACK_NAME;

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

        //EksClient eksClient = new DefaultEksClientBuilder().region(Region.of(awsRegion)).credentialsProvider(awsCommonUtils.getSTSCredentialsProvider()).build()

        DescribeClusterRequest request = DescribeClusterRequest
                .builder()
                .name(k8sClusterName)
                .build();

        Cluster eksCluster = eksClient.describeCluster(request).cluster();
        k8sConfig.setKubernetesApiEndpoint(eksCluster.endpoint());
        k8sConfig.setKubernetesToken(getAuthenticationToken(awsCommonUtils.getSTSCredentialsProvider(),Region.of(commonUtils.getAwsRegion()),k8sClusterName));

        return k8sConfig;
    }

    public String getAuthenticationToken(AwsCredentialsProvider awsAuth, Region awsRegion, String clusterName) {
        try {
            SdkHttpFullRequest requestToSign = SdkHttpFullRequest
                    .builder()
                    .method(SdkHttpMethod.GET)
                    .uri(getStsRegionalEndpointUri(awsRegion))
                    .appendHeader("x-k8s-aws-id", clusterName)
                    .appendRawQueryParameter("Action", "GetCallerIdentity")
                    .appendRawQueryParameter("Version", "2011-06-15")
                    .build();

            Date one = new Date();
            Aws4PresignerParams presignerParams = Aws4PresignerParams.builder()
                    .awsCredentials(awsAuth.resolveCredentials())
                    .signingRegion(awsRegion)
                    .signingName("sts")
                    .signingClockOverride(Clock.systemUTC())
                    .expirationTime(new Date(one.getTime() + 120000).toInstant())
                    .build();

            SdkHttpFullRequest signedRequest = Aws4Signer.create().presign(requestToSign, presignerParams);

            String encodedUrl = Base64.getUrlEncoder().withoutPadding().encodeToString(signedRequest.getUri().toString().getBytes(StandardCharsets.UTF_8));
            return ("k8s-aws-v1." + encodedUrl);
        } catch (Exception e) {
            String errorMessage = "A problem occurred generating an Eks authentication token for cluster: " + clusterName;
            throw new RuntimeException(errorMessage, e);
        }
    }

    public URI getStsRegionalEndpointUri(Region awsRegion) {
        try {
            return new URI("https", String.format("sts.%s.amazonaws.com", awsRegion.id()), "/", null);
        } catch (URISyntaxException shouldNotHappen) {
            String errorMessage = "An error occurred creating the STS regional endpoint Uri";
            throw new RuntimeException(errorMessage, shouldNotHappen);
        }
    }
}
