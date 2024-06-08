package com.example.onlinebankingsystem.service;

import com.example.onlinebankingsystem.exception.AlreadyUserExistException;
import com.example.onlinebankingsystem.model.User;
import com.example.onlinebankingsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService  implements UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void saveUser(String username, String password) {
        if (findByUsername(username) != null) {
            throw new AlreadyUserExistException(username);
        }
        User user = new User(username, passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER") // You can set roles/authorities here
                .build();
    }
}
