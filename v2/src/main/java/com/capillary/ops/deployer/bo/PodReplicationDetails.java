package com.capillary.ops.deployer.bo;

public class PodReplicationDetails {

    public PodReplicationDetails() {}

    public PodReplicationDetails(Integer total, Integer ready, Integer unavailable, Integer available, Integer updated) {
        this.total = total;
        this.ready = ready;
        this.unavailable = unavailable;
        this.available = available;
        this.updated = updated;
    }

    private Integer total;

    private Integer ready;

    private Integer unavailable;

    private Integer available;

    private Integer updated;

    public Integer getTotal() {
        return total == null ? 0 : total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getReady() {
        return ready == null ? 0 : ready;
    }

    public void setReady(Integer ready) {
        this.ready = ready;
    }

    public Integer getUnavailable() {
        return unavailable == null ? 0 : unavailable;
    }

    public void setUnavailable(Integer unavailable) {
        this.unavailable = unavailable;
    }

    public Integer getAvailable() {
        return available == null ? 0 : available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getUpdated() {
        return updated == null ? 0 : updated;
    }

    public void setUpdated(Integer updated) {
        this.updated = updated;
    }
}
