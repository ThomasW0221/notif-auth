package io.twinterf.notifauth.controllers;

import io.twinterf.notifauth.entities.User;
import io.twinterf.notifauth.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("get /users all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserById(@PathVariable String username) {
        var requestedUser = userService.getByUsername(username);
        if(requestedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(requestedUser);
    }

    //TODO Add requests for update and delete, holding off for now to decide token acceptance strategy
}
