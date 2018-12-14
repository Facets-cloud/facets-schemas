package com.capillary.ops.service;

import com.capillary.ops.bo.mongodb.MongoCommand;
import com.capillary.ops.bo.mongodb.MongoUser;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface MongoResourceService extends InfrastructureResourceService {
    public Map<Integer, String> runCommand(MongoCommand mongoCommand);

    public MongoUser createUser(MongoUser user);
}
