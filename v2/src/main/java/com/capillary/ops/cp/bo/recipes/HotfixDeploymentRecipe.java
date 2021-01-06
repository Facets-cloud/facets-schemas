package com.capillary.ops.cp.bo.recipes;

import com.capillary.ops.cp.bo.Resource;

import java.util.ArrayList;
import java.util.List;

public class HotfixDeploymentRecipe {

    private List<Resource> resourceList = new ArrayList<>();

    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }
}
