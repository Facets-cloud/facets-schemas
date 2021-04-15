package com.capillary.ops.cp.testSuite.aws;

import com.capillary.ops.cp.App;
import com.capillary.ops.cp.helpers.AwsCommonUtils;


import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.autoscaling.model.AutoScalingGroup;
import software.amazon.awssdk.services.autoscaling.model.DescribeAutoScalingGroupsRequest;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeVpcPeeringConnectionsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsRequest;
import software.amazon.awssdk.services.ec2.model.Filter;
import software.amazon.awssdk.services.ec2.model.Vpc;
import software.amazon.awssdk.services.ec2.model.VpcPeeringConnection;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Ignore
@TestPropertySource(locations="classpath:test.properties")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {App.class})
@SpringBootTest
public class BaseInfraTests {

    private static final Logger logger = LoggerFactory.getLogger(BaseInfraTests.class);

    @Autowired
    private AwsCommonUtils awsCommonUtils;

    @Test
    public void checkClusterVpc() {
        Ec2Client ec2Client = awsCommonUtils.getEC2Client();

        logger.info("executing test case that checks if vpc is created for the cluster or not.");
        DescribeVpcsRequest vpcsRequest = DescribeVpcsRequest.builder().filters(Filter.builder().name("isDefault").values("false").build(), Filter.builder().name("cidr").values(awsCommonUtils.getVpcCIDR()).build()).build();
        logger.debug("describing vpcs request " + vpcsRequest);

        List<Vpc> vpcList = ec2Client.describeVpcs(vpcsRequest).vpcs();
        Assert.assertEquals(1, vpcList.size());
        ec2Client.close();
    }

    @Test
    public void checkAutoScalingGroups() {
        AutoScalingClient autoScalingClient = awsCommonUtils.getAutoScalingGroupsClient();
        logger.info("Executing test case that checks the details of auto scaling groups.");
        DescribeAutoScalingGroupsRequest request = DescribeAutoScalingGroupsRequest.builder().build();

        List<AutoScalingGroup> autoScalingGroups = autoScalingClient.describeAutoScalingGroups(request).autoScalingGroups();
        logger.debug("Found the following autoscaling groups in the region of the cluster.");
        autoScalingGroups.forEach(x -> logger.debug("{}", x));

        List<AutoScalingGroup> clusterAutoScalingGroups = autoScalingGroups.stream().filter(x -> Pattern.matches(awsCommonUtils.getClusterName()+".+", x.autoScalingGroupName())).collect(Collectors.toList());
        logger.info("found the following autoscaling groups for the cluster. ");
        clusterAutoScalingGroups.forEach(x -> logger.info("{}", x));

        Assert.assertEquals(2, clusterAutoScalingGroups.size());

        Optional<AutoScalingGroup> spotGroup = clusterAutoScalingGroups.stream().filter(x -> Pattern.matches(".+spotfleet-node-group.+", x.autoScalingGroupName())).findFirst();

        Optional<AutoScalingGroup> onDemandFallbackGroup = clusterAutoScalingGroups.stream().filter(x -> Pattern.matches(".+fallback-ondemand-node-group.+", x.autoScalingGroupName())).findFirst();

        Assert.assertTrue(spotGroup.isPresent());
        Assert.assertTrue(onDemandFallbackGroup.isPresent());

        List<String> spotInstanceTypes = spotGroup.get().mixedInstancesPolicy().launchTemplate().overrides().stream().map(software.amazon.awssdk.services.autoscaling.model.LaunchTemplateOverrides::instanceType).collect(Collectors.toList());

        List<String> clusterSpotInstanceTypes = awsCommonUtils.getSpotInstanceTypes();

        Assert.assertTrue(spotInstanceTypes.size() == clusterSpotInstanceTypes.size() && spotInstanceTypes.containsAll(clusterSpotInstanceTypes) && clusterSpotInstanceTypes.containsAll(spotInstanceTypes));
        autoScalingClient.close();
    }

    @Test
    public void checkClusterToolingPeering() {
        Ec2Client ec2Client = awsCommonUtils.getEC2Client();
        logger.info("Executing test case to check if peering is created between tooling vpc and the cluster vpc. ");
        Filter accepterVpcId = Filter.builder().name("accepter-vpc-info.vpc-id").values("vpc-b1e65fd4").build();
        Filter requesterVpcCIDR = Filter.builder().name("requester-vpc-info.cidr-block").values(awsCommonUtils.getVpcCIDR()).build();

        DescribeVpcPeeringConnectionsRequest vpcPeeringConnectionsRequest = DescribeVpcPeeringConnectionsRequest.builder().filters(accepterVpcId, requesterVpcCIDR).build();

        List<VpcPeeringConnection> vpcPeeringConnections = ec2Client.describeVpcPeeringConnections(vpcPeeringConnectionsRequest).vpcPeeringConnections();
        logger.info("Found the following peering connections.");
        vpcPeeringConnections.forEach(x -> logger.info("{}", x));
        Assert.assertEquals(1, vpcPeeringConnections.size());
        ec2Client.close();
    }

    @Test
    public void checkAdditionalPeering() {

    }

}
