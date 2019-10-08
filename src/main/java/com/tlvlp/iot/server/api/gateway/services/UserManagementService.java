package com.tlvlp.iot.server.api.gateway.services;

import com.tlvlp.iot.server.api.gateway.persistence.User;
import com.tlvlp.iot.server.api.gateway.persistence.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserManagementService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserManagementService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        var users = userRepository.findAll();
        users.forEach(user -> user.setPassword(""));
        return users;
    }

    public void saveUser(User user) {
        if (isValidPassword(user.getPassword())) {
            var updatedPass = user.getPassword();
            user.setPassword(passwordEncoder.encode(updatedPass));
        }
        userRepository.save(user);
    }

    private boolean isValidPassword(String password) {
        return !password.isEmpty() && !password.isBlank() && !password.contains(" ");
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void authenticateUser(String userID, String password)
            throws NoSuchElementException, UserAuthenticationFailedException {
        User user = userRepository.findById(userID).orElseThrow();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserAuthenticationFailedException("Password mismatch");
        }
    }
}
