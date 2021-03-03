package com.capillary.ops.cp.service.aws;

import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.requests.AwsClusterRequest;
import com.capillary.ops.cp.service.ClusterService;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.ec2.model.InstanceType;

import java.util.ArrayList;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

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

        if (request.getInstanceTypes() != null && request.getInstanceTypes().size() > 0){
            cluster.setInstanceTypes(request.getInstanceTypes());
        }

        if (request.getK8sRequestsToLimitsRatio() == null){
            request.setK8sRequestsToLimitsRatio(1D);
        }
        cluster.setRoleARN(request.getRoleARN());
        cluster.setTz(request.getTz());
        cluster.setExternalId(request.getExternalId());
        cluster.setAwsRegion(request.getRegion().getName());
        cluster.setAzs(request.getAzs());
        cluster.setReleaseStream(request.getReleaseStream());
        cluster.setK8sRequestsToLimitsRatio(request.getK8sRequestsToLimitsRatio());
        //TODO: Variable Generations
        //1. Generate CIDR.
        cluster.setVpcCIDR(request.getVpcCIDR());
        cluster.setStackName(request.getStackName());
        cluster.setCdPipelineParent(request.getCdPipelineParent());
        cluster.setRequireSignOff(request.getRequireSignOff());
        return cluster;
    }

    @Override
    public AwsCluster updateCluster(AwsClusterRequest request, AwsCluster existing) {
        if (request.getTz() == null || request.getTz().getID() == null){
            request.setTz(new SimpleTimeZone(0, existing.getTz()));
        }

        if (StringUtils.isEmpty(request.getInstanceTypes())){
            request.setInstanceTypes(existing.getInstanceTypes());
        }

        if (checkChanged(existing.getInstanceTypes(), request.getInstanceTypes())){
            existing.setInstanceTypes(request.getInstanceTypes());
        }

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
        if (checkChanged(existing.getCdPipelineParent(), request.getCdPipelineParent())) {
            existing.setCdPipelineParent(request.getCdPipelineParent());
        }
        if (checkChanged(existing.getRequireSignOff(), request.getRequireSignOff())) {
            existing.setRequireSignOff(request.getRequireSignOff());
        }
        return existing;
    }

    private boolean checkChanged(Object old, Object changed) {
        return changed != null && !changed.equals(old);
    }
}
