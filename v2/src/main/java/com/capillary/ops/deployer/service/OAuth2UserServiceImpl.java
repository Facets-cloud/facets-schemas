package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private static class SimpleOauth2User implements OAuth2User, Serializable {

        private Collection<? extends GrantedAuthority> grantedAuthorities;
        private Map<String, Object> attributes;
        private String name;

        public SimpleOauth2User(Collection<? extends GrantedAuthority> grantedAuthorities,
                                Map<String, Object> attributes, String name) {
            this.grantedAuthorities = grantedAuthorities;
            this.attributes = attributes;
            this.name = name;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return grantedAuthorities;
        }

        @Override
        public Map<String, Object> getAttributes() {
            return attributes;
        }

        @Override
        public String getName() {
            return name;
        }

    }
    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) super.loadUser(userRequest);
        String username = (String) oAuth2User.getAttributes().get("email");
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if(System.getenv("ADMIN_USER").equalsIgnoreCase(username)) {
            grantedAuthorities.addAll(roles(Arrays.asList("ADMIN")));
        }
        try {
            com.capillary.ops.deployer.bo.User user = userRepository.findOneByUserName(username).get();
            grantedAuthorities.addAll(roles(user.getRoles()));
            return new SimpleOauth2User(grantedAuthorities, oAuth2User.getAttributes(), username);
        } catch (NoSuchElementException e) {
            return new SimpleOauth2User(grantedAuthorities, oAuth2User.getAttributes(), username);
        }
    }

    public List<GrantedAuthority> roles(List<String> roles) {
        if (roles == null) {
            return new ArrayList<>();
        }
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        return authorities;
    }

}