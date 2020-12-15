package com.capillary.ops.cp.bo.recipes;

import com.capillary.ops.cp.bo.AbstractDeploymentRecipe;
import com.fasterxml.jackson.annotation.JsonTypeName;

public class ESDRDeploymentRecipe {
    private String esInstanceName;
    private String snapshotName;

    public String getEsInstanceName() {
        return esInstanceName;
    }

    public void setEsInstanceName(String esInstanceName) {
        this.esInstanceName = esInstanceName;
    }

    public String getSnapshotName() {
        return snapshotName;
    }

    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }
}
