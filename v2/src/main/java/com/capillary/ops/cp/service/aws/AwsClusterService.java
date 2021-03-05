package com.capillary.ops.cp.service.aws;

import com.amazonaws.regions.Regions;
import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.requests.AwsClusterRequest;
import com.capillary.ops.cp.service.ClusterService;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.google.common.collect.Lists;
import com.jcabi.aspects.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.SimpleTimeZone;

/**
 * AWS implementation of the Facade
 */
@Component
@Loggable
public class AwsClusterService implements ClusterService<AwsCluster, AwsClusterRequest> {

    @Autowired
    private AwsAssumeRoleService awsAssumeRoleService;

    private static final Logger logger = LoggerFactory.getLogger(AwsClusterService.class);

    private static final List<Regions> CN_REGIONS = Lists.newArrayList(Regions.CN_NORTH_1, Regions.CN_NORTHWEST_1);

    private boolean isClusterAccessibleByRole(String roleArn, String externalId) {
        if (StringUtils.isEmpty(roleArn) || StringUtils.isEmpty(externalId)) {
            throw new NotFoundException("could not find role or external id");
        }

        return awsAssumeRoleService.testRoleAccess(roleArn, externalId);
    }

    private boolean isClusterAccessibleByAccessId(String accessKeyId, String secretAccessKey) {
        if (StringUtils.isEmpty(accessKeyId) || StringUtils.isEmpty(secretAccessKey)) {
            throw new NotFoundException("could not find access key id or secret access key");
        }

        return true;
    }

    private boolean isClusterAccessible(AwsClusterRequest request) {
        boolean accessible = false;

        try {
            accessible = isClusterAccessibleByRole(request.getRoleARN(), request.getExternalId());
        } catch (NotFoundException e) {
            logger.info("params for accessing cluster by role not found: {}", e.toString());
        }

        if (!accessible) {
            try {
                accessible = isClusterAccessibleByAccessId(request.getAccessKeyId(), request.getSecretAccessKey());
            } catch (NotFoundException e) {
                logger.info("params for accessing cluster by access id not found: {}", e.toString());
            }
        }

        return accessible;
    }

    private boolean clusterSupportsAccessKeyAuth(AwsClusterRequest request) {
        return CN_REGIONS.contains(request.getRegion());
    }

    private boolean requestHasAccessKeyAuth(AwsClusterRequest request) {
        return !StringUtils.isEmpty(request.getAccessKeyId()) || !StringUtils.isEmpty(request.getSecretAccessKey());
    }

    @Override
    public AwsCluster createCluster(AwsClusterRequest request) {
        //DONE: Validations
        //1. Test the arn & external Id connectivity
        if (!isClusterAccessible(request)) {
            throw new RuntimeException("cluster not accessible by provided role or access key");
        }
        //DONE: Variable Assignment
        AwsCluster cluster = new AwsCluster(request.getClusterName());

        if (request.getInstanceTypes() != null && request.getInstanceTypes().size() > 0){
            cluster.setInstanceTypes(request.getInstanceTypes());
        }

        if (request.getK8sRequestsToLimitsRatio() == null){
            request.setK8sRequestsToLimitsRatio(1D);
        }
        if (shouldSetAccessKey(request)) {
            cluster.setAccessKeyId(request.getAccessKeyId());
            cluster.setSecretAccessKey(request.getSecretAccessKey());
        } else if (requestHasAccessKeyAuth(request)) {
            throw new RuntimeException("Access key based cluster authentication not supported for region " + request.getRegion().toString());
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

    private boolean shouldSetAccessKey(AwsClusterRequest request) {
        return requestHasAccessKeyAuth(request) && clusterSupportsAccessKeyAuth(request);
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
        if (shouldSetAccessKey(request) && (checkChanged(existing.getAccessKeyId(), request.getAccessKeyId()) ||
                checkChanged(existing.getSecretAccessKey(), request.getSecretAccessKey()))) {
            existing.setAccessKeyId(request.getAccessKeyId());
            existing.setSecretAccessKey(request.getSecretAccessKey());
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
