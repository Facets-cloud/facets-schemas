package com.capillary.ops.service.impl;

import com.capillary.ops.bo.AbstractInfrastructureResource;
import com.capillary.ops.bo.HelmInfrastructureResource;
import com.capillary.ops.bo.InfrastructureResourceStatus;
import com.capillary.ops.bo.InstanceType;
import com.capillary.ops.bo.exceptions.ResourceAlreadyExists;
import com.capillary.ops.bo.exceptions.ResourceDoesNotExist;
import com.capillary.ops.bo.redis.RedisResource;
import com.capillary.ops.repository.redis.RedisInfraRepository;
import com.capillary.ops.service.DeploymentServiceClient;
import com.capillary.ops.service.InstanceTypeService;
import com.capillary.ops.service.RedisResourceService;
import com.capillary.ops.service.helm.HelmDeploymentService;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RedisResourceServiceImpl implements RedisResourceService {

  @Autowired private RedisInfraRepository redisInfraRepository;

  @Autowired private InstanceTypeService instanceTypeService;

  @Autowired private HelmDeploymentService helmDeploymentService;

  @Autowired private DeploymentServiceClient deploymentServiceClient;

  private RedisResource findResourceByNameAndEnvironment(RedisResource redisResource) {
    List<RedisResource> redisResources =
        redisInfraRepository.findByResourceNameAndEnvironment(
            redisResource.getResourceName(), redisResource.getEnvironment());
    if (redisResources.size() > 1) {
      System.out.println("error in data, more than one resources with same name");
    }

    return redisResources.isEmpty() ? null : redisResources.get(0);
  }

  private Map<String, Object> generateSetParams(RedisResource resource) {
    Map<String, Object> persistence = new HashMap<>();
    persistence.put("enabled", false);
    if (resource.getVolumeSize() != null) {
      persistence.put("enabled", true);
      persistence.put("size", resource.getVolumeSize() + "Gi");
    }

    Map<String, Object> nodeSelector = new HashMap<>();
    nodeSelector.put("environment", resource.getEnvironment().name());

    InstanceType masterInstanceType = instanceTypeService.findByName(resource.getInstanceType());
    if (masterInstanceType == null) {
      throw new ResourceDoesNotExist("following master instance type does not exist");
    }

    InstanceType slaveInstanceType =
        instanceTypeService.findByName(resource.getSlaveInstanceType());
    if (slaveInstanceType == null) {
      throw new ResourceDoesNotExist("following slave instance type does not exist");
    }

    //        Map<String, Object> serviceType = new HashMap<>(1);
    //        serviceType.put("type", "LoadBalancer");

    Map<String, Object> masterConfig = new HashMap<>();
    masterConfig.put("resources", masterInstanceType.toKubeConfig());
    //        masterConfig.put("service", serviceType);
    //        masterConfig.put("disableCommands", new ArrayList<>());

    Map<String, Object> slaveConfig = new HashMap<>();
    slaveConfig.put("resources", slaveInstanceType.toKubeConfig());

    Map<String, Object> clusterConfig = new HashMap<>();
    clusterConfig.put("slaveCount", resource.getSlaveCount());

    Map<String, Object> valueParams = new LinkedHashMap<>();
    valueParams.put("master", masterConfig);
    valueParams.put("salve", slaveConfig);
    valueParams.put("persistence", persistence);
    valueParams.put("nodeSelector", nodeSelector);
    valueParams.put("cluster", clusterConfig);
    valueParams.put("usePassword", false);

    return valueParams;
  }

  private void setParamsForPersistence(RedisResource savedResource, RedisResource newResource) {
    Integer volumeSize = newResource.getVolumeSize();
    if (volumeSize != null) {
      savedResource.setVolumeSize(volumeSize);
    }
  }

  private void setParamsForInstanceType(RedisResource savedResource, RedisResource newResource) {
    String instanceType = newResource.getInstanceType();
    if (!StringUtils.isEmpty(instanceType)) {
      savedResource.setInstanceType(instanceType);
    }
  }

  private void setParamsForSlaveInstanceType(
      RedisResource savedResource, RedisResource newResource) {
    String instanceType = newResource.getSlaveInstanceType();
    if (!StringUtils.isEmpty(instanceType)) {
      savedResource.setSlaveInstanceType(instanceType);
    }
  }

  @Override
  public RedisResource create(AbstractInfrastructureResource infrastructureResource) {
    RedisResource resource = (RedisResource) infrastructureResource;

    RedisResource existingRedisResource = this.findResourceByNameAndEnvironment(resource);
    if (existingRedisResource != null) {
      throw new ResourceAlreadyExists(
          "redis with given resource name and environment already exists");
    }

    Map<String, Object> helmSetParams = generateSetParams(resource);
    HelmInfrastructureResource helmInfrastructureResource =
        new HelmInfrastructureResource("redis", helmSetParams);

    deploymentServiceClient.deploy(resource, helmInfrastructureResource, "helm");

    return redisInfraRepository.save(resource);
  }

  @Override
  public RedisResource update(AbstractInfrastructureResource infrastructureResource) {
    RedisResource resource = (RedisResource) infrastructureResource;

    RedisResource savedResource = findResourceByNameAndEnvironment(resource);
    if (savedResource == null) {
      throw new ResourceDoesNotExist(
          "redis for given resource name and environment does not exist");
    }

    setParamsForPersistence(savedResource, resource);
    setParamsForInstanceType(savedResource, resource);
    setParamsForSlaveInstanceType(savedResource, resource);

    Map<String, Object> helmSetParams = generateSetParams(savedResource);

    HelmInfrastructureResource helmInfrastructureResource =
        new HelmInfrastructureResource(savedResource.getDeploymentName(), "redis", helmSetParams);
    deploymentServiceClient.update(savedResource, helmInfrastructureResource, "helm");

    savedResource.setDeploymentStatus(InfrastructureResourceStatus.PENDING);
    return savedResource;
  }

  @Override
  public List<AbstractInfrastructureResource> findAll() {
    return new ArrayList<>(redisInfraRepository.findAll());
  }

  @Override
  public RedisResource findById(String id) {
    Optional<RedisResource> redisResource = redisInfraRepository.findById(id);
    return redisResource.orElse(null);
  }
}
