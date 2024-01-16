package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    
    //user login
    @PostMapping("/login")
    public ResponseEntity<Optional<User>> login(@RequestParam("email") String email,
                                        @RequestParam("password") String password) {
    	Optional<User> user = userService.loginUser(email, password);
    	if (user.isPresent()) {
    		return ResponseEntity.ok(user);
    	}else {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
    }

    //get all users
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    //get a user by id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(user);
        } else {
        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //get all tasks for user
    @GetMapping("/{userId}/tasks")
    public ResponseEntity<List<Task>> getUserTasks(@PathVariable Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Task> tasks = user.getTasks();
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //register a new user
    @PostMapping("/")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User createdUser = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    //create a new task for user
    @PostMapping("/{userId}/tasks")
    public ResponseEntity<Task> createTask(@PathVariable Long userId, @RequestBody Task task) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            task.setUserId(userId); // Set the userId directly in the task

            Task savedTask = taskRepository.save(task);
            return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    //get user ID by email
    @GetMapping("/email/{email:.+}")
    public ResponseEntity<Long> getUserIdByEmail(@PathVariable("email") String email) {
        Optional<User> optionalUser = userService.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return ResponseEntity.ok(user.getId());
        } else {
            return ResponseEntity.notFound().build();
        }
    }





}