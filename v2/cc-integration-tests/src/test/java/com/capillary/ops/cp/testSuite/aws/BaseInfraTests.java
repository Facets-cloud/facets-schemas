package com.capillary.ops.cp.testSuite.aws;

import com.capillary.ops.cp.App;
import com.capillary.ops.cp.helpers.AwsCommonUtils;
import com.capillary.ops.cp.helpers.ClusterInfo;
import com.capillary.ops.cp.helpers.CommonUtils;

import com.capillary.ops.cp.helpers.k8s.K8sTestUtils;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import software.amazon.awssdk.services.autoscaling.model.AutoScalingGroup;
import software.amazon.awssdk.services.ec2.model.IpPermission;
import software.amazon.awssdk.services.ec2.model.Tag;
import software.amazon.awssdk.services.ec2.model.VpcEndpoint;
import software.amazon.awssdk.services.ec2.model.VpcEndpointType;
import software.amazon.awssdk.services.ec2.model.VpcPeeringConnection;
import software.amazon.awssdk.services.eks.EksClient;
import software.amazon.awssdk.services.eks.model.DescribeClusterRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;

@TestPropertySource(locations="classpath:test.properties")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {App.class})
@SpringBootTest
public class BaseInfraTests {

    private static final Logger logger = LoggerFactory.getLogger(BaseInfraTests.class);

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private AwsCommonUtils awsCommonUtils;

    @Autowired
    private ClusterInfo clusterInfo;

    @Autowired
    private K8sTestUtils k8sTestUtils;

    @Value("${codebuild.vpcId}")
    private String codeBuildVpc;

    @Value("${cluster.release.stream}")
    private String clusterReleaseStream;

    @Test
    public void checkClusterVpc() {
        Assert.assertEquals("Found few or more vpcs with the cluster CIDR. Expected only one. ", 1, clusterInfo.getClusterVpcs().size());
    }

    @Test
    public void checkClusterToolingPeering() {
        logger.info("Executing test case to check if peering is created between tooling vpc and the cluster vpc. ");
        long numToolingPeerings = clusterInfo.getClusterVpcPeeringConnections().stream().filter(x -> x.accepterVpcInfo().vpcId().equals(codeBuildVpc)).count();
        Assert.assertEquals("Expected only one peering connection between cluster and codebuild vpc. ", 1, numToolingPeerings);
    }

    @Test
    public void checkAdditionalPeeringRequesterEntry() {
        commonUtils.getAdditionalPeerings().forEach(additionalPeering -> {

            List<VpcPeeringConnection> peeringConnections = clusterInfo.getClusterVpcPeeringConnections().stream().filter(pc -> pc.accepterVpcInfo().region().equals(additionalPeering.getRegion()) && pc.accepterVpcInfo().vpcId().equals(additionalPeering.getVcpId()) && pc.accepterVpcInfo().ownerId().equals(additionalPeering.getAccountId())).collect(Collectors.toList());

            Assert.assertEquals("Expected Exactly 1 VpcPeeringConnection between additional peering instance and the cluster vpc. ", 1, peeringConnections.size());
        });
    }

    @Test
    public void checkAdditionalPeeringPublicPrivateRouteTablesRouteCreationForAdditionalPeeringInstances() {
        commonUtils.getAdditionalPeerings().forEach(additionalPeering -> {
            Assert.assertEquals("Expected 5 route tables exactly. ", 5, clusterInfo.getClusterRouteTables().size());
            Optional<VpcPeeringConnection> peeringConnection = clusterInfo.getClusterVpcPeeringConnections().stream().filter(pc -> pc.accepterVpcInfo().vpcId().equals(additionalPeering.getVcpId()) && pc.accepterVpcInfo().ownerId().equals(additionalPeering.getAccountId()) && pc.accepterVpcInfo().region().equals(additionalPeering.getRegion())).findFirst();
            Assert.assertTrue(peeringConnection.isPresent());

            String appPeeringConnId = peeringConnection.get().vpcPeeringConnectionId();
            String appCIDR = additionalPeering.getCidr();

            clusterInfo.getClusterRouteTables().forEach(table -> Assert.assertEquals("Failed to find a route in the table " + table.routeTableId(), 1, table.routes().stream().filter(route -> route.destinationCidrBlock() != null && route.destinationCidrBlock().equals(appCIDR) && route.vpcPeeringConnectionId() != null && route.vpcPeeringConnectionId().equals(appPeeringConnId)).count()));
        });
    }

    @Test
    public void checkKubernetesVersion() {
        EksClient eksClient = awsCommonUtils.getEKSClient();
        DescribeClusterRequest clusterRequest = DescribeClusterRequest.builder().name(commonUtils.getClusterName() + "-k8s-cluster").build();
        String clusterVersion = eksClient.describeCluster(clusterRequest).cluster().version();
        Assert.assertEquals(commonUtils.getK8sClusterVersion(), clusterVersion);
        eksClient.close();
    }

    @Test
    public void checkClusterVpcSecurityGroup() {
        Assert.assertTrue("Cannot find security group that is created for the cluster vpc ", clusterInfo.getClusterSecurityGroup().isPresent());

        List<IpPermission> ipPermissions = clusterInfo.getClusterSecurityGroup().get().ipPermissions();
        Assert.assertEquals("Found multiple ingress rules. Expected only one. ", 1, ipPermissions.size());
        IpPermission ingressRules = ipPermissions.get(0);


        List<IpPermission> ipPermissionsEgress = clusterInfo.getClusterSecurityGroup().get().ipPermissionsEgress();
        Assert.assertEquals("Found multiple egress rules. Expected only one. ", 1, ipPermissionsEgress.size());
        IpPermission egressRules = ipPermissionsEgress.get(0);

        Assert.assertEquals("-1", ingressRules.ipProtocol());
        Assert.assertEquals("-1", egressRules.ipProtocol());

        Assert.assertEquals("Expected only one cidr block for ingress rule. ", 1, ingressRules.ipRanges().size());
        Assert.assertEquals("Expected only one cidr block for egress rule. ", 1, egressRules.ipRanges().size());

        Assert.assertEquals("Ingress rule cidr is not matching with the cluster vpcCIDR ",awsCommonUtils.getVpcCIDR(), ingressRules.ipRanges().get(0).cidrIp());
        Assert.assertEquals("Egress rule ip range is not matching 0.0.0.0/0 ","0.0.0.0/0", egressRules.ipRanges().get(0).cidrIp());

        Assert.assertNull("Ingress from port should be a null value. ", ingressRules.fromPort());
        Assert.assertNull("Egress from port should be a null value. ", egressRules.fromPort());
        Assert.assertNull("Ingress to port should be a null value. ", ingressRules.toPort());
        Assert.assertNull("Egress to port should be a null value. ", egressRules.toPort());
    }

    @Test
    public void checkVpcEndpointForS3() {
        List<VpcEndpoint> vpcEndpoints = clusterInfo.getVpcEndpoints().stream().filter(x -> x.serviceName().equals("com.amazonaws." + awsCommonUtils.getAwsRegion() + ".s3") && x.vpcEndpointType().equals(VpcEndpointType.GATEWAY) && x.tags().contains(Tag.builder().key("Name").value(awsCommonUtils.getClusterName() + "-s3vpc-endpoint").build()) && !x.privateDnsEnabled()).collect(Collectors.toList());
        Assert.assertEquals("Expected exactly one s3 endpoint. ", 1, vpcEndpoints.size());
    }

    @Test
    public void checkAutoScalingGroups() {
        logger.info("Executing test case that checks the details of auto scaling groups.");
        Assert.assertEquals(2, clusterInfo.getClusterAutoScalingGroups().size());

        Optional<AutoScalingGroup> spotGroup = clusterInfo.getClusterAutoScalingGroups().stream().filter(x -> Pattern.matches(".+spotfleet-node-group.+", x.autoScalingGroupName())).findFirst();

        Optional<AutoScalingGroup> onDemandFallbackGroup = clusterInfo.getClusterAutoScalingGroups().stream().filter(x -> Pattern.matches(".+fallback-ondemand-node-group.+", x.autoScalingGroupName())).findFirst();

        Assert.assertTrue(spotGroup.isPresent());
        Assert.assertTrue(onDemandFallbackGroup.isPresent());

        List<String> spotInstanceTypes = spotGroup.get().mixedInstancesPolicy().launchTemplate().overrides().stream().map(software.amazon.awssdk.services.autoscaling.model.LaunchTemplateOverrides::instanceType).collect(Collectors.toList());

        List<String> clusterSpotInstanceTypes = awsCommonUtils.getSpotInstanceTypes();

        Assert.assertTrue(spotInstanceTypes.size() == clusterSpotInstanceTypes.size() && spotInstanceTypes.containsAll(clusterSpotInstanceTypes) && clusterSpotInstanceTypes.containsAll(spotInstanceTypes));
    }

    @Test
    public void checkChaosTestingNameSpace() throws Exception {
        Assume.assumeThat(clusterReleaseStream.toUpperCase(), is("QA"));
        Assert.assertTrue("Expected chaos testing namespace. ", k8sTestUtils.getClusterNameSpace("chaos-testing").isPresent());
    }

    @Test
    public void checkChaosTestingRole() throws Exception {
        Assume.assumeThat(clusterReleaseStream.toUpperCase(), is("QA"));
        Assert.assertTrue("Expected chaos mesh user role.", k8sTestUtils.getRole("chaos-testing", "chaos-mesh-user-role").isPresent());
    }

    @Test
    public void checkChaosTestingServiceAccount() {
        Assume.assumeThat(clusterReleaseStream.toUpperCase(), is("QA"));
        Assert.assertTrue("Expected chaos mesh user service account. ", k8sTestUtils.getServiceAccount("chaos-testing", "chaos-mesh-user").isPresent());
    }

    @Test
    public void checkChaosTestingRoleBinding() throws Exception {
        Assume.assumeThat(clusterReleaseStream.toUpperCase(), is("QA"));
        Assert.assertTrue("Expected chaos mesh user role binding. ", k8sTestUtils.getRoleBinding("chaos-testing", "chaos-mesh-user-role-binding").isPresent());
    }

    @Test
    public void checkChaosTestingClusterRoleBinding() {
        Assume.assumeThat(clusterReleaseStream.toUpperCase(), is("QA"));
        Assert.assertTrue("Expected chaos mesh user view cr binding in cluster role bindings. ", k8sTestUtils.getClusterRoleBinding("chaos-testing", "chaos-mesh-user-view-cr-binding").isPresent());
    }

    @Test
    public void checkEcrTokenRefreshers() {

        commonUtils.getArtifactories().forEach(artifactory -> {
            Assert.assertFalse(k8sTestUtils.getSecret("default", String.format("aws-ecr-token-refresher-secrets-%s", artifactory)).isPresent());

            Assert.assertFalse(k8sTestUtils.getSecret("default", String.format("aws-ecr-token-%s", artifactory)).isPresent());

            Assert.assertFalse(k8sTestUtils.getCronJob("default", String.format("ecr-token-refresher-%s", artifactory)).isPresent());

            Assert.assertFalse(k8sTestUtils.getServiceAccount("default", String.format("ecr-token-refresher-%s", artifactory)).isPresent());

            Assert.assertFalse(k8sTestUtils.getClusterRole("default", String.format("ecr-token-refresher-%s", artifactory)).isPresent());

            Assert.assertFalse(k8sTestUtils.getClusterRoleBinding("default", String.format("ecr-token-refresher-%s", artifactory)).isPresent());
        });
    }

    @Test
    public void coreDNSLifeCycleCheck() {
        Assume.assumeThat("Cluster type is not PROD. Skipping the test case. ", clusterReleaseStream.toUpperCase(), is("PROD"));

        Optional<Map<String, String>> nodeSelector = k8sTestUtils.getNodeSelector("coredns", "kube-system");

        Assert.assertTrue("Node selector cannot be null for prod cluster coredns. ", nodeSelector.isPresent());

        Assert.assertTrue("ccLifecycle should be present in the node selector of coredns. ", nodeSelector.get().containsKey("ccLifecycle"));

        Assert.assertEquals("ccLifecycle should have value ondemand in the node selector of coredns. ", "ondemand", nodeSelector.get().get("ccLifecycle"));
    }

}
