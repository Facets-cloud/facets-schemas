package com.capillary.ops.service;

import com.google.common.collect.Lists;
import com.mongodb.*;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import okhttp3.Response;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KubeMongoClient {

    private static final String KUBERNETES_MASTER_URL = "http://localhost:8443";

    private static final String CREATE_EXECUTE_FUNCTIONS_ROLE =
        "db.createRole( { role: \"executeFunctions\", privileges: [ { resource: { anyResource: true }, actions: [ \"anyAction\" ] } ], roles: [] } )";

    private static final String GRANT_EXECUTE_FUNCTIONS_ROLE_TO_ROOT =
        "db.grantRolesToUser(\"root\", [ { role: \"executeFunctions\", db: \"admin\" } ])";

    private static final String MONGO_EVAL_COMMAND =
        "mongo_admin_-uroot_-p%1$s_--authenticationDatabase_admin_--eval_%2$s";

    private String deploymentName;

    private String username = "root";

    private String password;

    private int port = 27017;

    private String dbName;

    private MongoCredential mongoCredential;

    private MongoClientOptions mongoClientOptions;

    private enum CommandStatus {
        PENDING, SUCCESS, ERROR
    }

    public KubeMongoClient(String deploymentName, String dbName, String password) {
        this.deploymentName = deploymentName;
        this.password = password;
        this.dbName = dbName;

        this.constructMongoCredentialsAndOptions();
    }

    private void constructMongoCredentialsAndOptions() {
        mongoCredential =
            MongoCredential.createCredential(this.username, "admin",
                this.password.toCharArray());
        mongoClientOptions =
            MongoClientOptions.builder().sslEnabled(false).build();
    }

    /**
     * @param command
     * @return
     */
    public Document execute(BasicDBObject command) {
        MongoClient mongoClient =
            new MongoClient(new ServerAddress(this.deploymentName
                + "-mongodb.infra", this.port), mongoCredential,
                mongoClientOptions);
        Document document =
            mongoClient.getDatabase(this.dbName).runCommand(command);
        mongoClient.close();

        return document;
    }

    /**
     * Executes a list of commands passed in the form of BasicDBObject
     *
     * @param commands List<BasicDBObject>
     * @return commandStatusMap - Map<Integer, String>
     */
    public Map<Integer, String> execute(List<BasicDBObject> commands) {
        Map<Integer, String> commandStatus = new HashMap<>(commands.size());
        for (int commandStatusCounter = 1; commandStatusCounter <= commands.size(); commandStatusCounter++) {
            commandStatus.put(commandStatusCounter, CommandStatus.PENDING.name());
        }

        MongoClient mongoClient =
                new MongoClient(new ServerAddress(this.deploymentName
                        + "-mongodb.infra", this.port), this.mongoCredential, this.mongoClientOptions);

        // Maintain a counter to keep the status of each executed command
        int commandStatusCounter = 1;
        for (BasicDBObject command: commands) {
            Document document = mongoClient.getDatabase(this.dbName).runCommand(command);

            // Break from the loop if one of the commands fails to execute
            if (!document.containsKey("ok") || document.getDouble("ok") != 1.0) {
                commandStatus.put(commandStatusCounter, CommandStatus.ERROR.name());
                break;
            }

            commandStatus.put(commandStatusCounter, CommandStatus.SUCCESS.name());
        }
        mongoClient.close();

        return commandStatus;
    }

    public boolean isExecuteFunctionPermissionPresent() {
        MongoClient mongoClient =
            new MongoClient(new ServerAddress(this.deploymentName
                + "-mongodb.infra", this.port), mongoCredential,
                mongoClientOptions);

        BasicDBObject roleQuery = new BasicDBObject("role", "executeFunctions");
        BasicDBObject command = new BasicDBObject(roleQuery);

        Document document =
            mongoClient.getDatabase("admin").getCollection("system.roles")
                .find(command).first();
        mongoClient.close();

        return document != null && !document.isEmpty();
    }

    private static class SimpleListener implements ExecListener {

        @Override
        public void onOpen(Response response) {
            System.out.println("inside onOpen");
            System.out.println("response = " + response);
            System.out.println("The shell will remain open for 10 seconds.");
        }

        @Override
        public void onFailure(Throwable t, Response response) {
            System.out.println("inside onFailure");
            System.out.println("response = " + response);
            System.err.println("shell barfed");
        }

        @Override
        public void onClose(int code, String reason) {
            System.out.println("inside onClose");
            System.out.println("The shell will now close.");
        }
    }

    public void createExecuteFunctionRoleAndGivePermission() {
//        BasicDBObject createRoleCommand =
//            new BasicDBObject(
//                "eval",
//                "db.createRole( { role: \"executeFunctions\", privileges: [ { resource: { anyResource: true }, actions: [ \"anyAction\" ] } ], roles: [] } )");
//        BasicDBObject addRoleToUserCommand =
//            new BasicDBObject(
//                "eval",
//                "db.grantRolesToUser(\"dbuser\", [ { role: \"executeFunctions\", db: \"admin\" } ])");
//        Config config = new ConfigBuilder().withMasterUrl(KUBERNETES_MASTER_URL).build();
        KubernetesClient kubernetesClient = new DefaultKubernetesClient();
        PodList podList = kubernetesClient.pods().inNamespace("infra").withLabel("release", this.deploymentName).list();
        podList.getItems().forEach(pod -> {
            String podName = pod.getMetadata().getName();
            String createExecuteFunctionsRole = String.format(MONGO_EVAL_COMMAND, this.password, CREATE_EXECUTE_FUNCTIONS_ROLE);
            String grantRoleToRootUser = String.format(MONGO_EVAL_COMMAND, this.password, GRANT_EXECUTE_FUNCTIONS_ROLE_TO_ROOT);

            try (ExecWatch ignored = kubernetesClient.pods()
                    .inNamespace("infra")
                    .withName(podName)
                    .readingInput(System.in)
                    .writingOutput(System.out)
                    .writingError(System.err)
                    .withTTY()
                    .usingListener(new SimpleListener())
                    .exec(createExecuteFunctionsRole.split("_"))) {

                Thread.sleep(10 * 1000);

            } catch (Exception ex) {
                System.out.println("exception occured while trying to add execute function role");
                ex.printStackTrace();
                throw new RuntimeException("Exception occured while executing command");
            }

            try (ExecWatch ignored = kubernetesClient.pods()
                    .inNamespace("infra")
                    .withName(podName)
                    .readingInput(System.in)
                    .writingOutput(System.out)
                    .writingError(System.err)
                    .withTTY()
                    .usingListener(new SimpleListener())
                    .exec(grantRoleToRootUser.split("_"))) {

                Thread.sleep(10 * 1000);

            } catch (Exception ex) {
                System.out.println("exception occured while trying to grant role to user");
                ex.printStackTrace();
                throw new RuntimeException("Exception occured while executing command");
            }
        });
    }
    //    public boolean checkForExecuteFunctionsRole() {
    //        MongoClient mongoClient =
    //                new MongoClient(new ServerAddress(this.deploymentName
    //                        + "-mongodb.infra", this.port), mongoCredential, mongoClientOptions);
    //
    //        BasicDBObject userQuery = new BasicDBObject("user", "root");
    //        BasicDBObject command = new BasicDBObject(userQuery);
    //
    //        Document document = mongoClient.getDatabase("admin").getCollection("system.users").find(command).first();
    //        Object roles = document.get("roles");
    //    }
}
