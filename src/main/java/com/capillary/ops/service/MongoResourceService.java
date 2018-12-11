package com.capillary.ops.service;

import com.capillary.ops.bo.HelmInfrastructureResource;
import com.capillary.ops.bo.MongoResource;
import com.capillary.ops.bo.exceptions.ResourceAlreadyExists;
import com.capillary.ops.bo.exceptions.ResourceDoesNotExist;
import com.capillary.ops.bo.mongodb.MongoCommand;
import com.capillary.ops.bo.mongodb.MongoUser;
import com.capillary.ops.repository.MongoInfraRepository;
import com.capillary.ops.repository.MongoUserRepository;
import com.mongodb.*;
import org.bson.Document;
import org.microbean.helm.chart.resolver.ChartResolverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class MongoResourceService {

    @Autowired
    private MongoInfraRepository mongoInfraRepository;

    @Autowired
    private HelmInfraService helmInfraService;

    @Autowired
    private MongoUserRepository mongoUserRepository;

    @Autowired
    private InstanceTypeService instanceTypeService;

    private enum CommandStatus {
        PENDING,
        SUCCESS,
        ERROR
    }

    public List<MongoResource> findResource(MongoResource mongoResource) {
        return mongoInfraRepository.findByAppNameAndEnvironment(mongoResource.getAppName(), mongoResource.getEnvironment());
    }

    private String constructResoureLimit(int value) {
        return String.valueOf(value) + "Mi";
    }

    private Map<String, Object> generateSetParams(MongoResource mongoResource) {
        Map<String, Object> persistence = new HashMap<>();
        persistence.put("enabled", false);

        Map<String, Object> nodeSelector = new HashMap<>();
        nodeSelector.put("env", mongoResource.getEnvironment().name());

        Map<String, Object> requests = new HashMap<>();
        requests.put("memory", constructResoureLimit(mongoResource.getResourceLimit().getMinMemory()));
        requests.put("cpu", constructResoureLimit(mongoResource.getResourceLimit().getMinCpu()));

        Map<String, Object> limits = new HashMap<>();
        limits.put("memory", constructResoureLimit(mongoResource.getResourceLimit().getMaxMemory()));
        limits.put("cpu", constructResoureLimit(mongoResource.getResourceLimit().getMaxCpu()));

        Map<String, Object> resources = new HashMap<>();
        resources.put("limits", limits);
        resources.put("requests", requests);

//        instanceTypeService.findByName()

        Map<String, Object> valueParams = new LinkedHashMap<>();
        valueParams.put("resources", resources);
        valueParams.put("persistence", persistence);
        valueParams.put("nodeSelector", nodeSelector);

        return valueParams;
    }

    public MongoResource update(MongoResource mongoResource) {

        return null;
    }

    public MongoResource save(MongoResource mongoResource) {
        List<MongoResource> mongoResources = this.findResource(mongoResource);
        if (!mongoResources.isEmpty()) {
            throw new ResourceAlreadyExists("mongodb for this app and environment already exists");
        }

        Map<String, Object> helmSetParams = generateSetParams(mongoResource);

        String mongoRootPassword = KubeUtils.generatePassword(15);
        helmSetParams.put("mongodbRootPassword", mongoRootPassword);

        Map<String, Object> serviceType = new HashMap<>();
        serviceType.put("type", "LoadBalancer");

        helmSetParams.put("service", serviceType);

        HelmInfrastructureResource helmInfrastructureResource = new HelmInfrastructureResource(mongoResource.getAppName(), "mongodb", helmSetParams);
        try {
            helmInfraService.deploy(helmInfrastructureResource);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ChartResolverException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mongoResource.setMongodbRootPassword(mongoRootPassword);
        return mongoInfraRepository.save(mongoResource);
    }

    public MongoUser createUser(MongoUser user) {
        MongoResource mongoResource = new MongoResource();
        mongoResource.setEnvironment(user.getEnvironment());
        mongoResource.setAppName(user.getAppName());

        List<MongoUser> existingUser = mongoUserRepository.findByUserNameAndAppNameAndDbNameAndEnvironment(user.getUserName(), user.getAppName(), user.getDbName(), user.getEnvironment().name());
        if (!existingUser.isEmpty()) {
            throw new ResourceAlreadyExists("The following user already exists for current app, db and environment");
        }

        List<MongoResource> resource = this.findResource(mongoResource);
        if (resource.isEmpty()) {
            throw new ResourceDoesNotExist("Mongodb resource does not exist for this application");
        }

        user.setPassword(KubeUtils.generatePassword(15));

        MongoCredential credential = MongoCredential.createCredential("root", "admin", resource.get(0).getMongodbRootPassword().toCharArray());
        MongoClientOptions options = MongoClientOptions.builder().sslEnabled(false).build();
        MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017), credential, options);
//        MongoClient mongoClient = new MongoClient(new ServerAddress(user.getAppName() + "-mongodb.infra", 27017), credential, options);

        BasicDBObject commandArguments = constructCreateUserCommand(user);
        BasicDBObject command = new BasicDBObject(commandArguments);

        Document document = mongoClient.getDatabase("admin").runCommand(command);
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
        for (String role: user.getUserRoles()) {
            BasicDBObject roleDbObject = new BasicDBObject();
            roleDbObject.put("role", role);
            roleDbObject.put("db", StringUtils.isEmpty(user.getDbName()) ? "admin" : user.getDbName());

            rolesList.add(roleDbObject);
        }
        return rolesList;
    }

    public Map<Integer, String> runCommand(MongoCommand mongoCommand) {
        List<MongoResource> mongoResources = mongoInfraRepository.findByAppNameAndEnvironment(mongoCommand.getAppName(), mongoCommand.getEnvironment());
        if (mongoResources.isEmpty()) {
            throw new ResourceDoesNotExist("Mongodb for given app and environment does not exist");
        }

        MongoResource mongoResource = mongoResources.get(0);

        MongoCredential credential = MongoCredential.createCredential("root", "admin", mongoResource.getMongodbRootPassword().toCharArray());
        MongoClientOptions options = MongoClientOptions.builder().sslEnabled(false).build();
        MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017), credential, options);

        Map<Integer, String> commandStatus = new HashMap<>(mongoCommand.getCommands().size());
        int commandCounter = 1;
        for (String command: mongoCommand.getCommands()) {
            commandStatus.put(commandCounter, CommandStatus.PENDING.name());
            commandCounter++;
        }

//        BasicDBObject commandArguments = new BasicDBObject();
//        commandArguments.put("role", "executeFunctions");
//
//        MongoDatabase adminDatabase = mongoClient.getDatabase("admin");
//        FindIterable<Document> documents = adminDatabase.getCollection("system.roles").find(new BasicDBObject(commandArguments));
//
//        if (documents == null) {
//            // Create role
//        }
//
//        BasicDBObject rootUserObject = new BasicDBObject();
//        rootUserObject.put("user", "root");
//
//        FindIterable<Document> userDocuments = adminDatabase.getCollection("system.users").find(new BasicDBObject(rootUserObject));

//        if (userDocuments.first().get("roles")) {
//            // Create role for user
//        }

        commandCounter = 1;
        for (String command: mongoCommand.getCommands()) {
            BasicDBObject basicDBObject = new BasicDBObject();
            basicDBObject.put("eval", command);

            BasicDBObject runCommand = new BasicDBObject(basicDBObject);
            Document document = mongoClient.getDatabase(mongoCommand.getDbName()).runCommand(runCommand);

            if (!document.containsKey("ok") || document.getDouble("ok") != 1.0) {
                commandStatus.put(commandCounter, CommandStatus.ERROR.name());
                break;
            }

            commandStatus.put(commandCounter, CommandStatus.SUCCESS.name());
        }
        mongoClient.close();

        return commandStatus;
    }
}
