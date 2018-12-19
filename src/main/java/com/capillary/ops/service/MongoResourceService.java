package com.capillary.ops.service;

import com.capillary.ops.bo.mongodb.MongoCommand;
import com.capillary.ops.bo.mongodb.MongoUser;

import java.util.Map;

public interface MongoResourceService extends InfrastructureResourceService {
    public Map<Integer, String> runCommand(MongoCommand mongoCommand);

    public MongoUser createUser(MongoUser user);
}
