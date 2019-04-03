package com.capillary.ops.bo.helm;

import java.util.List;
import java.util.Map;

public class CrmHelmApplication extends HelmApplication {

  public CrmHelmApplication() {}

  public CrmHelmApplication(
      ApplicationFamily applicationFamily,
      String name,
      String instanceType,
      Integer replicas,
      Map<String, String> configs,
      List<String> domains,
      ExposureType exposureType,
      String repositoryUrl,
      String pathFromRoot,
      List<Port> portMapping,
      Map<String, Object> params) {
    super(
        applicationFamily,
        name,
        instanceType,
        replicas,
        configs,
        domains,
        exposureType,
        repositoryUrl,
        pathFromRoot,
        portMapping);
    this.params = params;
  }

  private Map<String, Object> params;

  public Map<String, Object> getParams() {
    return params;
  }

  public void setParams(Map<String, Object> params) {
    this.params = params;
  }
}
