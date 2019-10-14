package com.capillary.ops.deployer.bo;

public class HealthCheck {

    public HealthCheck() {
    }

    public HealthCheck(Probe livenessProbe, Probe readinessProbe) {
        this.livenessProbe = livenessProbe;
        this.readinessProbe = readinessProbe;
    }

    private Probe livenessProbe;

    private Probe readinessProbe;

    public Probe getLivenessProbe() {
        return livenessProbe;
    }

    public void setLivenessProbe(Probe livenessProbe) {
        this.livenessProbe = livenessProbe;
    }

    public Probe getReadinessProbe() {
        return readinessProbe;
    }

    public void setReadinessProbe(Probe readinessProbe) {
        this.readinessProbe = readinessProbe;
    }
}
