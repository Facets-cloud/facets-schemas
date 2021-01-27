package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.TeamMembership;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMembershipRepository extends MongoRepository<TeamMembership, String> {
}
