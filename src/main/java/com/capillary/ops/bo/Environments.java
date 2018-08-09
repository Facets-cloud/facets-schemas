package com.capillary.ops.bo;

import java.util.function.Function;

public enum Environments {
  PRODUCTION(x->x),
  STAGE(x-> String.format("stage-%s", x)),
  NIGHTLY(x-> String.format("nightly-%s", x));

  private Function<String, String> nameGenerator;

  Environments(Function<String, String> nameGenerator) {
    this.nameGenerator = nameGenerator;
  }

  public String getDeisEndpoint() {
    return System.getenv().get(String.format("%s_ENDPOINT", this.name()));
  }

  public String getDeisUser() {
    return System.getenv().get(String.format("%s_USER", this.name()));
  }

  public String getDeisPassword() {
    return System.getenv().get(String.format("%s_PASSWORD", this.name()));
  }

  public String getDeisGitUri() {
    return System.getenv().get(String.format("%s_GITURI", this.name()));
  }

  public String getDomain(String appName) {
    String baseDomain = System.getenv().get(String.format("%s_BASEDOMAIN", this.name()));
    String deisAppName = generateAppName(appName);
    return deisAppName + "." + baseDomain;
  }

  public String generateAppName(String appName) {
    return nameGenerator.apply(appName);
  }
}
