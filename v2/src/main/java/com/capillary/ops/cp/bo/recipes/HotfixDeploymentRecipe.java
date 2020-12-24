package com.capillary.ops.cp.bo.recipes;

import java.util.HashMap;
import java.util.Map;

public class HotfixDeploymentRecipe {
    private Map<String, String> resourceTypeToResourceNameMap = new HashMap();

    public Map<String, String> getResourceTypeToResourceNameMap() {
        return resourceTypeToResourceNameMap;
    }

    public void setResourceTypeToResourceNameMap(Map<String, String> resourceTypeToResourceNameMap) {
        this.resourceTypeToResourceNameMap = resourceTypeToResourceNameMap;
    }
}
