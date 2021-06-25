package com.capillary.ops.cp.service.aws;

import com.amazonaws.regions.Regions;
import com.capillary.ops.cp.bo.AzureCluster;
import com.capillary.ops.cp.bo.AzureCluster;
import com.capillary.ops.cp.bo.components.ComponentType;
import com.capillary.ops.cp.bo.requests.AzureClusterRequest;
import com.capillary.ops.cp.bo.requests.AzureClusterRequest;
import com.capillary.ops.cp.service.ClusterService;
import com.capillary.ops.cp.service.ComponentVersionService;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.google.common.collect.Lists;
import com.jcabi.aspects.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

/**
 * AWS implementation of the Facade
 */
@Component
@Loggable
public class AzureClusterService implements ClusterService<AzureCluster, AzureClusterRequest> {

    @Autowired
    private AwsAssumeRoleService awsAssumeRoleService;

    @Autowired
    private ComponentVersionService componentVersionService;

    private static final Logger logger = LoggerFactory.getLogger(AzureClusterService.class);

    private boolean isClusterAccessible(AzureClusterRequest request) {
        return true;
    }

    @Override
    public AzureCluster createCluster(AzureClusterRequest request) {
        //DONE: Validations
        //1. Test the arn & external Id connectivity
        if (!isClusterAccessible(request)) {
            throw new RuntimeException("cluster not accessible by provided role or access key");
        }
        //DONE: Variable Assignment
        AzureCluster cluster = new AzureCluster(request.getClusterName());
        cluster.setStackName(request.getStackName());

        if (request.getInstanceTypes() != null && request.getInstanceTypes().size() > 0){
            cluster.setInstanceTypes(request.getInstanceTypes());
        }

        if (request.getK8sRequestsToLimitsRatio() == null){
            request.setK8sRequestsToLimitsRatio(1D);
        }

        Map<ComponentType, String> componentVersions = componentVersionService.getClusterComponentVersions(
                cluster.getStackName(), request);
        cluster.setComponentVersions(componentVersions);

        cluster.setTenantId(request.getTenantId());
        cluster.setTz(request.getTz());
        cluster.setSubscriptionId(request.getSubscriptionId());
        cluster.setRegion(request.getRegion());
        cluster.setAzs(request.getAzs());
        cluster.setReleaseStream(request.getReleaseStream());
        cluster.setK8sRequestsToLimitsRatio(request.getK8sRequestsToLimitsRatio());
        cluster.setClientId(request.getClientId());
        cluster.setClientSecret(request.getClientSecret());
        //TODO: Variable Generations
        //1. Generate CIDR.
        cluster.setVpcCIDR(request.getVpcCIDR());
        cluster.setCdPipelineParent(request.getCdPipelineParent());
        cluster.setRequireSignOff(request.getRequireSignOff());
        return cluster;
    }

    @Override
    public AzureCluster updateCluster(AzureClusterRequest request, AzureCluster existing) {
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

        if (checkChanged(existing.getClientId(), request.getClientId()) || checkChanged(existing.getClientSecret(),
            request.getClientSecret())) {
            existing.setClientSecret(request.getClientSecret());
            existing.setClientId(request.getClientId());
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
        componentVersionService.syncComponentsVersion(request, existing);
        return existing;
    }

    private boolean checkChanged(Object old, Object changed) {
        return changed != null && !changed.equals(old);
    }
}
