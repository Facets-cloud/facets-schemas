package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.Team;
import com.capillary.ops.cp.bo.TeamMembership;
import com.capillary.ops.cp.repository.TeamMembershipRepository;
import com.capillary.ops.cp.repository.TeamRepository;
import com.capillary.ops.deployer.repository.UserRepository;
import com.capillary.ops.deployer.service.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Team addTeamMembers(String teamId, List<String> userNames) {
        Team team = teamRepository.findById(teamId).get();
        userNames.stream().map(x -> new TeamMembership())
    }
}
