package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.TeamMembership;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMembershipRepository extends MongoRepository<TeamMembership, String> {
  List<TeamMembership> findByTeamId(String teamId);

  List<TeamMembership> findByUserName(String userName);
}
