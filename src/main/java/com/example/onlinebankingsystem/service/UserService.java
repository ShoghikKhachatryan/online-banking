package com.example.onlinebankingsystem.service;

import com.example.onlinebankingsystem.exception.AlreadyUserExistException;
import com.example.onlinebankingsystem.model.User;
import com.example.onlinebankingsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveUser(String username, String password) {
        if (findByUsername(username) != null) {
            throw new AlreadyUserExistException(username);
        }
        //TODO
    }

    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
