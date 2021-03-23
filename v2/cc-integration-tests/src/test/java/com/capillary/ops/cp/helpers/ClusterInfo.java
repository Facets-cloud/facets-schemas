package com.capillary.ops.cp.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.autoscaling.model.AutoScalingGroup;
import software.amazon.awssdk.services.autoscaling.model.DescribeAutoScalingGroupsRequest;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ClusterInfo {

    private static final Logger logger = LoggerFactory.getLogger(ClusterInfo.class);

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private AwsCommonUtils awsCommonUtils;

    private List<Vpc> clusterVpcs = new ArrayList<>();

    private List<VpcPeeringConnection> clusterVpcPeeringConnections = new ArrayList<>();

    private List<RouteTable> clusterRouteTables = new ArrayList<>();

    private SecurityGroup clusterSecurityGroup = null;

    private List<VpcEndpoint> vpcEndpoints = new ArrayList<>();

    private List<AutoScalingGroup> clusterAutoScalingGroups = new ArrayList<>();

    @Autowired
    public ClusterInfo(CommonUtils commonUtils, AwsCommonUtils awsCommonUtils) {
        Ec2Client ec2Client = awsCommonUtils.getEC2Client();
        AutoScalingClient autoScalingClient = awsCommonUtils.getAutoScalingGroupsClient();

        populateClusterVpcs(ec2Client, awsCommonUtils);
        populateClusterVpcPeeringConnections(ec2Client);
        populateRouteTables(ec2Client);
        populateClusterSecurityGroups(ec2Client, commonUtils);
        populateClusterVpcEndPoints(ec2Client);
        populateAutoScalingGroups(autoScalingClient, awsCommonUtils);

        ec2Client.close();
        autoScalingClient.close();
    }

    private void populateClusterVpcs(Ec2Client ec2Client, AwsCommonUtils awsCommonUtils) {
        DescribeVpcsRequest vpcsRequest = DescribeVpcsRequest.builder().filters(Filter.builder().name("isDefault").values("false").build(), Filter.builder().name("cidr").values(awsCommonUtils.getVpcCIDR()).build()).build();
        logger.debug("describing vpcs request " + vpcsRequest);
        this.clusterVpcs = ec2Client.describeVpcs(vpcsRequest).vpcs();
    }

    private void populateClusterVpcPeeringConnections(Ec2Client ec2Client) {
        if (clusterVpcs.size() > 0){
            Filter requesterVpcCIDR = Filter.builder().name("requester-vpc-info.vpc-id").values(clusterVpcs.get(0).vpcId()).build();
            DescribeVpcPeeringConnectionsRequest vpcPeeringConnectionsRequest = DescribeVpcPeeringConnectionsRequest.builder().filters(requesterVpcCIDR).build();
            clusterVpcPeeringConnections = ec2Client.describeVpcPeeringConnections(vpcPeeringConnectionsRequest).vpcPeeringConnections();
            clusterVpcPeeringConnections.forEach(x -> logger.debug(x.vpcPeeringConnectionId()));
        }
    }

    private void populateRouteTables(Ec2Client ec2Client) {
        if (clusterVpcs.size() > 0){
            Filter routeTablesVpcId = Filter.builder().name("vpc-id").values(clusterVpcs.get(0).vpcId()).build();
            Filter nonMainAssociationRouteTables = Filter.builder().name("association.main").values("false").build();
            DescribeRouteTablesRequest routeTablesRequest = DescribeRouteTablesRequest.builder().filters(routeTablesVpcId, nonMainAssociationRouteTables).build();

            clusterRouteTables = ec2Client.describeRouteTables(routeTablesRequest).routeTables();
        }
    }

    private void populateClusterSecurityGroups(Ec2Client ec2Client, CommonUtils commonUtils) {
        if (clusterVpcs.size() > 0) {
            Filter vpcId = Filter.builder().name("vpc-id").values(clusterVpcs.get(0).vpcId()).build();
            DescribeSecurityGroupsRequest securityGroupsRequest = DescribeSecurityGroupsRequest.builder().filters(vpcId).build();
            List<SecurityGroup> securityGroups = ec2Client.describeSecurityGroups(securityGroupsRequest).securityGroups();
            Optional<SecurityGroup> securityGroup = securityGroups.stream().filter(x -> Pattern.matches(String.format("allow_all_%s-default.+", commonUtils.getClusterName()), x.groupName())).findFirst();
            securityGroup.ifPresent(x -> clusterSecurityGroup = x);
        }
    }

    private void populateClusterVpcEndPoints(Ec2Client ec2Client) {
        if (clusterVpcs.size() > 0) {
            Filter vpcId = Filter.builder().name("vpc-id").values(clusterVpcs.get(0).vpcId()).build();
            Filter endPointStatus = Filter.builder().name("vpc-endpoint-state").values("available").build();

            DescribeVpcEndpointsRequest connectionsRequest = DescribeVpcEndpointsRequest.builder().filters(vpcId, endPointStatus).build();
            vpcEndpoints = ec2Client.describeVpcEndpoints(connectionsRequest).vpcEndpoints();
        }
    }

    private void populateAutoScalingGroups(AutoScalingClient autoScalingClient, AwsCommonUtils awsCommonUtils) {
        DescribeAutoScalingGroupsRequest request = DescribeAutoScalingGroupsRequest.builder().build();
        List<AutoScalingGroup> autoScalingGroups = autoScalingClient.describeAutoScalingGroups(request).autoScalingGroups();
        logger.debug("Found the following autoscaling groups in the region of the cluster.");
        autoScalingGroups.forEach(x -> logger.debug("{}", x));

        List<AutoScalingGroup> clusterAutoScalingGroups = autoScalingGroups.stream().filter(x -> Pattern.matches(awsCommonUtils.getClusterName()+".+", x.autoScalingGroupName())).collect(Collectors.toList());
        logger.debug("found the following autoscaling groups for the cluster. ");
        clusterAutoScalingGroups.forEach(x -> logger.info("{}", x));
        this.clusterAutoScalingGroups = clusterAutoScalingGroups;
    }

    public List<Vpc> getClusterVpcs() {
        return clusterVpcs;
    }

    public void setClusterVpcs(List<Vpc> clusterVpcs) {
        this.clusterVpcs = clusterVpcs;
    }

    public List<VpcPeeringConnection> getClusterVpcPeeringConnections() {
        return clusterVpcPeeringConnections;
    }

    public void setClusterVpcPeeringConnections(List<VpcPeeringConnection> clusterVpcPeeringConnections) {
        this.clusterVpcPeeringConnections = clusterVpcPeeringConnections;
    }

    public List<RouteTable> getClusterRouteTables() {
        return clusterRouteTables;
    }

    public void setClusterRouteTables(List<RouteTable> clusterRouteTables) {
        this.clusterRouteTables = clusterRouteTables;
    }

    public Optional<SecurityGroup> getClusterSecurityGroup() {
        return Optional.ofNullable(clusterSecurityGroup);
    }

    public void setClusterSecurityGroup(SecurityGroup clusterSecurityGroup) {
        this.clusterSecurityGroup = clusterSecurityGroup;
    }

    public List<VpcEndpoint> getVpcEndpoints() {
        return vpcEndpoints;
    }

    public void setVpcEndpoints(List<VpcEndpoint> vpcEndpoints) {
        this.vpcEndpoints = vpcEndpoints;
    }

    public List<AutoScalingGroup> getClusterAutoScalingGroups() {
        return clusterAutoScalingGroups;
    }

    public void setClusterAutoScalingGroups(List<AutoScalingGroup> clusterAutoScalingGroups) {
        this.clusterAutoScalingGroups = clusterAutoScalingGroups;
    }
}
