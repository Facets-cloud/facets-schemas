package com.capillary.ops.deployer.service.facade;

import com.capillary.ops.deployer.bo.PasswordChange;
import com.capillary.ops.deployer.bo.User;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserFacade {

    @Autowired
    private UserRepository userRepository;

    public User changePassword(String userId, PasswordChange pwdChange, boolean verifyOld) {
        Optional<User> byId = userRepository.findById(userId);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!byId.isPresent()) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        User user = byId.get();
        if(verifyOld){
            if(pwdChange.getOldPassword() == null || !user.getPassword().equals(bCryptPasswordEncoder.encode(pwdChange.getOldPassword()))){
                throw new IllegalArgumentException("Provided Old Password is not correct");
            }
        }
        user.setPassword(bCryptPasswordEncoder.encode(pwdChange.getNewPassword()));
        return userRepository.save(user);
    }

    public User createUser(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (user.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public User getUserByUserName(String userName) {
        return userRepository.findOneByUserName(userName).get();
    }

    public User updateUser(User user) {
        Optional<User> byId = userRepository.findById(user.getId());
        if (!byId.isPresent()) {
            throw new NotFoundException("User with id " + user.getId() + " not found");
        }
        user.setPassword(byId.get().getPassword());

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
