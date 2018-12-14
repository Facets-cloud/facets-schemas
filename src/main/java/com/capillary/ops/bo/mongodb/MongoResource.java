package com.capillary.ops.bo.mongodb;

import com.capillary.ops.bo.AbstractInfrastructureResource;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class MongoResource extends AbstractInfrastructureResource {

    @JsonIgnore
    private String mongodbRootPassword = "";

    public String getMongodbRootPassword() {
        return mongodbRootPassword;
    }

    public void setMongodbRootPassword(String mongodbRootPassword) {
        this.mongodbRootPassword = mongodbRootPassword;
    }
}
