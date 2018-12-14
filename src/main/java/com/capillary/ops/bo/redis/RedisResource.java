package com.capillary.ops.bo.redis;

import com.capillary.ops.bo.AbstractInfrastructureResource;

public class RedisResource extends AbstractInfrastructureResource {

    private String slaveInstanceType;

    private Integer slaveCount;

    public String getSlaveInstanceType() {
        return slaveInstanceType;
    }

    public void setSlaveInstanceType(String slaveInstanceType) {
        this.slaveInstanceType = slaveInstanceType;
    }

    public Integer getSlaveCount() {
        return slaveCount;
    }
}
