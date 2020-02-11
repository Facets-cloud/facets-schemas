package com.capillary.ops.cp.service.aws;

import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.requests.AwsClusterRequest;
import com.capillary.ops.cp.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * AWS implementation of the Facade
 */
@Component
public class AwsClusterService implements ClusterService<AwsCluster, AwsClusterRequest> {

    @Autowired
    AwsAssumeRoleService awsAssumeRoleService;

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
        cluster.setExternalId(request.getExternalId());
        cluster.setAwsRegion(request.getRegion());
        cluster.setAzs(request.getAzs());
        //TODO: Variable Generations
        //1. Generate CIDRs etc.
        cluster.setPrivateSubnetCIDR(new ArrayList<String>() {{
            add("10.250.100.0/24");
            add("10.250.101.0/24");
        }});
        cluster.setPublicSubnetCIDR(new ArrayList<String>() {{
            add("10.250.110.0/24");
            add("10.250.111.0/24");
        }});
        cluster.setVpcCIDR("10.250.0.0/16");
        //TODO: return cluster object
        // return
        return cluster;
    }
}
