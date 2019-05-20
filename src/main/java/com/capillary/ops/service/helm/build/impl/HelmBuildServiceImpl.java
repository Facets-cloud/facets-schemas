package com.capillary.ops.service.helm.build.impl;

import com.capillary.ops.App;
import com.capillary.ops.bo.helm.ApplicationFamily;
import com.capillary.ops.bo.helm.BuildStatus;
import com.capillary.ops.bo.helm.HelmApplication;
import com.capillary.ops.service.helm.build.BuildService;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ecr.EcrClient;
import software.amazon.awssdk.services.ecr.model.CreateRepositoryRequest;
import software.amazon.awssdk.services.ecr.model.SetRepositoryPolicyRequest;

@Service
public class HelmBuildServiceImpl implements BuildService {

  @Autowired private EcrClient ecrClient;

  @Override
  public BuildStatus triggerBuild(String appName, String branch) {
    return null;
  }

  @Override
  public BuildStatus getBuildStatus(String buildId) {
    return null;
  }

  @Override
  public List<BuildStatus> getAllBuilds(String appName, boolean pending) {
    return null;
  }

  @Override
  public void createEcrRepository(HelmApplication application) {
    String repositoryName =
        application.getApplicationFamily().getName().toLowerCase() + "/" + application.getName();
    System.out.println("creating repository with name = " + repositoryName);
    CreateRepositoryRequest createRepositoryRequest =
        CreateRepositoryRequest.builder().repositoryName(repositoryName).build();

    ecrClient.createRepository(createRepositoryRequest);
    setEcrPolicy(application, repositoryName);
  }

  private void setEcrPolicy(HelmApplication application, String repositoryName) {
    SetRepositoryPolicyRequest policyRequest =
        SetRepositoryPolicyRequest.builder()
            .repositoryName(repositoryName)
            .policyText(getEcrPolicyForFamily(application.getApplicationFamily()))
            .build();

    ecrClient.setRepositoryPolicy(policyRequest);
  }

  private String getEcrPolicyForFamily(ApplicationFamily applicationFamily) {
    String template = null;
    try {
      template =
          CharStreams.toString(
              new InputStreamReader(
                  App.class.getClassLoader().getResourceAsStream("aws/EcrPolicyTemplate.json"),
                  Charsets.UTF_8));
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("could not find the ecr policy template");
    }

    return template.replace("{{ACCOUNT_ID}}", applicationFamily.getAwsAccountId());
  }
}
