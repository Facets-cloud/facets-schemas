package com.capillary.ops.deployer.service.capillaryCloud;

import com.capillary.ops.deployer.bo.capillaryCloud.InfrastructureResourceInstances;
import com.google.gson.Gson;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InfrastructureResourceReferenceResolver {

    @Autowired
    private CapillaryCloudFacade capillaryCloudFacade;

    public Map<String, String> resolve(String clusterName, Map<String, String> configs) {
        Map<String, String> result = new HashMap<>();
        InfrastructureResourceInstances infrastructureResourceInstances =
                capillaryCloudFacade.getInfrastructureResourceInstances(clusterName);

        String json = new Gson().toJson(infrastructureResourceInstances);
        DocumentContext context = JsonPath.parse(json);
        for (String key: configs.keySet()) {
            result.put(key, context.read(configs.get(key), String.class));
        }
        return result;
    }
}
