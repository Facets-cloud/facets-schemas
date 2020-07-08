package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.repository.CpClusterRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AclService {

    @Autowired
    private CpClusterRepository clusterRepository;

    private static final List<String> validStackGetSuffixes = Lists.newArrayList("READ", "WRITE", "ADMIN");
    private static final List<String> validStackUpdateSuffixes = Lists.newArrayList("WRITE", "ADMIN");
    private static final String ADMIN_ROLE = "ADMIN";

    private Set<String> getRolesForAuthority(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(x -> x.substring("ROLE_".length()))
                .collect(Collectors.toSet());
    }

    public boolean hasStackAccess(Authentication authentication, String stackName, List<String> validRoleSuffixes) {
        Set<String> roles = getRolesForAuthority(authentication);
        if (roles.contains(ADMIN_ROLE)) {
            return true;
        }

        stackName = stackName.toUpperCase();
        for (String role: roles) {
            String[] roleParts = role.split("_");
            if (roleParts[0].startsWith(stackName) && validRoleSuffixes.contains(roleParts[roleParts.length-1])) {
                return true;
            }
        }

        return false;
    }

    public boolean hasStackReadAccess(Authentication authentication, String stackName) {
        return hasStackAccess(authentication, stackName, validStackGetSuffixes);
    }

    public boolean hasStackWriteAccess(Authentication authentication, String stackName) {
        return hasStackAccess(authentication, stackName, validStackUpdateSuffixes);
    }

    public boolean hasClusterAccess(Authentication authentication, String stackName, String clusterId,
                                    List<String> validRoleSuffixes) {
        final String stack = stackName.toUpperCase();

        Set<String> roles = getRolesForAuthority(authentication);
        if (roles.contains(ADMIN_ROLE)) {
            return true;
        }

        Set<String> stackLevelRoles = new HashSet<>();
        validRoleSuffixes.forEach(capability -> stackLevelRoles.add(stack + "_" + capability));
        stackLevelRoles.retainAll(roles);
        if (stackLevelRoles.size() > 0) {
            return true;
        }

        Set<String> clusterLevelRoles = new HashSet<>();
        validRoleSuffixes.forEach(capability -> clusterLevelRoles.add(stack + "_" + clusterId + "_" + capability));
        clusterLevelRoles.retainAll(roles);
        return clusterLevelRoles.size() > 0;
    }

    public boolean hasClusterReadAccess(Authentication authentication, String stackName, String clusterId) {
        return hasClusterAccess(authentication, stackName, clusterId, validStackGetSuffixes);
    }

    public boolean hasClusterReadAccess(Authentication authentication, String clusterId) {
        AbstractCluster cluster = clusterRepository.findById(clusterId).get();
        String stackName = cluster.getStackName();

        return hasClusterReadAccess(authentication, stackName, clusterId);
    }

    public boolean hasClusterWriteAccess(Authentication authentication, String stackName, String clusterId) {
        return hasClusterAccess(authentication, stackName, clusterId, validStackUpdateSuffixes);
    }

    public boolean hasClusterWriteAccess(Authentication authentication, String clusterId) {
        AbstractCluster cluster = clusterRepository.findById(clusterId).get();
        String stackName = cluster.getStackName();

        return hasClusterWriteAccess(authentication, stackName, clusterId);
    }
}
