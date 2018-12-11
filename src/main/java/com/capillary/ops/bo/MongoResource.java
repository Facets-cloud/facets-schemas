package com.capillary.ops.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public class MongoResource extends AbstractInfrastructureResource {

    private ResourceLimit resourceLimit;

    private Map<String, String> extraFlags;

    @JsonIgnore
    private String mongodbRootPassword = "";

    public ResourceLimit getResourceLimit() {
        return resourceLimit;
    }

    public void setResourceLimit(ResourceLimit resourceLimit) {
        this.resourceLimit = resourceLimit;
    }

    public Map<String, String> getExtraFlags() {
        return extraFlags;
    }

    public void setExtraFlags(Map<String, String> extraFlags) {
        this.extraFlags = extraFlags;
    }

    public String getMongodbRootPassword() {
        return mongodbRootPassword;
    }

    public void setMongodbRootPassword(String mongodbRootPassword) {
        this.mongodbRootPassword = mongodbRootPassword;
    }
}
