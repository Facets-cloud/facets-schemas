package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.requests.Cloud;
import com.fasterxml.jackson.core.JsonProcessingException;
import mockit.Tested;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BuildSpecServiceTest{

    @Tested
    BuildSpecService buildSpecService;

    @Test
    public void testOverrideBuildSpec() {
        try {
            String buildSpecYaml = buildSpecService.getBuildSpecYaml(Cloud.AWS);
            assert buildSpecYaml.contains("terraform init");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String buildSpecYaml = buildSpecService.getBuildSpecYaml(Cloud.LOCAL);
            assert !buildSpecYaml.contains("terraform init");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void overrideBuildSpecEmptyYaml() {
        String yaml = "";
        try {
            String newYaml = buildSpecService.overrideBuildSpec(yaml, Arrays.asList("Step1", "Step2"), new ArrayList<>());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test()
    public void overrideBuildSpecNonFilledYaml() {
        String yaml = "test:";
        try {
            String newYaml = buildSpecService.overrideBuildSpec(yaml, Arrays.asList("Step1", "Step2"), new ArrayList<>());
            System.out.println(newYaml);
            Map<String, Object> load = new Yaml().load(newYaml);
            assert load.containsKey("phases");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test()
    public void overrideBuildSpecFilledYaml() {
        String yaml = "phases:\n" +
                "  build:\n" +
                "    commands:\n" +
                "    - Original\n";
        try {
            String newYaml = buildSpecService.overrideBuildSpec(yaml, Arrays.asList("Step1", "Step2"), new ArrayList<>());
            System.out.println(newYaml);
            Map<String, Object> load = new Yaml().load(newYaml);
            assert load.containsKey("phases") && ((Map<String, Object>)load.get("phases")).containsKey("build");
            assert newYaml.contains("Step1");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test()
    public void overrideBuildSpecFilledYamlPreBuild() {
        String yaml = "phases:\n" +
                "  pre_build:\n" +
                "    commands:\n" +
                "    - Original\n";
        try {
            String newYaml = buildSpecService.overrideBuildSpec(yaml, new ArrayList<>(), Arrays.asList("Step1", "Step2"));
            System.out.println(newYaml);
            Map<String, Object> load = new Yaml().load(newYaml);
            assert load.containsKey("phases") && ((Map<String, Object>)load.get("phases")).containsKey("pre_build");
            assert newYaml.contains("Step1") && !newYaml.contains("Original");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}