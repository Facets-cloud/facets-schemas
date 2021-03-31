package com.capillary.ops.deployer.service.facade;

import com.capillary.ops.deployer.bo.User;
import com.capillary.ops.deployer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFacade {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(user.getPassword() !=null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public User getUserByUserName(String userName) {
        return userRepository.findOneByUserName(userName).get();
    }

    public User updateUser(User user) {
        userRepository.findById(user.getId()).get();
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
