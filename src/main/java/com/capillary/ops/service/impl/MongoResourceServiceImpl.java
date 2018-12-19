package com.capillary.ops.service.impl;

import com.capillary.ops.bo.AbstractInfrastructureResource;
import com.capillary.ops.bo.HelmInfrastructureResource;
import com.capillary.ops.bo.InstanceType;
import com.capillary.ops.bo.exceptions.ResourceAlreadyExists;
import com.capillary.ops.bo.exceptions.ResourceDoesNotExist;
import com.capillary.ops.bo.mongodb.MongoCommand;
import com.capillary.ops.bo.mongodb.MongoResource;
import com.capillary.ops.bo.mongodb.MongoUser;
import com.capillary.ops.repository.mongodb.MongoInfraRepository;
import com.capillary.ops.repository.mongodb.MongoUserRepository;
import com.capillary.ops.service.HelmInfrastructureService;
import com.capillary.ops.service.InstanceTypeService;
import com.capillary.ops.service.KubeUtils;
import com.capillary.ops.service.MongoResourceService;
import com.mongodb.*;
import hapi.release.ReleaseOuterClass;
import org.bson.Document;
import org.microbean.helm.chart.resolver.ChartResolverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MongoResourceServiceImpl implements MongoResourceService {

    @Autowired
    private MongoInfraRepository mongoInfraRepository;

    @Autowired
    private HelmInfrastructureService helmInfrastructureService;

    @Autowired
    private MongoUserRepository mongoUserRepository;

    @Autowired
    private InstanceTypeService instanceTypeService;

    private enum CommandStatus {
        PENDING,
        SUCCESS,
        ERROR
    }

    public MongoResource findResourceByNameAndEnvironment(MongoResource mongoResource) {
        List<MongoResource> mongoResources = mongoInfraRepository.findByResourceNameAndEnvironment(mongoResource.getResourceName(), mongoResource.getEnvironment());
        if (mongoResources.size() > 1) {
            System.out.println("error in data, more than one resources with same name");
        }

        return mongoResources.isEmpty() ? null : mongoResources.get(0);
    }

    private MongoResource setParamsForPersistence(MongoResource savedResource, MongoResource newResource) {
        Integer volumeSize = newResource.getVolumeSize();
        if (volumeSize != null) {
            savedResource.setVolumeSize(volumeSize);
        }

        return savedResource;
    }

    private MongoResource setParamsForInstanceType(MongoResource savedResource, MongoResource newResource) {
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
            throw new ResourceDoesNotExist("monogodb for given resource name and environment does not exist");
        }

        setParamsForPersistence(savedResource, mongoResource);
        setParamsForInstanceType(savedResource, mongoResource);

        Map<String, Object> helmSetParams = generateSetParams(savedResource);
        helmSetParams.put("mongodbRootPassword", savedResource.getMongodbRootPassword());

        Map<String, Object> serviceType = new HashMap<>(1);
        serviceType.put("type", "LoadBalancer");
        helmSetParams.put("service", serviceType);

        HelmInfrastructureResource helmInfrastructureResource = new HelmInfrastructureResource(mongoResource.getDeploymentName(), "mongodb", helmSetParams);
        try {
            helmInfrastructureService.update(helmInfrastructureResource, mongoResource.getEnvironment());
        } catch (URISyntaxException | ChartResolverException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("error occured while updating resource");
        }

        return mongoInfraRepository.save(savedResource);
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

//        Map<String, Object> serviceType = new HashMap<>();
//        serviceType.put("type", "LoadBalancer");
//        helmSetParams.put("service", serviceType);

        ReleaseOuterClass.Release release = null;
        HelmInfrastructureResource helmInfrastructureResource = new HelmInfrastructureResource("", "mongodb", helmSetParams);
        try {
            release = helmInfrastructureService.deploy(helmInfrastructureResource, mongoResource.getEnvironment());
        } catch (URISyntaxException | ChartResolverException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("error occured while deploying resource", e);
        }

        if (release == null) {
            throw new RuntimeException("error occured while deploying resource, release was null");
        }

        mongoResource.setMongodbRootPassword(mongoRootPassword);
        mongoResource.setDeploymentName(release.getName());
        return mongoInfraRepository.save(mongoResource);
    }

    @Override
    public MongoUser createUser(MongoUser user) {
        MongoResource mongoResource = new MongoResource();
        mongoResource.setEnvironment(user.getEnvironment());
        mongoResource.setResourceName(user.getResourceName());

        List<MongoUser> existingUser = mongoUserRepository.findByUserNameAndResourceNameAndDbNameAndEnvironment(user.getUserName(), user.getResourceName(), user.getDbName(), user.getEnvironment().name());
        if (!existingUser.isEmpty()) {
            throw new ResourceAlreadyExists("following user already exists for current app, db and environment");
        }

        MongoResource savedMongoResource = this.findResourceByNameAndEnvironment(mongoResource);
        if (savedMongoResource == null) {
            throw new ResourceDoesNotExist("mongodb savedMongoResource does not exist for this application");
        }

        user.setPassword(KubeUtils.generatePassword(15));

        MongoCredential credential = MongoCredential.createCredential("root", "admin", savedMongoResource.getMongodbRootPassword().toCharArray());
        MongoClientOptions options = MongoClientOptions.builder().sslEnabled(false).build();

//        MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017), credential, options);
        MongoClient mongoClient = new MongoClient(new ServerAddress(savedMongoResource.getDeploymentName() + "-mongodb.infra", 27017), credential, options);

        BasicDBObject commandArguments = constructCreateUserCommand(user);
        BasicDBObject command = new BasicDBObject(commandArguments);

        Document document = mongoClient.getDatabase("admin").runCommand(command);
        System.out.println("document = " + document);
        if (!document.containsKey("ok") && document.getDouble("ok") != 1.0) {
            return null;
        }
        mongoClient.close();

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
        for (String role: user.getUserRoles()) {
            BasicDBObject roleDbObject = new BasicDBObject();
            roleDbObject.put("role", role);
            roleDbObject.put("db", StringUtils.isEmpty(user.getDbName()) ? "admin" : user.getDbName());

            rolesList.add(roleDbObject);
        }
        return rolesList;
    }

    public Map<Integer, String> runCommand(MongoCommand mongoCommand) {
        List<MongoResource> mongoResources = mongoInfraRepository.findByResourceNameAndEnvironment(mongoCommand.getResourceName(), mongoCommand.getEnvironment());
        if (mongoResources.isEmpty()) {
            throw new ResourceDoesNotExist("Mongodb for given app and environment does not exist");
        }

        MongoResource mongoResource = mongoResources.get(0);

        MongoCredential credential = MongoCredential.createCredential("root", "admin", mongoResource.getMongodbRootPassword().toCharArray());
        MongoClientOptions options = MongoClientOptions.builder().sslEnabled(false).build();
        MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017), credential, options);

        Map<Integer, String> commandStatus = new HashMap<>(mongoCommand.getCommands().size());
        int commandStatusCounter = 1;
        for (String command: mongoCommand.getCommands()) {
            commandStatus.put(commandStatusCounter, CommandStatus.PENDING.name());
            commandStatusCounter++;
        }

        commandStatusCounter = 1;
        for (String command: mongoCommand.getCommands()) {
            BasicDBObject basicDBObject = new BasicDBObject();
            basicDBObject.put("eval", command);

            BasicDBObject runCommand = new BasicDBObject(basicDBObject);
            Document document = mongoClient.getDatabase(mongoCommand.getDbName()).runCommand(runCommand);

            if (!document.containsKey("ok") || document.getDouble("ok") != 1.0) {
                commandStatus.put(commandStatusCounter, CommandStatus.ERROR.name());
                break;
            }

            commandStatus.put(commandStatusCounter, CommandStatus.SUCCESS.name());
        }
        mongoClient.close();

        return commandStatus;
    }
}
