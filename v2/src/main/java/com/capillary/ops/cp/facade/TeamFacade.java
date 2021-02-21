package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.Team;
import com.capillary.ops.cp.bo.TeamMembership;
import com.capillary.ops.cp.repository.TeamMembershipRepository;
import com.capillary.ops.cp.repository.TeamRepository;
import com.capillary.ops.deployer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamFacade {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMembershipRepository teamMembershipRepository;

    @Autowired
    private UserRepository userRepository;

    public Team createTeam(Team stack) {
        return teamRepository.save(stack);
    }

    public List<TeamMembership> addTeamMembers(String teamId, List<String> userNames) {
      Team team = teamRepository.findById(teamId).get();
      List<TeamMembership> teamMemberships =
        userNames.stream().map(x -> new TeamMembership(team.getId(), x)).collect(Collectors.toList());
      teamMembershipRepository.saveAll(teamMemberships);
      return teamMemberships;
    }

    public List<Team> getTeams() {
      return teamRepository.findAll();
    }

    public List<TeamMembership> getTeamMembers(String teamId) {
      Team team = teamRepository.findById(teamId).get();
      return teamMembershipRepository.findByTeamId(teamId);
    }
}
