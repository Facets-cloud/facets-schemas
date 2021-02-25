package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.bo.Team;
import com.capillary.ops.cp.bo.TeamMembership;
import com.capillary.ops.cp.facade.TeamFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("cc-ui/v1/teams/")
public class UiTeamController {

    @Autowired
    private TeamFacade teamFacade;

    @PostMapping
    @PreAuthorize("hasRole('CC-ADMIN')")
    public Team upsertTeam(@RequestBody Team team) {
        return teamFacade.createTeam(team);
    }

    @GetMapping
    public List<Team> getTeams() {
      return teamFacade.getTeams();
    }

    @GetMapping("{teamId}")
    public Team getTeam(@PathVariable String teamId) {
      return teamFacade.getTeam(teamId);
    }

    @GetMapping("{teamId}/members")
    public List<TeamMembership> getTeamMembers(@PathVariable String teamId) {
      return teamFacade.getTeamMembers(teamId);
    }

    @PostMapping("{teamId}/members")
    @PreAuthorize("hasRole('CC-ADMIN')")
    public List<TeamMembership> addTeamMembers(@RequestBody List<String> userNames, @PathVariable String teamId) {
        return teamFacade.addTeamMembers(teamId, userNames);
    }
}
