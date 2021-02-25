package com.capillary.ops.cp;

import com.capillary.ops.cp.bo.TeamMembership;
import com.capillary.ops.cp.bo.TeamResource;
import com.capillary.ops.cp.repository.TeamMembershipRepository;
import com.capillary.ops.cp.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomPermissionEvaluator implements PermissionEvaluator {

  @Autowired
  private TeamMembershipRepository teamMembershipRepository;

  @Autowired
  private TeamRepository teamRepository;

  @Override
  public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
    if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
      return false;
    }
    String userName = getUserName(auth.getPrincipal());

    if (((String) permission).equalsIgnoreCase("RESOURCE_NAME_READ")) {
      List<TeamMembership> membershipList = teamMembershipRepository.findByUserName(userName);
      if (membershipList.isEmpty()) {
        return true;
      }
      Set<TeamResource> resources = membershipList.stream()
        .map(x -> x.getTeamId())
        .map(x -> teamRepository.findById(x).get())
        .flatMap(x -> x.getResources().stream()).collect(Collectors.toSet());
      if(resources.stream().anyMatch(x -> x.equals((TeamResource) targetDomainObject))) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
    if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
      return false;
    }
    return false;
  }

  private String getUserName(Object principal) {
    if(principal instanceof OAuth2User){
      return ((OAuth2User) principal).getName();
    }
    else{
      return principal.toString();
    }
  }
}

