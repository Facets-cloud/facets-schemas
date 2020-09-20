package com.capillary.ops.cp.bo.requests;

import com.capillary.ops.cp.bo.TerraformStep;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CloudCodeBuildSpecBuildPhase extends CloudCodeBuildSpecPhase {

    public CloudCodeBuildSpecBuildPhase() {}

    public CloudCodeBuildSpecBuildPhase(List<String> buildPhaseStart, List<TerraformStep> tfSteps, List<String> buildPhaseEnd) {
        this.buildPhaseStart = buildPhaseStart;
        this.tfSteps = tfSteps;
        this.buildPhaseEnd = buildPhaseEnd;
    }

    private List<String> buildPhaseStart;

    private List<TerraformStep> tfSteps;

    private List<String> buildPhaseEnd;

    public List<String> getBuildPhaseStart() {
        return buildPhaseStart;
    }

    public void setBuildPhaseStart(List<String> buildPhaseStart) {
        this.buildPhaseStart = buildPhaseStart;
    }

    public List<TerraformStep> getTfSteps() {
        return tfSteps;
    }

    public void setTfSteps(List<TerraformStep> tfSteps) {
        this.tfSteps = tfSteps;
    }

    public List<String> getBuildPhaseEnd() {
        return buildPhaseEnd;
    }

    public void setBuildPhaseEnd(List<String> buildPhaseEnd) {
        this.buildPhaseEnd = buildPhaseEnd;
    }

    @Override
    public String getPhaseName() {
        return "build";
    }

    @Override
    public List<String> toBuildSpecCommands() {
        List<String> commands = new ArrayList<>(buildPhaseStart);
        List<String> terraformSteps = tfSteps.stream().map(TerraformStep::toTFCommand).collect(Collectors.toList());
        commands.addAll(terraformSteps);
        commands.addAll(buildPhaseEnd);
        return commands;
    }

    @Override
    public CloudCodeBuildSpecPhase mergePhase(CloudCodeBuildSpecPhase oldPhase) {
        CloudCodeBuildSpecBuildPhase phase = (CloudCodeBuildSpecBuildPhase) oldPhase;
        List<String> startPhase = this.getBuildPhaseStart() != null ? this.getBuildPhaseStart() : phase.getBuildPhaseStart();
        List<TerraformStep> terraformSteps = this.getTfSteps() != null ? this.getTfSteps() : phase.getTfSteps();
        List<String> endPhase = this.getBuildPhaseEnd() != null ? this.getBuildPhaseEnd() : phase.getBuildPhaseEnd();

        return new CloudCodeBuildSpecBuildPhase(startPhase, terraformSteps, endPhase);
    }
}
