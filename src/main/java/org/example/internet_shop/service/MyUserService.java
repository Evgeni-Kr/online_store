package org.example.internet_shop.service;

import org.example.internet_shop.dao.MyUser;
import org.example.internet_shop.dao.UserRole;
import org.example.internet_shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MyUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean signUp(String username, String password) {
        if(userRepository.findByUsername(username) != null) {
           return false;
        }
        MyUser user = new MyUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(UserRole.USER);
        userRepository.save(user);
        return true;
    }
}
