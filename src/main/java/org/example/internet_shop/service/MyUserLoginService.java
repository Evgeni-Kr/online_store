package org.example.internet_shop.service;

import org.example.internet_shop.dao.MyUser;
import org.example.internet_shop.dao.UserRole;
import org.example.internet_shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyUserLoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MyUserLoginService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean signUp(MyUser myUser) {
        if (userRepository.findByUsername(myUser.getUsername()) != null) {
            return false;
        }
            myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
            myUser.setRole(UserRole.USER);
            userRepository.save(myUser);
            return true;
    }
}
