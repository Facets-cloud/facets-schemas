package com.capillary.ops.deployer.service;

import com.capillary.ops.cp.bo.CloudCodeBuildSpec;
import com.capillary.ops.cp.bo.requests.CloudCodeBuildSpecBuildPhase;
import com.capillary.ops.cp.bo.requests.CloudCodeBuildSpecPhase;
import com.capillary.ops.cp.repository.CloudCodeBuildSpecRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.*;

@Service
public class CloudBuildSpecService {

    @Autowired
    private CloudCodeBuildSpecRepository cloudCodeBuildSpecRepository;

    private static final String CODE_BUILD_PROJECT_NAME = "capillary-cloud-tf-apply";

    private static final String CODE_BUILD_SPEC_VERSION = "0.2";

    private static final List<String> BUILD_PHASES = Lists.newArrayList("install", "pre_build", "build", "post_build");

    private String readBuildSpecBasePhase(String phase) {
        try {
            URL url = this.getClass().getResource("/cloudBuildSpec/" + phase + ".json");
            return Resources.toString(url, Charsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CloudCodeBuildSpec getBaseBuildSpec() {
        Gson gson = new Gson();
        String buildPhaseFile = readBuildSpecBasePhase("build");
        CloudCodeBuildSpecBuildPhase buildPhase = gson.fromJson(buildPhaseFile, CloudCodeBuildSpecBuildPhase.class);

        ArrayList<String> artifacts = Lists.newArrayList("capillary-cloud-tf/tfaws/fetched_builds/**/*",
                "capillary-cloud-tf/tfaws/*.json");

        return new CloudCodeBuildSpec(CODE_BUILD_PROJECT_NAME, null,
                CODE_BUILD_SPEC_VERSION, ImmutableMap.of("build", buildPhase), artifacts);
    }

    private Map<String, CloudCodeBuildSpecPhase> mergeCodeBuildSpecPhases(Map<String, CloudCodeBuildSpecPhase> first,
                                                                 Map<String, CloudCodeBuildSpecPhase> second) {
        Map<String, CloudCodeBuildSpecPhase> mergedBuildPhase = new HashMap<>();
        BUILD_PHASES.parallelStream().forEach(phase -> {
            CloudCodeBuildSpecPhase oldPhase = first.get(phase);
            CloudCodeBuildSpecPhase newPhase = second.get(phase);
            CloudCodeBuildSpecPhase buildSpecPhase = newPhase != null ? newPhase : oldPhase;
            if (buildSpecPhase != null) {
                mergedBuildPhase.put(buildSpecPhase.getPhaseName(), buildSpecPhase.mergePhase(oldPhase));
            }
        });

        return mergedBuildPhase;
    }

    private CloudCodeBuildSpec mergeCodeBuildSpec(CloudCodeBuildSpec first, CloudCodeBuildSpec second) {
        String version = second.getVersion() != null ? second.getVersion() : first.getVersion();
        String projectName = second.getProjectName() != null ? second.getProjectName() : first.getProjectName();
        List<String> artifacts = second.getArtifactFiles() != null ? second.getArtifactFiles() : first.getArtifactFiles();
        Map<String, CloudCodeBuildSpecPhase> buildSpecPhases = mergeCodeBuildSpecPhases(first.getBuildSpecPhases(),
                second.getBuildSpecPhases());
        return new CloudCodeBuildSpec(projectName, second.getClusterId(),
                version, buildSpecPhases, artifacts);
    }

    public CloudCodeBuildSpec getClusterBuildSpec(String clusterId) {
        CloudCodeBuildSpec baseBuildSpec = getBaseBuildSpec();
        Optional<CloudCodeBuildSpec> buildSpecForCluster = cloudCodeBuildSpecRepository.findByClusterId(clusterId);
        return buildSpecForCluster.orElse(baseBuildSpec);
    }

    public CloudCodeBuildSpec updateClusterBuildSpec(String clusterId, CloudCodeBuildSpec cloudCodeBuildSpec) {
        CloudCodeBuildSpec existingBuildSpec = getClusterBuildSpec(clusterId);
        CloudCodeBuildSpec codeBuildSpec = mergeCodeBuildSpec(existingBuildSpec, cloudCodeBuildSpec);
        return cloudCodeBuildSpecRepository.save(codeBuildSpec);
    }

    private Map<String, Object> createBuildSpecObject(CloudCodeBuildSpec buildSpec) {
        Map<String, Object> buildSpecYaml = new HashMap<>();
        buildSpecYaml.put("version", buildSpec.getVersion());

        Map<String, Object> artifacts = new HashMap<>();
        artifacts.put("files", new ArrayList<String>(buildSpec.getArtifactFiles()));
        buildSpecYaml.put("artifacts", artifacts);

        Map<String, Object> phases = new HashMap<>();
        if (buildSpec.getBuildSpecPhases().containsKey("build")) {
            List<String> commands = buildSpec.getBuildSpecPhases().get("build").toBuildSpecCommands();
            Map<String, Object> build = new HashMap<>();
            build.put("commands", commands);
            phases.put("build", build);
        }

        buildSpecYaml.put("phases", phases);

        return buildSpecYaml;
    }

    public String getBuildSpec(String clusterId) {
        CloudCodeBuildSpec clusterBuildSpec = getClusterBuildSpec(clusterId);
        Map<String, Object> buildSpecObject = createBuildSpecObject(clusterBuildSpec);

        YAMLMapper yamlMapper = new YAMLMapper();
        yamlMapper.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true);
        yamlMapper.configure(YAMLGenerator.Feature.SPLIT_LINES, false);
        try {
            return yamlMapper.writeValueAsString(buildSpecObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMergedBuildSpec(String clusterId, CloudCodeBuildSpec cloudCodeBuildSpec) {
        CloudCodeBuildSpec existingSpec = getClusterBuildSpec(clusterId);
        CloudCodeBuildSpec mergedSpec = mergeCodeBuildSpec(existingSpec, cloudCodeBuildSpec);
        Map<String, Object> buildSpecObject = createBuildSpecObject(mergedSpec);

        YAMLMapper yamlMapper = new YAMLMapper();
        yamlMapper.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true);
        yamlMapper.configure(YAMLGenerator.Feature.SPLIT_LINES, false);
        try {
            return yamlMapper.writeValueAsString(buildSpecObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
