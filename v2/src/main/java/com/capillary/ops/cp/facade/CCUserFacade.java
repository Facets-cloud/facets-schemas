package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.user.Role;
import com.capillary.ops.deployer.bo.User;
import com.capillary.ops.deployer.repository.UserRepository;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Loggable
public class CCUserFacade {

    @Autowired
    UserRepository userRepository;

    public Role[] getAllRoles() {
        return Role.values();
    }

    public List<User> getAllCCUsers() {
        List<User> all = userRepository.findAll();
        List<String> validRoles = Arrays.stream(getAllRoles()).map(Role::getId).collect(Collectors.toList());

        all.forEach(
                user -> {
                    List<String> filteredRoles = user.getRoles().stream().filter(r -> validRoles.contains(r)).collect(Collectors.toList());
                    if(filteredRoles.size() == 0){
                        filteredRoles.add(Role.GUEST.getId());
                        user.getRoles().add(Role.GUEST.getId());
                        userRepository.save(user);
                    }
                    user.setRoles(filteredRoles);
                }
        );
        return all;
    }
}
