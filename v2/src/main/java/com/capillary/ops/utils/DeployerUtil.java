package com.capillary.ops.utils;

import com.capillary.ops.deployer.service.BasicUserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;

public class DeployerUtil {
    public static String getAuthUserName(){
        String userName = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof OAuth2User) {
            userName = ((OAuth2User) principal).getName();
        } else if (principal instanceof BasicUserDetailsService.SimpleBasicAuthUser) {
            userName = ((BasicUserDetailsService.SimpleBasicAuthUser) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    public static OAuth2User getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof OAuth2User) {
            return ((OAuth2User) principal);
        }
        return null;
    }

  public static Collection<? extends GrantedAuthority> getUserAuthorities() {
    return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
  }
}
