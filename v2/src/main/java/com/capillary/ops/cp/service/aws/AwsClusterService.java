package com.capillary.ops.cp.service.aws;

import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.requests.AwsClusterRequest;
import com.capillary.ops.cp.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * AWS implementation of the Facade
 */
@Component
public class AwsClusterService implements ClusterService<AwsCluster, AwsClusterRequest> {

    @Autowired
    private AwsAssumeRoleService awsAssumeRoleService;

    @Override
    public AwsCluster createCluster(AwsClusterRequest request) {
        //DONE: Validations
        //1. Test the arn & external Id connectivity
        if (!awsAssumeRoleService.testRoleAccess(request.getRoleARN(), request.getExternalId())) {
            throw new RuntimeException("No Access for given Role");
        }
        //DONE: Variable Assignment
        AwsCluster cluster = new AwsCluster(request.getClusterName());
        cluster.setRoleARN(request.getRoleARN());
        cluster.setTz(request.getTz());
        cluster.setExternalId(request.getExternalId());
        cluster.setAwsRegion(request.getRegion().getName());
        cluster.setAzs(request.getAzs());
        cluster.setReleaseStream(request.getReleaseStream());
        //TODO: Variable Generations
        //1. Generate CIDR.
        cluster.setVpcCIDR("10.250.0.0/16");
        cluster.setStackName(request.getStackName());
        return cluster;
    }
}
