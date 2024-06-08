package com.example.onlinebankingsystem.service;

import com.example.onlinebankingsystem.model.User;
import com.example.onlinebankingsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
