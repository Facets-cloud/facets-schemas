package com.capillary.ops.bo.mongodb;

import com.capillary.ops.bo.Environments;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import org.springframework.data.annotation.Id;

public class MongoCommand {

  @Id @JsonIgnore private String id;

  private String resourceName;

  private Environments environment;

  private String dbName;

  private List<String> commands;

  public String getResourceName() {
    return resourceName;
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
