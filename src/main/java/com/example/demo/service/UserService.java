package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //return the list of users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

   	//return the optional user
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    //register user
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    //delete a user by id
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    //login
    public Optional<User> loginUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    //find a user by their email, ignoring case sensitivity.
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }
}