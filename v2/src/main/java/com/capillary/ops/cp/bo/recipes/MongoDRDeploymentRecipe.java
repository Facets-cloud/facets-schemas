package com.capillary.ops.cp.bo.recipes;

import com.capillary.ops.cp.bo.AbstractDeploymentRecipe;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("MONGO_DR")
public class MongoDRDeploymentRecipe {
    private String dbInstanceName;
    private String snapshotId;

    public String getDbInstanceName() {
        return dbInstanceName;
    }

    public void setDbInstanceName(String dbInstanceName) {
        this.dbInstanceName = dbInstanceName;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }
}
