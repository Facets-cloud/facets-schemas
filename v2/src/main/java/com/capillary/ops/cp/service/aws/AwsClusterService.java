package com.capillary.ops.cp.service.aws;

import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.requests.AwsClusterRequest;
import com.capillary.ops.cp.service.ClusterService;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * AWS implementation of the Facade
 */
@Component
@Loggable
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
        cluster.setK8sRequestsToLimitsRatio(request.getK8sRequestsToLimitsRatio());
        //TODO: Variable Generations
        //1. Generate CIDR.
        cluster.setVpcCIDR("10.250.0.0/16");
        cluster.setStackName(request.getStackName());
        return cluster;
    }

    @Override
    public AwsCluster updateCluster(AwsClusterRequest request, AwsCluster existing) {
        if (checkChanged(existing.getTz(), request.getTz().getID())) {
            existing.setTz(request.getTz());
        }
        if (checkChanged(existing.getRoleARN(), request.getRoleARN()) || checkChanged(existing.getExternalId(),
            request.getExternalId())) {
            existing.setRoleARN(request.getRoleARN());
            existing.setExternalId(request.getExternalId());
        }
        if (checkChanged(existing.getK8sRequestsToLimitsRatio(), request.getK8sRequestsToLimitsRatio())) {
            existing.setK8sRequestsToLimitsRatio(request.getK8sRequestsToLimitsRatio());
        }
        return existing;
    }

    private boolean checkChanged(Object old, Object changed) {
        return changed != null && !old.equals(changed);
    }
}
