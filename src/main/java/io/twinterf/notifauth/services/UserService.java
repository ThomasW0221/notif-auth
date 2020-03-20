package io.twinterf.notifauth.services;

import io.twinterf.notifauth.entities.User;
import io.twinterf.notifauth.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByUsername(String username) {
        return userRepository.findById(username).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addOrUpdateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }
}
