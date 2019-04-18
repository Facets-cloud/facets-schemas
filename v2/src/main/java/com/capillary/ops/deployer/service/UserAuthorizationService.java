package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserAuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String appAdminUser = System.getenv("APP_ADMIN_USER");
        String appAdminPassword = System.getenv("APP_ADMIN_PASSWORD");
        String appCommonPassword = System.getenv("APP_COMMON_PASSWORD");
        User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();

        if(username.equalsIgnoreCase(appAdminUser)) {
            return userBuilder.username(appAdminUser).password(appAdminPassword).roles("ADMIN").build();
        }

        try {
            com.capillary.ops.deployer.bo.User user = userRepository.findOneByUserName(username).get();
            List<String> roles = user.getRoles() == null ? new ArrayList<>() : user.getRoles();
            return userBuilder.username(username)
                    .password(appCommonPassword)
                    .roles((String[]) roles.toArray())
                    .build();
        } catch (NoSuchElementException e) {
            throw new UsernameNotFoundException("");
        }
    }
}