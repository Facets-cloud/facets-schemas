package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.User;
import com.capillary.ops.deployer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BasicUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findOneByUserName(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = userOptional.get();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if(System.getenv("ADMIN_USER").equalsIgnoreCase(username)) {
            grantedAuthorities.addAll(roles(Collections.singletonList("ADMIN")));
        }

        grantedAuthorities.addAll(roles(user.getRoles()));
        return new SimpleBasicAuthUser(grantedAuthorities, user.getUserName(), user.getPassword());
    }

    public static class SimpleBasicAuthUser implements UserDetails, Serializable {

        private final Collection<? extends GrantedAuthority> grantedAuthorities;
        private final String username;
        private final String password;

        public SimpleBasicAuthUser(Collection<? extends GrantedAuthority> grantedAuthorities, String username, String password) {
            this.grantedAuthorities = grantedAuthorities;
            this.username = username;
            this.password = password;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return grantedAuthorities;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }

    public List<GrantedAuthority> roles(List<String> roles) {
        if (roles == null) {
            return new ArrayList<>();
        }
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

}