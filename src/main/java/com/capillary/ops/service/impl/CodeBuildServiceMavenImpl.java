package com.capillary.ops.service.impl;

import com.capillary.ops.bo.codebuild.BuildSpec;
import com.capillary.ops.bo.codebuild.CodeBuildApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CodeBuildServiceMavenImpl extends CodeBuildServiceImpl {

  @Override
  protected String createBuildSpec(CodeBuildApplication application) {
    BuildSpec buildSpec = new BuildSpec();
    String buildSpecString = null;
    buildSpec.setVersion("0.2");

    List<String> installCommands = new ArrayList<>();
    Map<String, Object> installPhase = new HashMap<>();
    installCommands.add(
        "nohup dockerd --host=unix:///var/run/docker.sock --host=tcp://0.0.0.0:2375 --storage-driver=overlay&");
    installCommands.add("timeout 15 sh -c \"until docker info; do echo .; sleep 1; done\"");
    installPhase.put("commands", installCommands);

    List<String> preBuildCommands = new ArrayList<>();
    Map<String, Object> preBuildPhase = new HashMap<>();
    preBuildPhase.put("commands", preBuildCommands);

    List<String> buildCommands = new ArrayList<>();
    Map<String, Object> buildPhase = new HashMap<>();
    buildCommands.add("$(aws ecr get-login --region us-east-1 --no-include-email)");
    buildCommands.add(String.format("cd %s", application.getProjectFolder()));
    buildCommands.add(
        "mvn clean package -Dmaven.test.failure.ignore=false -DskipFormat=true -Dmaven.test.skip=true -U -Pdocker-cibuild");

    buildPhase.put("commands", buildCommands);

    List<String> postBuildCommands = new ArrayList<>();
    Map<String, Object> postBuildPhase = new HashMap<>();
    postBuildPhase.put("commands", postBuildCommands);

    Map<String, Object> phases = new HashMap<>();
    phases.put("install", installPhase);
    phases.put("pre_build", preBuildPhase);
    phases.put("build", buildPhase);
    phases.put("post_build", postBuildPhase);

    Map<String, Object> cache = new HashMap<>();
    List<String> cachePaths = new ArrayList<>();
    cachePaths.add("/root/.m2/**/*");
    cache.put("paths", cachePaths);

    buildSpec.setPhases(phases);
    buildSpec.setCache(cache);

    YAMLMapper yamlMapper = new YAMLMapper();
    yamlMapper.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true);
    yamlMapper.configure(YAMLGenerator.Feature.SPLIT_LINES, false);
    try {
      buildSpecString = yamlMapper.writeValueAsString(buildSpec);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return buildSpecString;
  }

  @Override
  public String getContainerImageConfig() {
    return "codebuild.mavenimage.path";
  }
}
