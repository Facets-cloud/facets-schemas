package com.capillary.ops.cp.service;

import com.capillary.ops.App;
import com.capillary.ops.cp.bo.components.ComponentType;
import com.capillary.ops.cp.bo.components.SupportedVersions;
import com.capillary.ops.cp.repository.ComponentVersionRepository;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class MetaService {

    @Autowired
    private ComponentVersionRepository componentVersionRepository;

    public List<SupportedVersions> getSupportedComponentVersions() {
        List<SupportedVersions> supportedVersions = new ArrayList<>();
        try {
            String supportedVersionsData = CharStreams.toString(
                    new InputStreamReader(
                            App.class.getClassLoader().getResourceAsStream("componentUpgrade/supportedVersions.json"),
                            Charsets.UTF_8));
            JSONObject jsonObject = new JSONObject(supportedVersionsData);
            jsonObject.toMap().forEach((x, y) -> {
                HashSet<String> versionSet = new HashSet<String>((ArrayList) y);
                SupportedVersions versions = new SupportedVersions(ComponentType.valueOf(x), versionSet);
                supportedVersions.add(versions);
            });
        } catch (IOException e) {
            throw new NotFoundException("could not find supported versions");
        }
        return supportedVersions;
    }

    public SupportedVersions getSupportedComponentVersions(ComponentType componentType) {
        Optional<SupportedVersions> optional = componentVersionRepository.findById(componentType.name());
        return optional.orElseThrow(() -> new NotFoundException("No component present for type: " + componentType.name()));
    }

    public Map<ComponentType, Set<String>> getSupportedVersionsMap() {
        List<SupportedVersions> supportedComponentVersions = getSupportedComponentVersions();
        Map<ComponentType, Set<String>> map = new HashMap<>();
        supportedComponentVersions.forEach(x -> map.put(x.getComponentType(), x.getSupportedVersions()));

        return map;
    }
}
