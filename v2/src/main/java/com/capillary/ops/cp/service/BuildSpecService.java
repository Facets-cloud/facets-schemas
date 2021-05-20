package com.capillary.ops.cp.service;

import com.capillary.ops.App;
import com.capillary.ops.cp.bo.requests.Cloud;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BuildSpecService {
    // TODO: write test cases for all
    public static class YamlMap {
        private Map<String, Object> yaml;

        public Map<String, Object> getYaml() {
            return yaml;
        }

        public YamlMap(Map<String, Object> yaml) {
            this.yaml = new HashMap<>();
            this.yaml.putAll(yaml);
        }
    }

    /**
     * Given a Cloud select the build spec template
     *
     * @param cloud
     * @return
     * @throws IOException
     */
    public String getBuildSpecYaml(Cloud cloud) throws IOException {
        String yamlFile = "";
        switch (cloud) {
            case LOCAL:
                yamlFile = "cc/cc-local-buildspec.yaml";
                break;
            case AWS:
            default:
                yamlFile = "cc/cc-buildspec.yaml";
        }
        return CharStreams.toString(
                new InputStreamReader(
                        App.class.getClassLoader().getResourceAsStream(yamlFile),
                        Charsets.UTF_8));
    }

    /**
     * Given a Yaml file  override the steps
     *
     * @param yaml
     * @param overrideSteps
     * @param preBuildSteps
     * @return
     */
    public String overrideBuildSpec(String yaml,
                                    List<String> overrideSteps,
                                    List<String> preBuildSteps) throws JsonProcessingException {
        if(StringUtils.isEmpty(yaml)){
            throw new IllegalArgumentException("Yaml cannot be empty");
        }
        Map<String, Object> buildSpec = new Yaml().load(yaml);

        if (overrideSteps != null && !overrideSteps.isEmpty()) {
            Map<String, Object> phases = (Map<String, Object>) buildSpec.getOrDefault("phases", new HashMap<>());
            Map<String, Object> build = (Map<String, Object>) phases.getOrDefault("build", new HashMap<>());
            List<String> commands = overrideSteps;
            build.put("commands", commands);
            phases.put("build", build);
            buildSpec.put("phases", phases);
        }
        if (preBuildSteps != null && !preBuildSteps.isEmpty()) {
            Map<String, Object> phases = (Map<String, Object>) buildSpec.getOrDefault("phases", new HashMap<>());
            Map<String, Object> build = (Map<String, Object>) phases.getOrDefault("pre_build", new HashMap<>());
            List<String> commands = preBuildSteps;
            build.put("commands", commands);
            phases.put("pre_build", build);
            buildSpec.put("phases", phases);
        }
        return this.toYamlString(new YamlMap(buildSpec));
    }

    /**
     * Convert YamlMap as String
     *
     * @param yamlMap
     * @return
     * @throws JsonProcessingException
     */
    private String toYamlString(YamlMap yamlMap) throws JsonProcessingException {
        YAMLMapper yamlMapper = new YAMLMapper();
        yamlMapper.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true);
        yamlMapper.configure(YAMLGenerator.Feature.SPLIT_LINES, false);
        return yamlMapper.writeValueAsString(yamlMap.getYaml());
    }

}
