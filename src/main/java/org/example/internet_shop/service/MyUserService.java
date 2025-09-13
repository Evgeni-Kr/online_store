package org.example.internet_shop.service;

import org.example.internet_shop.dao.MyUser;
import org.example.internet_shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyUserService {

    private final UserRepository userRepository;

    @Autowired
    public MyUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MyUser getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
