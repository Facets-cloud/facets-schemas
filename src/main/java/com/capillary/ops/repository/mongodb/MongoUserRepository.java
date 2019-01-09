package com.capillary.ops.repository.mongodb;

import com.capillary.ops.bo.mongodb.MongoUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoUserRepository extends MongoRepository<MongoUser, String> {

    public List<MongoUser> findByUserNameAndResourceNameAndDbNameAndEnvironment(
        String userName, String resourceName, String dbName, String environment);

    public List<MongoUser> findByUserNameAndPasswordAndAppNameAndDbNameAndEnvironment(
        String userName, String password, String appName, String dbName,
        String environment);

    public List<MongoUser> findByAppNameAndEnvironment(String appName,
        String environment);
}
