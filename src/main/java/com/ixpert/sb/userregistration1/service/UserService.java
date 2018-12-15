package com.ixpert.sb.userregistration1.service;


import com.ixpert.sb.userregistration1.model.User;
import com.ixpert.sb.userregistration1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository1){
        this.userRepository = userRepository1;
    }


    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User findByConfirmationToken(String confirmationToken){
        return userRepository.findByConfirmationToken(confirmationToken);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }


}
