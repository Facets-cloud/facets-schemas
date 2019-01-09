package com.capillary.ops.service.impl;

import com.capillary.ops.bo.AbstractInfrastructureResource;
import com.capillary.ops.bo.InfrastructureResourceStatus;
import com.capillary.ops.bo.redis.RedisResource;
import com.capillary.ops.repository.redis.RedisInfraRepository;
import com.capillary.ops.service.AbstractResourceResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisResourceResponseHandler implements
    AbstractResourceResponseHandler {

    @Autowired
    private RedisInfraRepository redisInfraRepository;

    @Override
    public void handleResponse(
        AbstractInfrastructureResource infrastructureResource,
        String releaseName) {
        RedisResource redisResource = (RedisResource) infrastructureResource;
        redisResource.setDeploymentName(releaseName);
        redisResource.setDeploymentStatus(InfrastructureResourceStatus.SUCCESS);

        redisInfraRepository.save(redisResource);
    }

    @Override
    public void handleError(
        AbstractInfrastructureResource infrastructureResource, Exception ex) {
        RedisResource redisResource = (RedisResource) infrastructureResource;

        System.out.println("error happened while deploying resource:"
            + redisResource);
        ex.printStackTrace();

        infrastructureResource
            .setDeploymentStatus(InfrastructureResourceStatus.FAILURE);
        redisInfraRepository.save(redisResource);
    }
}
