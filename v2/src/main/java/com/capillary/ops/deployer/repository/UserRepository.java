package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findOneByUserName(String userName);
}
