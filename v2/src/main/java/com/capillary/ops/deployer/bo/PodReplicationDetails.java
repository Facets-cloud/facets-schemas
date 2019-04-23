package com.capillary.ops.deployer.bo;

public class PodReplicationDetails {

    public PodReplicationDetails() {}

    public PodReplicationDetails(int total, int ready, int unavailable, int available, int updated) {
        this.total = total;
        this.ready = ready;
        this.unavailable = unavailable;
        this.available = available;
        this.updated = updated;
    }

    private int total;

    private int ready;

    private int unavailable;

    private int available;

    private int updated;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getReady() {
        return ready;
    }

    public void setReady(int ready) {
        this.ready = ready;
    }

    public int getUnavailable() {
        return unavailable;
    }

    public void setUnavailable(int unavailable) {
        this.unavailable = unavailable;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }
}
