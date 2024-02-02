package com.example.demo.controller;

import com.example.demo.jwt.AuthService;
import com.example.demo.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    //user login
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestParam("email") String email,
                                            @RequestParam("password") String password) {
        String token = authService.tokenlogin(email,password);
        if (token!=null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    Collections.singletonMap("accessToken", token));
        }else {
            return new ResponseEntity<>("not found",HttpStatus.NOT_FOUND);
        }
    }

    //register a new user
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody User user) {
        String token = authService.tokenRegister(user);
        if (token!=null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    Collections.singletonMap("accessToken", token));
        }else {
            return new ResponseEntity<>("Email is existed!",HttpStatus.BAD_REQUEST);
        }
    }
}
