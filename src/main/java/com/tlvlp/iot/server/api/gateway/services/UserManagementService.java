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
        var users = userRepository.findAll();
        users.forEach(user -> user.setPassword(""));
        return users;
    }

    public void saveUser(User user) throws UserManagementException {
       var oldUser = userRepository.findById(user.getUserID());
       if (isNewUserWithoutPassword(user, oldUser)) {
           throw new UserManagementException("New user must have a valid password");
       } else if (isOldUserPasswordUpdateSkipped(user, oldUser)) {
           var oldPass = oldUser.get().getPassword();
           user.setPassword(oldPass);
       } else if (containsValidPassword(user)) {
           var updatedPass = passwordEncoder.encode(user.getPassword());
           user.setPassword(updatedPass);
       }
        userRepository.save(user);
    }

    private boolean isNewUserWithoutPassword(User newUser, Optional<User> oldUser) {
        return !isPasswordValid(newUser.getPassword()) && oldUser.isEmpty();
    }

    private boolean containsValidPassword(User newUser) {
        return isPasswordValid(newUser.getPassword());
    }

    private boolean isOldUserPasswordUpdateSkipped(User newUser, Optional<User> oldUser) {
        return newUser.getPassword().equals("") && oldUser.isPresent();
    }

    private boolean isPasswordValid(String password) {
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
