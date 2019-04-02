package com.capillary.ops.service.impl;

import com.capillary.ops.bo.*;
import com.capillary.ops.bo.exceptions.ResourceAlreadyExists;
import com.capillary.ops.bo.exceptions.ResourceDoesNotExist;
import com.capillary.ops.bo.mongodb.MongoCommand;
import com.capillary.ops.bo.mongodb.MongoResource;
import com.capillary.ops.bo.mongodb.MongoUser;
import com.capillary.ops.repository.mongodb.MongoInfraRepository;
import com.capillary.ops.repository.mongodb.MongoUserRepository;
import com.capillary.ops.service.*;
import com.capillary.ops.service.helm.HelmDeploymentService;
import com.mongodb.BasicDBObject;
import java.util.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MongoResourceServiceImpl implements MongoResourceService {

  @Autowired private MongoInfraRepository mongoInfraRepository;

  @Autowired private HelmDeploymentService helmDeploymentService;

  @Autowired private MongoUserRepository mongoUserRepository;

  @Autowired private InstanceTypeService instanceTypeService;

  @Autowired private DeploymentServiceClient deploymentServiceClient;

  private enum CommandStatus {
    PENDING,
    SUCCESS,
    ERROR
  }

  public MongoResource findResourceByNameAndEnvironment(
      String resourceName, Environments environment) {
    List<MongoResource> mongoResources =
        mongoInfraRepository.findByResourceNameAndEnvironment(resourceName, environment);
    if (mongoResources.size() > 1) {
      System.out.println("error in data, more than one resources with same name");
    }

    return mongoResources.isEmpty() ? null : mongoResources.get(0);
  }

  public MongoResource findResourceByNameAndEnvironment(MongoResource mongoResource) {
    return findResourceByNameAndEnvironment(
        mongoResource.getResourceName(), mongoResource.getEnvironment());
  }

  private MongoResource setParamsForPersistence(
      MongoResource savedResource, MongoResource newResource) {
    Integer volumeSize = newResource.getVolumeSize();
    if (volumeSize != null) {
      savedResource.setVolumeSize(volumeSize);
    }

    return savedResource;
  }

  private MongoResource setParamsForInstanceType(
      MongoResource savedResource, MongoResource newResource) {
    String instanceType = newResource.getInstanceType();
    if (!StringUtils.isEmpty(instanceType)) {
      savedResource.setInstanceType(instanceType);
    }

    return savedResource;
  }

  private Map<String, Object> generateSetParams(MongoResource mongoResource) {
    Map<String, Object> persistence = new HashMap<>();
    persistence.put("enabled", false);
    if (mongoResource.getVolumeSize() != null) {
      persistence.put("enabled", true);
      persistence.put("size", mongoResource.getVolumeSize() + "Gi");
    }

    Map<String, Object> nodeSelector = new HashMap<>();
    nodeSelector.put("environment", mongoResource.getEnvironment().name());

    InstanceType instanceType = instanceTypeService.findByName(mongoResource.getInstanceType());
    if (instanceType == null) {
      throw new ResourceDoesNotExist("following instance type does not exist");
    }

    Map<String, Object> valueParams = new LinkedHashMap<>();
    valueParams.put("resources", instanceType.toKubeConfig());
    valueParams.put("persistence", persistence);
    valueParams.put("nodeSelector", nodeSelector);

    return valueParams;
  }

  @Override
  public MongoResource update(AbstractInfrastructureResource infrastructureResource) {
    MongoResource mongoResource = (MongoResource) infrastructureResource;

    MongoResource savedResource = findResourceByNameAndEnvironment(mongoResource);
    if (savedResource == null) {
      throw new ResourceDoesNotExist(
          "monogodb for given resource name and environment does not exist");
    }

    setParamsForPersistence(savedResource, mongoResource);
    setParamsForInstanceType(savedResource, mongoResource);

    Map<String, Object> helmSetParams = generateSetParams(savedResource);
    helmSetParams.put("mongodbRootPassword", savedResource.getMongodbRootPassword());

    HelmInfrastructureResource helmInfrastructureResource =
        new HelmInfrastructureResource(savedResource.getDeploymentName(), "mongodb", helmSetParams);

    deploymentServiceClient.update(savedResource, helmInfrastructureResource, "helm");

    savedResource.setDeploymentStatus(InfrastructureResourceStatus.PENDING);
    return savedResource;
  }

  @Override
  public List<AbstractInfrastructureResource> findAll() {
    return new ArrayList<>(mongoInfraRepository.findAll());
  }

  @Override
  public MongoResource findById(String id) {
    Optional<MongoResource> mongoResource = mongoInfraRepository.findById(id);
    return mongoResource.orElse(null);
  }

  @Override
  public MongoResource create(AbstractInfrastructureResource infrastructureResource) {
    MongoResource mongoResource = (MongoResource) infrastructureResource;

    MongoResource existingMonogResource = this.findResourceByNameAndEnvironment(mongoResource);
    if (existingMonogResource != null) {
      throw new ResourceAlreadyExists("mongodb with this name and environment already exists");
    }

    Map<String, Object> helmSetParams = generateSetParams(mongoResource);

    String mongoRootPassword = KubeUtils.generatePassword(15);
    helmSetParams.put("mongodbRootPassword", mongoRootPassword);

    HelmInfrastructureResource helmInfrastructureResource =
        new HelmInfrastructureResource("mongodb", helmSetParams);

    deploymentServiceClient.deploy(mongoResource, helmInfrastructureResource, "helm");

    mongoResource.setMongodbRootPassword(mongoRootPassword);
    return mongoInfraRepository.save(mongoResource);
  }

  @Override
  public MongoUser createUser(MongoUser user) {
    MongoResource mongoResource = new MongoResource();
    mongoResource.setEnvironment(user.getEnvironment());
    mongoResource.setResourceName(user.getResourceName());

    List<MongoUser> existingUser =
        mongoUserRepository.findByUserNameAndResourceNameAndDbNameAndEnvironment(
            user.getUserName(),
            user.getResourceName(),
            user.getDbName(),
            user.getEnvironment().name());
    if (!existingUser.isEmpty()) {
      throw new ResourceAlreadyExists(
          "following user already exists for current app, db and environment");
    }

    MongoResource savedMongoResource = this.findResourceByNameAndEnvironment(mongoResource);
    if (savedMongoResource == null) {
      throw new ResourceDoesNotExist(
          "mongodb savedMongoResource does not exist for this application");
    }

    user.setPassword(KubeUtils.generatePassword(15));

    BasicDBObject commandArguments = constructCreateUserCommand(user);
    BasicDBObject command = new BasicDBObject(commandArguments);

    Document document =
        new KubeMongoClient(
                savedMongoResource.getDeploymentName(),
                "admin",
                savedMongoResource.getMongodbRootPassword())
            .execute(command);

    System.out.println("document = " + document);
    if (!document.containsKey("ok") && document.getDouble("ok") != 1.0) {
      return null;
    }

    return mongoUserRepository.save(user);
  }

  private BasicDBObject constructCreateUserCommand(MongoUser user) {
    BasicDBObject commandArguments = new BasicDBObject();
    commandArguments.put("createUser", user.getUserName());
    commandArguments.put("pwd", user.getPassword());

    List<BasicDBObject> rolesList = constructRoleList(user);
    commandArguments.put("roles", rolesList);
    return commandArguments;
  }

  private List<BasicDBObject> constructRoleList(MongoUser user) {
    List<BasicDBObject> rolesList = new ArrayList<>();
    for (String role : user.getUserRoles()) {
      BasicDBObject roleDbObject = new BasicDBObject();
      roleDbObject.put("role", role);
      roleDbObject.put("db", StringUtils.isEmpty(user.getDbName()) ? "admin" : user.getDbName());

      rolesList.add(roleDbObject);
    }
    return rolesList;
  }

  public Map<Integer, String> runCommand(MongoCommand mongoCommand) {
    MongoResource mongoResource =
        findResourceByNameAndEnvironment(
            mongoCommand.getResourceName(), mongoCommand.getEnvironment());
    if (mongoResource == null) {
      throw new ResourceDoesNotExist("Mongodb for given app and environment does not exist");
    }

    KubeMongoClient mongoAdminClient =
        new KubeMongoClient(
            mongoResource.getDeploymentName(), "admin", mongoResource.getMongodbRootPassword());
    if (!mongoAdminClient.isExecuteFunctionPermissionPresent()) {
      System.out.println("execute function role does not exist, going to create");
      mongoAdminClient.createExecuteFunctionRoleAndGivePermission();
      System.out.println("successfully created execute function role");
    }

    KubeMongoClient mongoClient =
        new KubeMongoClient(
            mongoResource.getDeploymentName(),
            mongoCommand.getDbName(),
            mongoResource.getMongodbRootPassword());
    List<BasicDBObject> commandList = new ArrayList<>(mongoCommand.getCommands().size());
    mongoCommand
        .getCommands()
        .forEach(
            command -> {
              BasicDBObject basicDBObject = new BasicDBObject();
              basicDBObject.put("eval", command);

              BasicDBObject runCommand = new BasicDBObject(basicDBObject);
              commandList.add(runCommand);
            });

    return mongoClient.execute(commandList);
  }
}
