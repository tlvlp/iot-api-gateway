package com.tlvlp.iot.server.api.gateway.services;

import com.tlvlp.iot.server.api.gateway.persistence.User;
import com.tlvlp.iot.server.api.gateway.persistence.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserManagementService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserManagementService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByID(String userID) {
        return userRepository.findById(userID);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User authenticateUser(String userID, String password)
            throws NoSuchElementException, UserAuthenticationFailedException {
        User user = userRepository.findById(userID).orElseThrow();
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new UserAuthenticationFailedException("Password mismatch");
        }
    }
}
