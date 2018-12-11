package com.capillary.ops.bo.mongodb;

import com.capillary.ops.bo.Environments;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.util.List;

public class MongoCommand {

    @Id
    @JsonIgnore
    private String id;

    private String appName;

    private Environments environment;

    private String dbName;

    private List<String> commands;

    public String getAppName() {
        return appName;
    }

    public Environments getEnvironment() {
        return environment;
    }

    public String getDbName() {
        return dbName;
    }

    public List<String> getCommands() {
        return commands;
    }
}
