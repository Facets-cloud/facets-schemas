package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.Team;
import com.capillary.ops.cp.facade.TeamFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cc-ui/v1/teams/")
public class UiTeamController {

    @Autowired
    private TeamFacade teamFacade;

    @PostMapping
    @PreAuthorize("hasRole('CC-ADMIN')")
    public Team createTeam(@RequestBody Team team) {
        return teamFacade.createTeam(team);
    }

    @PostMapping("{teamId}/members")
    @PreAuthorize("hasRole('CC-ADMIN')")
    public Team addTeamMembers(@RequestBody List<String> userNames, @PathVariable String teamId) {
        return teamFacade.addTeamMembers(teamId, userNames);
    }
}
