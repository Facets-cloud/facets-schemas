package com.capillary.ops.cp.bo.requests;

import java.util.List;

public abstract class CloudCodeBuildSpecPhase {
    public abstract String getPhaseName();

    public abstract List<String> toBuildSpecCommands();

    public abstract CloudCodeBuildSpecPhase mergePhase(CloudCodeBuildSpecPhase newPhase);
}
