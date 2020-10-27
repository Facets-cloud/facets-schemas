package com.capillary.ops.cp.bo.recipes;

import com.capillary.ops.cp.bo.AbstractDeploymentRecipe;
import com.fasterxml.jackson.annotation.JsonTypeName;

public class MongoVolumeResizeDeploymentRecipe {
    private String dbInstanceName;
    private Integer size;

    public String getDbInstanceName() {
        return dbInstanceName;
    }

    public void setDbInstanceName(String dbInstanceName) {
        this.dbInstanceName = dbInstanceName;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
