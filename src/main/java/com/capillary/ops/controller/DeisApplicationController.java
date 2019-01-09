package com.capillary.ops.controller;

import com.capillary.ops.bo.*;
import com.capillary.ops.bo.exceptions.ApplicationAlreadyExists;
import com.capillary.ops.bo.exceptions.ApplicationDoesNotExist;
import com.capillary.ops.bo.exceptions.ResourceAlreadyExists;
import com.capillary.ops.bo.mongodb.MongoCommand;
import com.capillary.ops.bo.mongodb.MongoResource;
import com.capillary.ops.bo.mongodb.MongoUser;
import com.capillary.ops.bo.redis.RedisResource;
import com.capillary.ops.service.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DeisApplicationController {

  @Autowired ApplicationMongoService applicationMongoService;

  @Autowired private DeisApiService deisApiService;

  @Autowired private DeploymentMongoService deploymentMongoService;

  @Autowired private SSHKeyPairGenerator sshKeyPairGenerator;

  @Autowired private GitService gitService;

  @Autowired private ConfigSetService configSetService;

  @Autowired private MongoResourceService mongoResourceService;

  @Autowired private InstanceTypeService instanceTypeService;

  @Autowired private RedisResourceService redisResourceService;

  @PostMapping("/applications")
  public ResponseEntity<Application> createApplication(@RequestBody Application application)
      throws ApplicationAlreadyExists {
    SSHKeyPair keyPair = sshKeyPairGenerator.generate();
    application.setPrivateKey(keyPair.getPrivateKey());
    application.setPublicKey(keyPair.getPublicKey());
    application = applicationMongoService.createApplication(application);

    for (Environments environment : Environments.values()) {
      deisApiService.createApplication(environment, application);
    }
    return new ResponseEntity<>(application, HttpStatus.OK);
  }

  @PostMapping("/applications/{applicationId}/deployments")
  public ResponseEntity<Deployment> deployByAppId(
      @PathVariable String applicationId, @RequestBody Deployment deployment)
      throws ApplicationDoesNotExist {
    Application application = applicationMongoService.getApplicationById(applicationId);
    if (application == null) {
      throw new ApplicationDoesNotExist();
    }
    deployment.setApplicationId(application.getId());
    deploymentMongoService.createDeployment(deployment);
    gitService.pushToDeis(application, deployment);
    return new ResponseEntity<>(deployment, HttpStatus.OK);
  }

  @GetMapping("/deployments/{deploymentId}")
  public ResponseEntity<Deployment> getDeployment(@PathVariable String deploymentId) {
    Deployment deployment = deploymentMongoService.getDeployment(deploymentId);
    return new ResponseEntity<>(deployment, HttpStatus.OK);
  }

  @GetMapping("/applications")
  public ResponseEntity<List<Application>> getApps() {
    List<Application> apps = applicationMongoService.getAll();
    return new ResponseEntity<>(apps, HttpStatus.OK);
  }

  @GetMapping("/applications/{applicationId}")
  public ResponseEntity<Application> getApp(@PathVariable String applicationId) {
    Application app = applicationMongoService.getApplicationById(applicationId);
    return new ResponseEntity<>(app, HttpStatus.OK);
  }

  @PutMapping("/applications/{applicationId}")
  public ResponseEntity<Application> updateApp(
      @PathVariable String applicationId, @RequestBody Application application) {
    Application app = applicationMongoService.getApplicationById(applicationId);
    app.setConfigs(application.getConfigs());
    app.setRepoURL(application.getRepoURL());
    app.setProjectFolder(application.getProjectFolder());
    for (Environments environment : Environments.values()) {
      deisApiService.updateApplication(environment, app);
    }
    applicationMongoService.updateApplication(app);
    return new ResponseEntity<>(app, HttpStatus.OK);
  }

  @GetMapping("/applications/{applicationId}/deployments")
  public ResponseEntity<List<Deployment>> getDeployments(@PathVariable String applicationId) {
    Application app = applicationMongoService.getApplicationById(applicationId);
    List<Deployment> deployment = deploymentMongoService.getDeploymentOfApp(app.getId());
    return new ResponseEntity<>(deployment, HttpStatus.OK);
  }

  @PostMapping("/configsets")
  public ResponseEntity<ConfigSet> addConfigSet(@RequestBody ConfigSet configSet) {
    List<ConfigSet> configSetByName = configSetService.getConfigSetByName(configSet.getName());
    if (configSetByName != null) {
      throw new ResourceAlreadyExists();
    }
    return new ResponseEntity<>(configSetService.addConfigSet(configSet), HttpStatus.OK);
  }

  @GetMapping("/configsets")
  public ResponseEntity<List<ConfigSet>> getConfigSets() {
    return new ResponseEntity<>(configSetService.getAllConfigSets(), HttpStatus.OK);
  }

  @GetMapping("/applications/{applicationId}/branches")
  public ResponseEntity<List<String>> getBranches(@PathVariable String applicationId) {
    Application app = applicationMongoService.getApplicationById(applicationId);
    return new ResponseEntity<>(gitService.listBranches(app), HttpStatus.OK);
  }

  @GetMapping("/applications/{applicationId}/tags")
  public ResponseEntity<List<String>> getTags(@PathVariable String applicationId) {
    Application app = applicationMongoService.getApplicationById(applicationId);
    return new ResponseEntity<>(gitService.listTags(app), HttpStatus.OK);
  }

  @DeleteMapping("/applications/{applicationId}")
  public ResponseEntity<Application> deleteApp(@PathVariable String applicationId) {
    Application app = applicationMongoService.getApplicationById(applicationId);
    for (Environments environment : Environments.values()) {
      deisApiService.deleteApplication(environment, app);
    }
    applicationMongoService.deleteApplication(app);
    return new ResponseEntity<>(app, HttpStatus.OK);
  }

  @PostMapping("/resources/mongodb")
  public ResponseEntity<MongoResource> createMongoResource(
      @RequestBody MongoResource mongoResource) {
    MongoResource resource = (MongoResource) mongoResourceService.create(mongoResource);
    return new ResponseEntity<>(resource, HttpStatus.OK);
  }

  @PutMapping("/resources/mongodb")
  public ResponseEntity<MongoResource> updateMongoResource(
      @RequestBody MongoResource mongoResource) {
    MongoResource resource = (MongoResource) mongoResourceService.update(mongoResource);
    return new ResponseEntity<>(resource, HttpStatus.OK);
  }

  @GetMapping("/resources/mongodb")
  public ResponseEntity<List<MongoResource>> getAllMongoResources() {
    List<MongoResource> mongoResources =
        mongoResourceService
            .findAll()
            .stream()
            .map(resource -> (MongoResource) resource)
            .collect(Collectors.toList());
    return new ResponseEntity<>(mongoResources, HttpStatus.OK);
  }

  @GetMapping("/resources/mongodb/{id}")
  public ResponseEntity<MongoResource> getMongoResourceById(@PathVariable String id) {
    MongoResource resource = (MongoResource) mongoResourceService.findById(id);
    return new ResponseEntity<>(resource, HttpStatus.OK);
  }

  @PostMapping("/resources/mongodb/user")
  public ResponseEntity<MongoUser> addMongoUser(@RequestBody MongoUser mongoUser) {
    System.out.println("mongoUser = " + mongoUser);
    MongoUser user = mongoResourceService.createUser(mongoUser);

    if (user == null) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PostMapping("/resources/mongodb/command")
  public ResponseEntity<Map<Integer, String>> runMongoCommand(
      @RequestBody MongoCommand mongoCommand) {
    Map<Integer, String> commandStatus = mongoResourceService.runCommand(mongoCommand);
    return new ResponseEntity<>(commandStatus, HttpStatus.OK);
  }

  @PostMapping("/infra/resource/instancetype")
  public ResponseEntity<InstanceType> addInstanceType(@RequestBody InstanceType instanceType) {
    return new ResponseEntity<>(instanceTypeService.save(instanceType), HttpStatus.OK);
  }

  @GetMapping("/resources/instancetype")
  public ResponseEntity<List<InstanceType>> getAllInstanceTypes() {
    return new ResponseEntity<>(instanceTypeService.findAll(), HttpStatus.OK);
  }

  @GetMapping("/resources/instancetype/{instanceTypeName}")
  public ResponseEntity<InstanceType> getInstanceTypeByName(@PathVariable String instanceTypeName) {
    return new ResponseEntity<>(instanceTypeService.findByName(instanceTypeName), HttpStatus.OK);
  }

  @PostMapping("/resources/redis")
  public ResponseEntity<RedisResource> createRedisResource(
      @RequestBody RedisResource redisResource) {
    RedisResource resource = (RedisResource) redisResourceService.create(redisResource);
    return new ResponseEntity<>(resource, HttpStatus.OK);
  }

  @PutMapping("/resources/redis")
  public ResponseEntity<RedisResource> updateRedisResource(
      @RequestBody RedisResource redisResource) {
    RedisResource resource = (RedisResource) redisResourceService.update(redisResource);
    return new ResponseEntity<>(resource, HttpStatus.OK);
  }

  @GetMapping("/resources/redis")
  public ResponseEntity<List<RedisResource>> getAllRedisResources() {
    List<RedisResource> redisResources =
        redisResourceService
            .findAll()
            .stream()
            .map(resource -> (RedisResource) resource)
            .collect(Collectors.toList());
    return new ResponseEntity<>(redisResources, HttpStatus.OK);
  }

  @GetMapping("/resources/redis/{id}")
  public ResponseEntity<RedisResource> getRedisResourceById(@PathVariable String id) {
    RedisResource resource = (RedisResource) redisResourceService.findById(id);
    return new ResponseEntity<>(resource, HttpStatus.OK);
  }
}
