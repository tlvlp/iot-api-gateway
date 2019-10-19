package com.tlvlp.iot.server.api.gateway.rpc;

import com.tlvlp.iot.server.api.gateway.persistence.Role;
import com.tlvlp.iot.server.api.gateway.persistence.User;
import com.tlvlp.iot.server.api.gateway.services.UserAuthenticationFailedException;
import com.tlvlp.iot.server.api.gateway.services.UserManagementException;
import com.tlvlp.iot.server.api.gateway.services.UserManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController("/admin")
public class UserManagementAPI {

    private UserManagementService userManagementService;

    public UserManagementAPI(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
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

    @GetMapping("${API_GATEWAY_API_AUTHENTICATE_USER}")
    public ResponseEntity authenticateUser(@RequestParam String userID,
                                   @RequestParam String password) {
        try {
            userManagementService.authenticateUser(userID, password);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found");
        } catch (UserAuthenticationFailedException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
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
