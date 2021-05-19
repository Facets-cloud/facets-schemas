package com.capillary.ops.cp.service;

import com.capillary.ops.App;
import com.capillary.ops.cp.bo.requests.Cloud;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
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
    public YamlMap getBuildSpecYaml(Cloud cloud) throws IOException {
        String yamlFile = "";
        switch (cloud) {
            case LOCAL:
                yamlFile = "cc/cc-local-buildspec.yaml";
                break;
            case AWS:
            default:
                yamlFile = "cc/cc-buildspec.yaml";
        }
        String buildSpecYaml =
                CharStreams.toString(
                        new InputStreamReader(
                                App.class.getClassLoader().getResourceAsStream(yamlFile),
                                Charsets.UTF_8));
        Map<String, Object> buildSpec = new Yaml().load(buildSpecYaml);
        return new YamlMap(buildSpec);
    }

    /**
     * Given a Yaml template map override the steps
     *
     * @param input
     * @param overrideSteps
     * @param preBuildSteps
     * @return
     */
    public YamlMap overrideBuildSpec(YamlMap input,
                                     List<String> overrideSteps,
                                     List<String> preBuildSteps) {
        Map<String, Object> buildSpec = new HashMap<String, Object>() {{
            putAll(input.getYaml());
        }};

        if (overrideSteps != null && !overrideSteps.isEmpty()) {
            (((Map<String, Object>) ((Map<String, Object>) buildSpec.get("phases")).get("build")))
                    .put("commands", overrideSteps);
        }
        if (preBuildSteps != null && !preBuildSteps.isEmpty()) {
            ((List<String>) (((Map<String, Object>) ((Map<String, Object>) buildSpec.get("phases")).get("pre_build"))).get("commands"))
                    .addAll(preBuildSteps);
        }
        return new YamlMap(buildSpec);
    }

    /**
     * Convert YamlMap as String
     *
     * @param yamlMap
     * @return
     * @throws JsonProcessingException
     */
    public String toYamlString(YamlMap yamlMap) throws JsonProcessingException {
        YAMLMapper yamlMapper = new YAMLMapper();
        yamlMapper.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true);
        yamlMapper.configure(YAMLGenerator.Feature.SPLIT_LINES, false);
        return yamlMapper.writeValueAsString(yamlMap.getYaml());
    }

}
