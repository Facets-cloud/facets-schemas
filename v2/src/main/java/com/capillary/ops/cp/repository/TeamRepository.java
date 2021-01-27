package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends MongoRepository<Team, String> {
    Optional<Team> findOneByName(String name);
}
