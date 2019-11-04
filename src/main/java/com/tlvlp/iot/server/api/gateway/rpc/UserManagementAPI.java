package com.tlvlp.iot.server.api.gateway.rpc;

import com.tlvlp.iot.server.api.gateway.security.Role;
import com.tlvlp.iot.server.api.gateway.security.User;
import com.tlvlp.iot.server.api.gateway.services.UserManagementException;
import com.tlvlp.iot.server.api.gateway.services.UserManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class UserManagementAPI {

    private UserManagementService userManagementService;

    public UserManagementAPI(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @PostMapping("${API_GATEWAY_API_AUTHENTICATE_USER}")
    public ResponseEntity<User> getUserAfterAuthentication(HttpServletRequest request) {
        try {
            var loggedInUserName = request.getUserPrincipal().getName();
            var user = userManagementService.getUserAfterAuthentication(loggedInUserName);
            return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found");
        }
    }

    @GetMapping("${API_GATEWAY_API_GET_ALL_USERS}")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userManagementService.getAllUsers(), HttpStatus.ACCEPTED);
    }

    @PostMapping("${API_GATEWAY_API_SAVE_USER}")
    public ResponseEntity saveUser(@RequestBody @Valid User user) {
        try {
            userManagementService.saveUser(user);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (UserManagementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    @PostMapping("${API_GATEWAY_API_DELETE_USER}")
    public ResponseEntity deleteUser(@RequestBody User user) {
        userManagementService.deleteUser(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("${API_GATEWAY_API_GET_ROLES}")
    public ResponseEntity<Role[]> getRoles() {
        var roles = Role.values();
        if (roles.length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to retrieve Roles");
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }


}
