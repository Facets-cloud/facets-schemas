package com.capillary.ops.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class DeployerUtil {
    public static String getAuthUserName(){
        String userName = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof OAuth2User){
            userName = ((OAuth2User) principal).getName();
        }
        else{
            userName = principal.toString();
        }
        return userName;
    }
}
